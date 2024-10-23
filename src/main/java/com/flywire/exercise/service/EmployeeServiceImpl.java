package com.flywire.exercise.service;

import com.flywire.exercise.model.Employee;
import com.flywire.exercise.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public List<Employee> getActiveEmployeesSortedByLastName() {
        return employeeRepository.findAll().stream()
                .filter(Employee::isActive)
                .sorted(Comparator.comparing(employee -> {
                    String[] nameParts = employee.getName().split(" ");
                    return nameParts[nameParts.length - 1];
                }))
                .collect(Collectors.toList());
    }

    @Override
    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    @Override
    public List<String> getDirectReportsNames(Long id) {
        Employee employee = employeeRepository.findById(id);
        if (employee != null && employee.getDirectReports() != null) {
            return employee.getDirectReports().stream()
                    .map(this::getEmployeeById)
                    .filter(Objects::nonNull)
                    .map(Employee::getName)
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    @Override
    public List<Employee> getEmployeesByHiredDateRange(String startDate, String endDate) throws ParseException {
        Date start = dateFormat.parse(startDate);
        Date end = dateFormat.parse(endDate);

        return employeeRepository.findAll().stream()
                .filter(employee -> !employee.getParsedHireDate().before(start) && !employee.getParsedHireDate().after(end))
                .sorted((e1, e2) -> e2.getParsedHireDate().compareTo(e1.getParsedHireDate()))
                .collect(Collectors.toList());
    }

    @Override
    public void saveEmployee(Employee employee) {
        employeeRepository.saveEmployee(employee);
    }

    @Override
    public void deactivateEmployee(Long id) {
        employeeRepository.deactivateEmployeeById(id);
    }

}
