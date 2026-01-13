package com.nashtech.techhub.controller;

import com.nashtech.techhub.dto.EmployeeRequest;
import com.nashtech.techhub.dto.EmployeeResponse;
import com.nashtech.techhub.services.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Employee Management", description = "Operations with audit and soft delete")
@RestController
@RequestMapping("api/employees")
public class EmployeeController {

    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    @Operation(summary = "Get all active employees")
    @GetMapping
    public ResponseEntity<List<EmployeeResponse>> getEmployees() {
        List<EmployeeResponse> employees = service.getAllActiveEmployees();
        return ResponseEntity.ok(employees);
    }

    @Operation(summary = "Create a new employee")
    @PostMapping
    public ResponseEntity<EmployeeResponse> create(@Valid @RequestBody EmployeeRequest request) {
        EmployeeResponse saved = service.createEmployee(request);
        return ResponseEntity.ok(saved);
    }

    @Operation(summary = "Update an existing employee")
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponse> update(@PathVariable Long id, @Valid @RequestBody EmployeeRequest request) {
        EmployeeResponse updated = service.updateEmployee(id, request);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Soft delete an employee")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.softDelete(id);
        return ResponseEntity.ok().build();
    }
}
