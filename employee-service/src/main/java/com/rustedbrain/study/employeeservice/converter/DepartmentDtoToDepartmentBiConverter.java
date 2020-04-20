package com.rustedbrain.study.employeeservice.converter;

import com.rustedbrain.study.employeeservice.model.Department;
import com.rustedbrain.study.employeeservice.model.DepartmentDto;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class DepartmentDtoToDepartmentBiConverter implements BiConverter<DepartmentDto, Department> {

    @NonNull
    @Override
    public Department convert(DepartmentDto departmentDto) {
        return Department.builder()
                .id(departmentDto.getId())
                .name(departmentDto.getName())
                .build();
    }

    @NonNull
    @Override
    public DepartmentDto convertFrom(Department department) {
        return DepartmentDto.builder()
                .id(department.getId())
                .name(department.getName())
                .build();
    }
}
