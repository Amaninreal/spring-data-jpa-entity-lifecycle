package com.nashtech.techhub.services.implementations;

import com.nashtech.techhub.dto.EmployeeRequest;
import com.nashtech.techhub.dto.EmployeeResponse;
import com.nashtech.techhub.entity.Employee;
import com.nashtech.techhub.exception.EmployeeNotFoundException;
import com.nashtech.techhub.repository.EmployeeRepository;
import com.nashtech.techhub.services.EmployeeService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public List<EmployeeResponse> getAllActiveEmployees() {
        return employeeRepository.findAllActive()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeResponse createEmployee(EmployeeRequest request) {
        Employee employee = new Employee();
        employee.setName(request.getName());
        employee.setRole(request.getRole());
        Employee saved = employeeRepository.save(employee);
        return mapToResponse(saved);
    }

    @Override
    public EmployeeResponse updateEmployee(Long id, EmployeeRequest request) {
        Employee existing = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
        existing.setName(request.getName());
        existing.setRole(request.getRole());
        Employee updated = employeeRepository.save(existing);
        return mapToResponse(updated);
    }

    @Override
    public void softDelete(Long id) {
        Employee existing = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
        existing.setDeleted(true);
        existing.setDeletedTime(LocalDateTime.now());
        employeeRepository.save(existing);
    }


    // utility method
    private EmployeeResponse mapToResponse(Employee employee) {
        EmployeeResponse response = new EmployeeResponse();
        response.setId(employee.getId());
        response.setName(employee.getName());
        response.setRole(employee.getRole());
        response.setCreatedTime(employee.getCreatedTime());
        response.setUpdatedTime(employee.getUpdatedTime());
        return response;
    }
}
