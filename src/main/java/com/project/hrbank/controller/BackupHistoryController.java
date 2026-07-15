package com.project.hrbank.controller;



import com.project.hrbank.controller.doc.BackupHistoryControllerDoc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api/backups"})
@RequiredArgsConstructor
@Slf4j
public class BackupHistoryController implements BackupHistoryControllerDoc {
}