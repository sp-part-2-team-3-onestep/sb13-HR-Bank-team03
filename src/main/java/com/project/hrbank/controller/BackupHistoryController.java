package com.project.hrbank.controller;



import com.project.hrbank.controller.doc.BackupHistoryControllerDoc;
import com.project.hrbank.dto.response.BackupDto;
import com.project.hrbank.service.BackupHistoryService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/api/backups"})
@RequiredArgsConstructor
@Slf4j
public class BackupHistoryController implements BackupHistoryControllerDoc {
    private final BackupHistoryService backupHistoryService;

    @PostMapping
    public ResponseEntity<BackupDto> create(
            HttpServletRequest request
    ){
        String workerIp = request.getRemoteAddr();

        return ResponseEntity.ok(backupHistoryService.create(workerIp));
    }

    @GetMapping("/latest")
    public ResponseEntity<BackupDto> latest(
            @RequestParam(defaultValue = "COMPLETED") String status // COMPLETED, FAILED, IN_PROGRESS,
    ){
        return ResponseEntity.ok(backupHistoryService.getLatestBackup(status));
    }

}
