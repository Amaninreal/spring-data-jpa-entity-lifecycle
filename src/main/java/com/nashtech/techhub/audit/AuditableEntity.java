package com.nashtech.techhub.audit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditEntityListener.class)
public abstract class AuditableEntity {

    @CreatedDate
    @Column(updatable = false)
    protected LocalDateTime createdTime;

    @LastModifiedDate
    protected LocalDateTime updatedTime;

    @JsonIgnore
    protected LocalDateTime deletedTime;

    @JsonIgnore
    protected boolean isDeleted = false;
}