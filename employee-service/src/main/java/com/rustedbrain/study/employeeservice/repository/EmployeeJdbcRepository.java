package com.rustedbrain.study.employeeservice.repository;

import com.rustedbrain.study.employeeservice.model.Department;
import com.rustedbrain.study.employeeservice.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Pair;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class EmployeeJdbcRepository {

    private static final String SELECT_BY_ID_SQL = "SELECT emp.id employeeId, emp.name employeeName," +
            " emp.active, emp.departmentId departmentId, d.name departmentName " +
            "FROM employees emp LEFT JOIN departments d ON emp.departmentId = d.id WHERE emp.id = ?";

    private static final String SELECT_ALL_SQL =
            "SELECT e.id employeeId, e.name employeeName, e.active, d.id departmentId, d.name departmentName " +
                    "from employees e LEFT JOIN departments d ON e.departmentId = d.id WHERE e.name LIKE ?";

    private static final String SELECT_PAGE_SQL = "( " + SELECT_ALL_SQL + " ) ORDER BY e.id LIMIT ? OFFSET ?";

    private static final String COUNT_ALL_ROWS_SQL = "SELECT COUNT(*) FROM ( " + SELECT_ALL_SQL + " ) empl";

    private static final String INSERT_SQL = "INSERT INTO employees (name, active) VALUES (?, ?)";

    private static final String UPDATE_SQL = "UPDATE employees SET name = ?, active = ?, departmentId = ? WHERE id = ?";

    private static final String DELETE_SQL = "DELETE FROM employees WHERE employees.id = ?";

    protected JdbcTemplate jdbcTemplate;

    @Autowired
    public EmployeeJdbcRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Pair<Employee, Department> getById(Long id) {
        Pair<Employee, Department> employee = this.jdbcTemplate.queryForObject(
                SELECT_BY_ID_SQL,
                new Object[]{id},
                new DepartmentEmployeeRowMapper()
        );
        return Optional.ofNullable(employee).orElseThrow();
    }

    public Page<Pair<Employee, Department>> findAll(Pageable pageable) {
        // I used pageable sort for my "like filter" purposes, bad practice for production but fits to this task pretty good
        String property = pageable.getSort().get().findFirst()
                .map(Sort.Order::getProperty)
                .map(s -> s + "%")
                .orElse("%");

        List<Pair<Employee, Department>> employees = this.jdbcTemplate.query(
                SELECT_PAGE_SQL,
                new Object[]{property, pageable.getPageSize(), pageable.getOffset()},
                new DepartmentEmployeeRowMapper()
        );

        Long count = this.jdbcTemplate.queryForObject(COUNT_ALL_ROWS_SQL, new Object[]{property}, Long.TYPE);

        return new PageImpl<>(employees, pageable, Optional.ofNullable(count).orElse(0L));
    }

    public Employee create(Employee employee) {
        PreparedStatementCreatorFactory statementCreatorFactory = new PreparedStatementCreatorFactory(INSERT_SQL);
        statementCreatorFactory.setReturnGeneratedKeys(true);

        Object[] params = new Object[]{employee.getName(), employee.getActive()};
        PreparedStatementCreator statementCreator = statementCreatorFactory.newPreparedStatementCreator(params);

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        this.jdbcTemplate.update(statementCreator, keyHolder);

        Optional.ofNullable(keyHolder.getKey()).map(Number::longValue).ifPresent(employee::setId);
        return employee;
    }

    public void delete(Long id) {
        this.jdbcTemplate.update(DELETE_SQL, id);
    }

    public Employee update(Long id, Employee updateToEmployee) {
        String newName = updateToEmployee.getName();
        Boolean newStatus = updateToEmployee.getActive();
        Long newDepartmentId = updateToEmployee.getDepartmentId();

        this.jdbcTemplate.update(
                UPDATE_SQL,
                newName, newStatus, newDepartmentId, id
        );
        return updateToEmployee;
    }

    public static class DepartmentEmployeeRowMapper implements RowMapper<Pair<Employee, Department>> {

        @Override
        public Pair<Employee, Department> mapRow(ResultSet rs, int rowNum) throws SQLException {
            Employee employee = Employee.builder()
                    .id(rs.getLong("employeeId"))
                    .name(rs.getString("employeeName"))
                    .active(rs.getBoolean("active"))
                    .departmentId(rs.getObject("departmentId", Long.class))
                    .build();

            Department department = Department.builder()
                    .id(rs.getLong("departmentId"))
                    .name(rs.getString("departmentName"))
                    .build();

            return Pair.of(employee, department);
        }
    }
}
