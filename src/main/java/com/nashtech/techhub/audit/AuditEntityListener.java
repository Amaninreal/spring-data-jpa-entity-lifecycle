package com.nashtech.techhub.audit;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.time.LocalDateTime;

public class AuditEntityListener {
    @PrePersist
    public void setCreatedAt(Object entity) {
        if (entity instanceof AuditableEntity auditable) {
            LocalDateTime now = LocalDateTime.now();
            auditable.setCreatedTime(now);
            auditable.setUpdatedTime(now);
        }
    }

    @PreUpdate
    public void setUpdatedAt(Object entity) {
        if (entity instanceof AuditableEntity auditable) {
            auditable.setUpdatedTime(LocalDateTime.now());
        }
    }
}
