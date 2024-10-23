package com.flywire.exercise.service;

import com.flywire.exercise.model.Employee;

import java.text.ParseException;
import java.util.List;

public interface EmployeeService {
    List<Employee> getAllEmployees();
    List<Employee> getActiveEmployeesSortedByLastName();
    Employee getEmployeeById(Long id);
    List<String> getDirectReportsNames(Long id);
    List<Employee> getEmployeesByHiredDateRange(String startDate, String endDate) throws ParseException;
    void saveEmployee(Employee employee);
    void deactivateEmployee(Long id);
}
