package com.rustedbrain.study.employeeservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeDto {

    private Long id;
    @NotBlank
    @Size(min = 2, max = 64)
    private String name;
    private Boolean active;
    private Long departmentId;
}
