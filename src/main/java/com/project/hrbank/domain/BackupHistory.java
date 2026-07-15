package com.project.hrbank.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BackupHistory extends Base {

    @Column(length = 50)
    private String ip;

    @Column
    private Instant startTime;

    @Column
    private Instant endTime;

    @Column
    @Enumerated(EnumType.STRING)
    private BackupStatus backupStatus;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private FileMeta fileMeta;

    public BackupHistory(
        String worker,
        Instant startTime,
        Instant endTime,
        BackupStatus backupStatus) {
        super();

    }

}