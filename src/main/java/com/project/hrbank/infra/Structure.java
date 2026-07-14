package com.project.hrbank.infra;

import org.springframework.http.ResponseEntity;

import java.io.InputStream;
import java.nio.file.Path;

/**
 * - 생성 (쓰기)<br>
 * - 파일 메타데이터 DB 작성<br>
 * - 조회 (다운로드)<br>
 * - (삭제)<br>
 * - (파일 메타데이터 DB 삭제)<br>
 */

public interface Structure {

    String put(String fileName, byte[] bytes);

    InputStream get(Path path);

    ResponseEntity<?> download(String fileName);

    void delete(String savePath);

//    OutputStream set(Long fileId);

//    void write(Long fileId, byte[] bytes);


    String getNotDuplicateFileName(String fileName);

    String resolvePath(String fileName);
}
