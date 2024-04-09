package com.example.employeepara.controller;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.employeepara.model.Employee;
import com.example.employeepara.service.EmployeeService;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<Response<Object>> addEmployee(@RequestBody Employee employee) {
        if (employee.getName() == null || employee.getName().isEmpty() || employee.getDepartment() == null || employee.getDepartment().isEmpty() || employee.getAge() == 0) {
            Response<Object> nullResponse = new Response<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Any of the fields cannot be empty.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(nullResponse);
        }

        Employee addedEmployee = employeeService.addEmployee(employee);
        if (addedEmployee != null) {
            Response<Object> successResponse = new Response<>(HttpStatus.CREATED.value(), addedEmployee);
            return ResponseEntity.status(HttpStatus.CREATED).body(successResponse);
        } else {
            Response<Object> errorResponse = new Response<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to add employee");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<Object>> getEmployeeById(@PathVariable int id) {
        ResponseEntity<Employee> responseEntity = employeeService.getEmployeeById(id);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            Response<Object> successResponse = new Response<>(responseEntity.getStatusCodeValue(), responseEntity.getBody());
            return ResponseEntity.ok().body(successResponse);
        } else {
            Response<Object> errorResponse = new Response<>(responseEntity.getStatusCodeValue(), "Employee not found");
            return ResponseEntity.status(responseEntity.getStatusCode()).body(errorResponse);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<Object>> deleteEmployeeById(@PathVariable int id) {
        boolean success = employeeService.deleteEmployeeById(id);
        if (success) {
            Response<Object> successResponse = new Response<>(HttpStatus.ACCEPTED.value(), "Employee deleted successfully!");
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(successResponse);
        } else {
            Response<Object> errorResponse = new Response<>(HttpStatus.NOT_FOUND.value(), "Employee not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }


    @Data
    static class Response<T> {
        private int statusCode;
        private T message;

        public Response(int statusCode, T message) {
            this.statusCode = statusCode;
            this.message = message;
        }
    }
}
