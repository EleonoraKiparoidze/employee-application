package com.rustedbrain.study.employeeservice.repository;

import com.rustedbrain.study.employeeservice.model.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
public class DepartmentJdbcRepository {

    private static final String SELECT_ALL_SQL = "SELECT d.id, d.name FROM departments d";
    private static final String SELECT_SQL = "SELECT d.id, d.name FROM departments d WHERE id = ?";
    private static final String INSERT_SQL = "INSERT INTO departments (name) VALUES (?)";
    private static final String UPDATE_SQL = "UPDATE departments SET name = ? WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM departments WHERE departments.id = ?";

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DepartmentJdbcRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Optional<Department> findById(Long id) {
        Department department = jdbcTemplate.queryForObject(SELECT_SQL, new Object[]{id}, new DepartmentRowMapper());
        return Optional.ofNullable(department);
    }

    public Page<Department> findAll(Pageable pageable) {
        List<Department> departments = jdbcTemplate.query(SELECT_ALL_SQL, new DepartmentRowMapper());
        return new PageImpl<>(departments);
    }

    public Department create(Department departmentToCreate) {
        PreparedStatementCreatorFactory statementCreatorFactory = new PreparedStatementCreatorFactory(INSERT_SQL);
        statementCreatorFactory.setReturnGeneratedKeys(true);

        Object[] params = new Object[]{departmentToCreate.getName()};
        PreparedStatementCreator statementCreator = statementCreatorFactory.newPreparedStatementCreator(params);

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        this.jdbcTemplate.update(statementCreator, keyHolder);

        Optional.ofNullable(keyHolder.getKey()).map(Number::longValue).ifPresent(departmentToCreate::setId);
        return departmentToCreate;
    }

    public Department update(Long id, Department departmentToUpdate) {
        String newName = departmentToUpdate.getName();
        this.jdbcTemplate.update(UPDATE_SQL, newName, id);
        return departmentToUpdate;
    }

    public void delete(Long id) {
        this.jdbcTemplate.update(DELETE_SQL, id);
    }

    private static class DepartmentRowMapper implements RowMapper<Department> {

        @Override
        public Department mapRow(ResultSet resultSet, int i) throws SQLException {
            return Department.builder()
                    .id(resultSet.getLong("id"))
                    .name(resultSet.getString("name"))
                    .build();
        }
    }
}
