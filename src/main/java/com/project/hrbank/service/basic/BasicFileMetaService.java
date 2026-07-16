package com.project.hrbank.service.basic;

import com.project.hrbank.domain.FileMeta;
import com.project.hrbank.exception.FileNotExistException;
import com.project.hrbank.infra.Structure;
import com.project.hrbank.repository.FileMetaRepository;
import com.project.hrbank.service.FileMetaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BasicFileMetaService implements FileMetaService {

    private final FileMetaRepository fileMetaRepository;
    private final Structure structure;

    @Override
    public ResponseEntity<?> download(Long id) {
        FileMeta fileMeta = getFileMetaOrExcept(id);
        return structure.download(fileMeta.getFileName(), fileMeta.getFileType());
    }

    private FileMeta getFileMetaOrExcept(Long id) {
        return fileMetaRepository.findById(id)
                .orElseThrow(() -> new FileNotExistException("파일이 존재하지 않습니다. - " + id, "File not exists"));
    }
}