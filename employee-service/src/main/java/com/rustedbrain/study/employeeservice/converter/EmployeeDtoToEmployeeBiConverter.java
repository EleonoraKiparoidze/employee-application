package com.rustedbrain.study.employeeservice.converter;

import com.rustedbrain.study.employeeservice.model.Employee;
import com.rustedbrain.study.employeeservice.model.EmployeeDto;
import org.springframework.stereotype.Component;

@Component
public class EmployeeDtoToEmployeeBiConverter implements BiConverter<EmployeeDto, Employee> {

    @Override
    public Employee convert(EmployeeDto employeeDto) {
        return Employee.builder()
                .id(employeeDto.getId())
                .name(employeeDto.getName())
                .active(employeeDto.getActive())
                .departmentId(employeeDto.getDepartmentId())
                .build();
    }

    @Override
    public EmployeeDto convertFrom(Employee employee) {
        return EmployeeDto.builder()
                .id(employee.getId())
                .name(employee.getName())
                .active(employee.getActive())
                .departmentId(employee.getDepartmentId())
                .build();
    }
}
