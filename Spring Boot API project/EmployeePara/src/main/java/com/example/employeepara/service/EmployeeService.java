package com.example.employeepara.service;

import com.example.employeepara.model.Employee;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class EmployeeService {
    private final String JSON_FILE_PATH = "employees.json";
    private Map<Integer, Employee> employees;

    public EmployeeService() {
        try {
            employees = readEmployeesFromFile();
        } catch (IOException e) {
            employees = new HashMap<>();
        }
    }

    public Employee addEmployee(Employee employee) {
        int nextId = generateNextId();
        employee.setId(nextId);
        employees.put(nextId, employee);
        try {
            writeEmployeesToFile(employees);
            return employee;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public ResponseEntity<Employee> getEmployeeById(int id) {
        Employee employee = employees.get(id);
        if (employee != null) {
            return ResponseEntity.ok().body(employee);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    public boolean deleteEmployeeById(int id) {
        if (employees.containsKey(id)) {
            employees.remove(id);
            try {
                writeEmployeesToFile(employees);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    private int generateNextId() {
        return employees.isEmpty() ? 1 : employees.keySet().stream().max(Integer::compareTo).orElse(0) + 1;
    }

    private Map<Integer, Employee> readEmployeesFromFile() throws IOException {
        File file = new File(JSON_FILE_PATH);
        if (!file.exists() || file.length() == 0) {
            return new HashMap<>();
        }
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(file, new TypeReference<>() {
        });
    }

    private void writeEmployeesToFile(Map<Integer, Employee> employees) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.writeValue(new File(JSON_FILE_PATH), employees);
    }

}
