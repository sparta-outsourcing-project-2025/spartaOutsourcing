package com.example.spartaoutsourcing.common.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public class AuditableEntity extends BaseEntity {

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    private LocalDateTime deletedAt;
}