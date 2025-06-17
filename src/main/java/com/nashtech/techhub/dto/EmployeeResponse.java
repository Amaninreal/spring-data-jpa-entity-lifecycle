package com.nashtech.techhub.dto;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EmployeeResponse {
    private Long id;
    private String name;
    private String role;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}
