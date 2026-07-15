package com.project.hrbank.controller;

import com.project.hrbank.controller.doc.FileMetaControllerDoc;
import com.project.hrbank.domain.FileMeta;
import com.project.hrbank.service.FileMetaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
@Slf4j
public class FileMetaController implements FileMetaControllerDoc {
    private FileMetaService fileMetaService;

    @GetMapping("/{id}/download")
    public ResponseEntity<?> fileDownload(@PathVariable Long id) {
        return fileMetaService.download(id);
    }
}