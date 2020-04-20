package com.rustedbrain.study.employeeservice.service;

import com.rustedbrain.study.employeeservice.model.Department;
import com.rustedbrain.study.employeeservice.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;

public interface EmployeeService {
    Page<Pair<Employee, Department>> findAll(Pageable pageable);

    Employee create(Employee employee);

    Pair<Employee, Department> getById(Long id);

    void delete(Long id);

    Employee update(Long id, Employee employee);
}
