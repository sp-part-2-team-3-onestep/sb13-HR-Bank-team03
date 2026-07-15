package com.project.hrbank.service.basic;


import com.project.hrbank.config.DataCondition;
import com.project.hrbank.domain.*;
import com.project.hrbank.dto.request.BackupHistorySearchRequest;
import com.project.hrbank.dto.response.BackupDto;
import com.project.hrbank.dto.response.CursorPageResponse;
import com.project.hrbank.exception.BackupHistoryAlreadyRunningExcption;
import com.project.hrbank.exception.BackupHistoryStatusException;
import com.project.hrbank.infra.Structure;
import com.project.hrbank.mapper.DtoMapper;
import com.project.hrbank.repository.*;
import com.project.hrbank.service.BackupHistoryService;
import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Transactional
public class BasicBackupHistoryService implements BackupHistoryService {
    private final BackupHistoryRepository backupHistoryRepository;
    private final EmployeeRepository employeeRepository;
    private final FileMetaRepository fileMetaRepository;
    private final Structure structure;
    private final DtoMapper dtoMapper;
    private final DataCondition dataCondition;

    private boolean lock = false;


    // 내부 백업 결과 반환용 이너클래스.
    // 그럴 일 은 없지만, 외부 클래스의 GC 를 위해 static(record) 선언
    private record BackupTfo(long result, FileMeta fileMeta) {}

    @Scheduled(     // spring batch scheduler
            fixedRateString = "${hrbank.batch:3600}",
            initialDelay = 10,
            timeUnit = TimeUnit.SECONDS
    )
    protected void batchCreate(){
        create("system");
    }


    @Override
    // ?? 400 에러 요청할 부분이 없음
    public BackupDto create(String workerIp){
        Instant backupStart = Instant.now();

        // 백업 시작 상태 등록
        BackupHistory currentBackup = backupHistoryRepository.save(new BackupHistory(
                workerIp,
                backupStart,
                null,
                BackupStatus.RUNNING
        ));

        BackupTfo res = createBackup();

        // result
        switch ((int) res.result()){
            case 0 -> {     // success
                currentBackup.setEndTime(Instant.now());
                currentBackup.setBackupStatus(BackupStatus.DONE);
                currentBackup.setFileMeta(res.fileMeta());
                return dtoMapper.toDto(backupHistoryRepository.save(currentBackup));
            }
            case 1 -> {     // skipped
                currentBackup.setEndTime(Instant.now());
                currentBackup.setBackupStatus(BackupStatus.SKIP);
                currentBackup.setFileMeta(res.fileMeta());
                return dtoMapper.toDto(backupHistoryRepository.save(currentBackup));
            }
            default -> {    // error
                currentBackup.setEndTime(Instant.now());
                currentBackup.setBackupStatus(BackupStatus.FAIL);
                currentBackup.setFileMeta(res.fileMeta());
                return dtoMapper.toDto(backupHistoryRepository.save(currentBackup));
            }
        }
    }


    @Override
    public BackupDto getLatestBackup(String status){
        BackupStatus filter;
        switch (status){
            case "COMPLETED" -> filter = BackupStatus.DONE;
            case "FAILED" -> filter = BackupStatus.FAIL;
            case "IN_PROGRESS" -> filter = BackupStatus.RUNNING;
            default -> throw new BackupHistoryStatusException("잘못된 요청입니다","not current request");
        }

        BackupHistory bu = backupHistoryRepository.findFirstByBackupStatus(filter);
        return dtoMapper.toDto(bu);
    }

    public CursorPageResponse<BackupDto> getBackupList(
        BackupHistorySearchRequest request,
        int pageSize,
        String sort,
        String direction
    ){
        Slice<BackupHistory> res = backupHistoryRepository.backupHistory(request,pageSize,sort,direction);

        List<BackupDto> content = res.getContent().stream().map(dtoMapper::toDto).toList();

        BackupDto last = !content.isEmpty() ? content.get(content.size() - 1) :  null;

        String cursor = null;
        if (last != null) {
            if (sort.equals("startedAt")) cursor = last.startedAt().toString();
            else cursor = last.endedAt().toString();
        }

        return new CursorPageResponse<BackupDto>(
                content,
                cursor,
                last == null ? null : last.id(),
                content.size(),
                backupHistoryRepository.count(),
                res.hasNext()
        );
    }








    private BackupTfo createBackup(){
        if (lock) throw new BackupHistoryAlreadyRunningExcption("백업이 진행중 입니다.","backup already running");
        else lock = true;

        if (!dataCondition.checkDataChanged()){
            lock = false;
            return new BackupTfo(1,null);
        }


        String filename = structure.getNotDuplicateFileName("backup");
        Path path = Paths.get(structure.resolvePath(filename));

        long backupSize = createBackupFile(path);
        BackupTfo res;
        if (backupSize < 0) {
            res = new BackupTfo(backupSize,null);
            structure.delete(path.toString());   // 에러라면 파일 삭제.
        } else {
            FileMeta fileMeta = fileMetaRepository.save(new FileMeta(filename,".cvs",backupSize));
            res = new BackupTfo(0, fileMeta);
        }

        // 데이터 컨디션, 작업관리 체크
        dataCondition.flagSetUnchanged();
        lock = false;
        return res;
    }


    private long createBackupFile(Path filepath){
        int pageSize = 100;
        int pageNumber = 0;
        boolean header = false;
        long size = 0L;
        // Structure 쪽 매서드 (output 스트림 관련)
        try (
                OutputStream out = Files.newOutputStream(filepath, StandardOpenOption.APPEND);
                BufferedOutputStream but = new BufferedOutputStream(out)
        ){
            // 페이지네이션으로 가져오고, 스트림에 써넣는다. 페이지가 끝날 때 까지.
            while(true){
                Page<Employee> list = employeeRepository.findAllBy(
                        PageRequest.of(pageNumber, pageSize)
                );
                if (list.isEmpty()) break;

                // list 를 csv 로 변환 후 쓰기
                // 헤더
                if (!header) {
                    header = true;
                    byte[] headerBytes = getCvsHeader(list.getContent().get(0));
                    but.write(headerBytes);
                    size += headerBytes.length;
                }

                // 바디
                for (Employee employee : list.getContent()) {
                    byte[] bodyBytes = getCvsBody(employee);
                    but.write(bodyBytes);
                    size += bodyBytes.length;
                }

                // 다 쓰면 루프 아웃
                if (!list.hasNext()) break;
                pageNumber++;
            }

            return size;

        } catch (IOException | RuntimeException e){
            // 파일 작성 에러. 작성된 파일의 삭제 필요.
            return -1L;
        }
    }





    private <T> byte[] getCvsHeader(T entity){
        StringBuilder header = new StringBuilder();
        for (Field field : getFields(entity)) {
            header.append(field.getName()).append(",");
        }
        header.deleteCharAt(header.length() - 1);
        return header.toString().getBytes(StandardCharsets.UTF_8);
    }

    private <T> byte[] getCvsBody(T entity){
        Class<?> cls = entity.getClass();
        StringBuilder body = new StringBuilder();
        for (Field field : getFields(entity)) {
            body.append(getFieldValue(cls,field.getName())).append(",");
        }
        body.deleteCharAt(body.length() - 1);
        return body.toString().getBytes(StandardCharsets.UTF_8);
    }

    private <T> Field[] getFields(T entity){
        Class<?> cls = entity.getClass();
        return cls.getFields();
    }

    private Object getFieldValue(Class<?> cls, String fieldName){
        try{
            return cls.getField(fieldName);
        } catch (NoSuchFieldException e){
            return null;
        }
    }
}
