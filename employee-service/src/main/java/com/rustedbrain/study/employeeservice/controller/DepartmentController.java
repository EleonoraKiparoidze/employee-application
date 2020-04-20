package com.rustedbrain.study.employeeservice.controller;

import com.rustedbrain.study.employeeservice.converter.DepartmentDtoToDepartmentBiConverter;
import com.rustedbrain.study.employeeservice.model.Department;
import com.rustedbrain.study.employeeservice.model.DepartmentDto;
import com.rustedbrain.study.employeeservice.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/departments")
@CrossOrigin(origins = "*", maxAge = 3600)
public class DepartmentController {

    private final DepartmentService departmentService;

    private final DepartmentDtoToDepartmentBiConverter converter;

    @Autowired
    public DepartmentController(DepartmentService departmentService, DepartmentDtoToDepartmentBiConverter converter) {
        this.departmentService = departmentService;
        this.converter = converter;
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentDto> getById(@PathVariable Long id) {
        Department department = departmentService.getById(id);
        return ResponseEntity.ok(converter.convertFrom(department));
    }

    @GetMapping
    public ResponseEntity<Page<DepartmentDto>> getAll(@PageableDefault(size = 5) Pageable pageable) {
        Page<DepartmentDto> departmentDtoPage = departmentService.getAll(pageable).map(converter::convertFrom);
        return ResponseEntity.ok(departmentDtoPage);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        departmentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<DepartmentDto> create(@Valid @RequestBody DepartmentDto departmentDto) {
        Department departmentToCreate = converter.convert(departmentDto);
        Department createdDepartment = departmentService.create(departmentToCreate);
        return ResponseEntity.ok(converter.convertFrom(createdDepartment));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartmentDto> create(@PathVariable Long id, @RequestBody DepartmentDto departmentDto) {
        Department departmentToUpdate = converter.convert(departmentDto);
        Department updatedDepartment = departmentService.update(id, departmentToUpdate);
        return ResponseEntity.ok(converter.convertFrom(updatedDepartment));
    }
}
