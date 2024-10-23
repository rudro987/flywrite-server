package com.flywire.exercise.controller;

import com.flywire.exercise.model.Employee;
import com.flywire.exercise.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createEmployee(@RequestBody Employee employee) {
        if (employee.getName() == null || employee.getPosition() == null || employee.getId() == null) {
            return new ResponseEntity<>("Invalid data: 'name', 'position', and 'id' are required", HttpStatus.BAD_REQUEST);
        }

        Employee existingEmployee = employeeService.getEmployeeById(employee.getId());
        if (existingEmployee != null) {
            return new ResponseEntity<>("Employee with ID " + employee.getId() + " already exists", HttpStatus.CONFLICT);
        }

        employeeService.saveEmployee(employee);
        return new ResponseEntity<>("Employee created successfully", HttpStatus.CREATED);
    }

    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/active")
    public  List<Employee> getActiveEmployeesSorted() {
        return employeeService.getActiveEmployeesSortedByLastName();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getEmployeeDetailsWithDirectReportsById(@PathVariable Long id) {
        Employee employee = employeeService.getEmployeeById(id);

        if (employee == null) {
            return ResponseEntity.notFound().build();
        }

        List<String> directReportNames = employeeService.getDirectReportsNames(id);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("id", employee.getId());
        responseBody.put("name", employee.getName());
        responseBody.put("position", employee.getPosition());
        responseBody.put("hireDate", employee.getHireDate());
        responseBody.put("active", employee.isActive());
        responseBody.put("directReports", directReportNames);


        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @GetMapping("/sorted-by-hireDate")
    public List<Employee> getEmployeesByHireDateRange(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) throws ParseException {
        return employeeService.getEmployeesByHiredDateRange(startDate, endDate);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> deactivateEmployee(@PathVariable Long id) {
        Employee employee = employeeService.getEmployeeById(id);

        if (employee == null) {
            return new ResponseEntity<>("Employee not found", HttpStatus.NOT_FOUND);
        }

        if (!employee.isActive()) {
            return new ResponseEntity<>("Employee is already deactivated", HttpStatus.BAD_REQUEST);
        }

        employeeService.deactivateEmployee(id);
        return new ResponseEntity<>("Employee deactivated successfully", HttpStatus.OK);
    }

}
