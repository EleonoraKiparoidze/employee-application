package com.rustedbrain.study.employeeservice.controller;

import com.rustedbrain.study.employeeservice.converter.DepartmentDtoToDepartmentBiConverter;
import com.rustedbrain.study.employeeservice.converter.EmployeeDtoToEmployeeBiConverter;
import com.rustedbrain.study.employeeservice.model.Department;
import com.rustedbrain.study.employeeservice.model.DepartmentDto;
import com.rustedbrain.study.employeeservice.model.Employee;
import com.rustedbrain.study.employeeservice.model.EmployeeDto;
import com.rustedbrain.study.employeeservice.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employees")
@CrossOrigin(origins = "*", maxAge = 3600)
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EmployeeDtoToEmployeeBiConverter employeeBiConverter;
    private final DepartmentDtoToDepartmentBiConverter departmentBiConverter;

    @Autowired
    public EmployeeController(EmployeeService employeeService, EmployeeDtoToEmployeeBiConverter employeeBiConverter,
                              DepartmentDtoToDepartmentBiConverter departmentBiConverter) {
        this.employeeService = employeeService;
        this.employeeBiConverter = employeeBiConverter;
        this.departmentBiConverter = departmentBiConverter;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pair<EmployeeDto, DepartmentDto>> getById(@PathVariable Long id) {
        Pair<Employee, Department> departmentEmployee = employeeService.getById(id);
        Pair<EmployeeDto, DepartmentDto> departmentEmployeeDto = Pair.of(
                employeeBiConverter.convertFrom(departmentEmployee.getFirst()),
                departmentBiConverter.convertFrom(departmentEmployee.getSecond())
        );
        return ResponseEntity.ok(departmentEmployeeDto);
    }

    @GetMapping
    public ResponseEntity<Page<Pair<EmployeeDto, DepartmentDto>>> findAll(@PageableDefault(size = 5) Pageable pageable) {
        Page<Pair<EmployeeDto, DepartmentDto>> departmentEmployeePage = employeeService.findAll(pageable)
                .map(pair -> Pair.of(
                        employeeBiConverter.convertFrom(pair.getFirst()),
                        departmentBiConverter.convertFrom(pair.getSecond())
                ));
        return ResponseEntity.ok(departmentEmployeePage);
    }

    @PostMapping
    public ResponseEntity<EmployeeDto> create(@RequestBody EmployeeDto employeeDto) {
        Employee employee = employeeBiConverter.convert(employeeDto);
        Employee createdEmployee = employeeService.create(employee);
        return ResponseEntity.ok(employeeBiConverter.convertFrom(createdEmployee));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDto> update(@PathVariable Long id, @RequestBody EmployeeDto employeeDto) {
        Employee employee = employeeBiConverter.convert(employeeDto);
        Employee updatedEmployee = employeeService.update(id, employee);
        return ResponseEntity.ok(employeeBiConverter.convertFrom(updatedEmployee));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        employeeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
