package com.rustedbrain.study.employeeservice.service;

import com.rustedbrain.study.employeeservice.model.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DepartmentService {
    Department getById(Long id);

    Page<Department> getAll(Pageable pageable);

    Department create(Department departmentToCreate);

    void delete(Long id);

    Department update(Long id, Department departmentToUpdate);
}
