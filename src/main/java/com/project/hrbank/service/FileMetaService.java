package com.project.hrbank.service;

import org.springframework.http.ResponseEntity;

public interface FileMetaService {

    ResponseEntity<?> download(Long id);
}