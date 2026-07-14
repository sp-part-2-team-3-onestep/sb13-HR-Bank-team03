package com.project.hrbank.infra;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Component
@ConditionalOnProperty(name = "hrbank.infra.type", havingValue = "local")
public class LocalStructure implements Structure {

    @Value("${hrbank.infra.local.root-path:./storage}")
    private Path rootPath;

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(rootPath);
        } catch (IOException e) {
            throw new UncheckedIOException("스토리지 디렉토리를 생성할 수 없습니다: ", e);
        }
    }

    @Override
    public String put(String fileName, byte[] bytes) {

        String savedFileName = UUID.randomUUID() + "_" + fileName;
        Path path = rootPath.resolve(savedFileName);

        try {
            Files.write(path,bytes);
        } catch (IOException e) {
            throw new UncheckedIOException("파일 저장에 실패했습니다: " + fileName, e);
        }

        return savedFileName;
    }


//    public void write(Path path, InputStream in) {
//        try(OutputStream out = out(path)){
//           in.transferTo(out);
//        } catch(IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private OutputStream out(Path path) throws IOException {
//            OutputStream out = Files.newOutputStream(path);
//            return new BufferedOutputStream(out);
//    }



    @Override
    public InputStream get(Path path) {
        try {
            return Files.newInputStream(path);
        } catch (IOException e) {
            throw new UncheckedIOException("파일을 읽을 수 없습니다: " + path, e);
        }
    }

    @Override
    public ResponseEntity<Resource> download(String fileName) {
        String savedFileName = UUID.randomUUID() + "_" + fileName;
        Path path = rootPath.resolve(savedFileName);
        InputStream inputStream = get(path);
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        new InputStreamResource(inputStream)
                );
    }

    @Override
    public void delete(String savePath) {
        Path path = rootPath.resolve(savePath);

        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new UncheckedIOException("파일 삭제에 실패했습니다: " + savePath, e);
        }
    }
}
