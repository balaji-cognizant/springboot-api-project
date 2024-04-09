package com.example.employeepara.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Employee {
    private int id;
    private String name;
    private String department;
    private int age;
    public Employee(int id,String name,String department,int age)
    {
        this.id = Integer.parseInt(sanitizeInput(String.valueOf(id)));
        this.name = sanitizeInput(name);
        this.age = Integer.parseInt(sanitizeInput(String.valueOf(age)));
        this.department = sanitizeInput(department);
    }

    private String sanitizeInput(String input) {
        if (input == null) {
            return null;
        }

        // Remove leading and trailing whitespace
        String sanitizedInput = input.trim();

        // Remove potential malicious HTML tags and attributes
        sanitizedInput = sanitizedInput.replaceAll("(?i)<(\\/)?[a-z][^>]*?>", "");

        // Remove potential SQL injection characters
        sanitizedInput = sanitizedInput.replace("'", "''");

        // Remove potential command injection characters
        sanitizedInput = sanitizedInput.replace("[;&|`$]", "");

        // Remove specific HTML tags
        sanitizedInput = sanitizedInput.replace("<script>", "")
                .replace("</script>", "")
                .replace("<.*?>", "");

        int maxLength = 1000;
        if (sanitizedInput.length() > maxLength) {
            sanitizedInput = sanitizedInput.substring(0, maxLength);
        }

        return sanitizedInput;
    }
}
