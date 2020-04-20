package com.rustedbrain.study.employeeservice.service;

import com.rustedbrain.study.employeeservice.model.Department;
import com.rustedbrain.study.employeeservice.repository.DepartmentJdbcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentJdbcRepository repository;

    @Autowired
    public DepartmentServiceImpl(DepartmentJdbcRepository repository) {
        this.repository = repository;
    }

    @Override
    public Department getById(Long id) {
        Optional<Department> optionalDepartment = repository.findById(id);
        return optionalDepartment.orElseThrow();
    }

    @Override
    public Page<Department> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Department create(Department departmentToCreate) {
        return repository.create(departmentToCreate);
    }

    @Override
    public Department update(Long id, Department departmentToUpdate) {
        return repository.update(id, departmentToUpdate);
    }

    @Override
    public void delete(Long id) {
        this.repository.delete(id);
    }
}
