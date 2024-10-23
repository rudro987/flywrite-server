package com.flywire.exercise.model;

import lombok.Getter;
import lombok.Setter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class Employee {
    private Long id;
    private String name;
    private String position;
    private String hireDate;
    private boolean active;
    private List<Long> directReports;

    public Date getParsedHireDate() {
        try {
            SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");
            return dateFormatter.parse(hireDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
