package com.nashtech.techhub.service;

import com.nashtech.techhub.dto.EmployeeRequest;
import com.nashtech.techhub.dto.EmployeeResponse;
import com.nashtech.techhub.entity.Employee;
import com.nashtech.techhub.exception.EmployeeNotFoundException;
import com.nashtech.techhub.repository.EmployeeRepository;
import com.nashtech.techhub.services.implementations.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link EmployeeServiceImpl} using Unit 5 and Mockito.
 * This test suite verifies the behavior of the service layer, ensuring
 * that Employee entities are handled correctly according to business logic.
 */
@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;

    /**
     * Initializes a sample Employee entity before each test.
     */
    @BeforeEach
    void setup() {
        employee = new Employee();
        employee.setId(1L);
        employee.setName("Aman");
        employee.setRole("QA");
        employee.setCreatedTime(LocalDateTime.now());
        employee.setUpdatedTime(LocalDateTime.now());
    }

    /**
     * Test case for fetching all active employees.
     * Verifies that the repository is queried and the correct response is returned.
     */
    @Test
    void testGetAllActiveEmployees() {
        when(employeeRepository.findAllActive()).thenReturn(List.of(employee));

        List<EmployeeResponse> responses = employeeService.getAllActiveEmployees();

        assertEquals(1, responses.size());
        assertEquals("Aman", responses.getFirst().getName());
        verify(employeeRepository, times(1)).findAllActive();
    }

    /**
     * Test case for creating a new employee.
     * Ensures the repository's save method is called and the response is mapped properly.
     */
    @Test
    void testCreateEmployee() {
        EmployeeRequest request = new EmployeeRequest();
        request.setName("John");
        request.setRole("Dev");

        Employee saved = new Employee();
        saved.setId(2L);
        saved.setName("John");
        saved.setRole("Dev");

        when(employeeRepository.save(any(Employee.class))).thenReturn(saved);

        EmployeeResponse response = employeeService.createEmployee(request);

        assertEquals("John", response.getName());
        assertEquals("Dev", response.getRole());
        verify(employeeRepository).save(any(Employee.class));
    }

    /**
     * Test case for updating an existing employee.
     * Ensures existing employee is found, updated, and saved correctly.
     */
    @Test
    void testUpdateEmployee() {
        EmployeeRequest request = new EmployeeRequest();
        request.setName("Updated Name");
        request.setRole("Lead");

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        EmployeeResponse response = employeeService.updateEmployee(1L, request);

        assertEquals("Updated Name", response.getName());
        assertEquals("Lead", response.getRole());
        verify(employeeRepository).save(employee);
    }

    /**
     * Test case for updating an employee that does not exist.
     * Expects EmployeeNotFoundException to be thrown.
     */
    @Test
    void testUpdateEmployee_NotFound() {
        when(employeeRepository.findById(999L)).thenReturn(Optional.empty());

        EmployeeRequest request = new EmployeeRequest();
        request.setName("Ghost");
        request.setRole("None");

        assertThrows(EmployeeNotFoundException.class, () ->
                employeeService.updateEmployee(999L, request));
    }

    /**
     * Test case for soft deleting an existing employee.
     * Verifies that the 'deleted' flag and timestamp are set.
     */
    @Test
    void testSoftDeleteEmployee() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        employeeService.softDelete(1L);

        ArgumentCaptor<Employee> captor = ArgumentCaptor.forClass(Employee.class);
        verify(employeeRepository).save(captor.capture());

        Employee softDeleted = captor.getValue();
        assertTrue(softDeleted.isDeleted());
        assertNotNull(softDeleted.getDeletedTime());
    }

    /**
     * Test case for soft deleting a non-existing employee.
     * Expects EmployeeNotFoundException to be thrown.
     */
    @Test
    void testSoftDeleteEmployee_NotFound() {
        when(employeeRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(EmployeeNotFoundException.class, () ->
                employeeService.softDelete(999L));
    }
}
