package com.rustedbrain.study.employeeservice.service;

import com.rustedbrain.study.employeeservice.model.Department;
import com.rustedbrain.study.employeeservice.model.Employee;
import com.rustedbrain.study.employeeservice.repository.EmployeeJdbcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeJdbcRepository repository;

    @Autowired
    public EmployeeServiceImpl(EmployeeJdbcRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<Pair<Employee, Department>> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Employee create(Employee employee) {
        return repository.create(employee);
    }

    @Override
    public Pair<Employee, Department> getById(Long id) {
        return repository.getById(id);
    }

    @Override
    public void delete(Long id) {
        repository.delete(id);
    }

    @Override
    public Employee update(Long id, Employee updateToEmployee) {
        repository.update(id, updateToEmployee);
        return updateToEmployee;
    }
}
