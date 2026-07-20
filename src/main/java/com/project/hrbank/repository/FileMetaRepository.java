package com.project.hrbank.repository;

import com.project.hrbank.domain.FileMeta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileMetaRepository extends JpaRepository<FileMeta, Long> {
}