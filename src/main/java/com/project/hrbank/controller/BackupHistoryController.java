package com.project.hrbank.controller;



import com.project.hrbank.controller.doc.BackupHistoryControllerDoc;
import com.project.hrbank.dto.request.BackupHistorySearchRequest;
import com.project.hrbank.dto.response.BackupDto;
import com.project.hrbank.dto.response.CursorPageResponse;
import com.project.hrbank.service.BackupHistoryService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

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

    @GetMapping
    public ResponseEntity<CursorPageResponse<BackupDto>> list(
            @RequestParam(required = false) String worker,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Instant startedAtFrom,
            @RequestParam(required = false) Instant startedAtTo,
            @RequestParam(required = false) Long idAfter,
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "startedAt") String sortField,
            @RequestParam(defaultValue = "DESC") String sortDirection
            ){
        BackupHistorySearchRequest req = new BackupHistorySearchRequest(
                worker, status, startedAtFrom, startedAtTo, idAfter, cursor
        );

        return ResponseEntity.ok(backupHistoryService.getBackupList(req, size, sortField, sortDirection));

    }

}
