package io.d4tzz.dbproject.repo;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import io.d4tzz.dbproject.model.Employee;

@ApplicationScoped
public class EmployeeRepo {
    @Inject
    private DataSource dataSource;

    public Employee findById(int id) throws SQLException {
        Connection connection = dataSource.getConnection();

        String query = "SELECT * FROM employees WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, id);

        statement.execute();
        ResultSet resultSet = statement.getResultSet();

        Employee employee = null;
        if (resultSet.next()) {
            employee = getEmployeeFromResultSet(resultSet);
        }

        return employee;
    }

    public List<Employee> findAll(int size, int page) throws SQLException {
        Connection connection = dataSource.getConnection();

        String query = "SELECT * FROM employees OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, page * size);
        statement.setInt(2, size);

        ResultSet resultSet = statement.executeQuery();
        List<Employee> employees = new ArrayList<>();
        while (resultSet.next()) {
            Employee employee = getEmployeeFromResultSet(resultSet);
            employees.add(employee);
        }

        return employees;
    }

    public Employee isMaxPage(int size, int page) throws SQLException {
        int nextPage = page + 1;

        Connection connection = dataSource.getConnection();

        String query = "SELECT * FROM employees OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, nextPage * size);
        statement.setInt(2, 1);
        ResultSet resultSet = statement.executeQuery();

        Employee employee = null;
        while (resultSet.next()) {
            employee = getEmployeeFromResultSet(resultSet);
        }

        return employee;
    }

    private Employee getEmployeeFromResultSet(ResultSet resultSet) throws SQLException {
        Employee employee = new Employee();
        employee.setId(resultSet.getInt("id"));
        employee.setName(resultSet.getString("name"));
        employee.setAddress(resultSet.getString("address"));
        employee.setPhone(resultSet.getString("phone"));
        employee.setEmail(resultSet.getString("email"));
        employee.setDob(resultSet.getDate("dob"));
        employee.setGender(resultSet.getString("gender"));

        return employee;
    }

    public Employee save(Employee employee) throws SQLException {
        Connection connection = dataSource.getConnection();

        String query = "INSERT INTO EMPLOYEES (name, address, phone, email, dob, gender) VALUES (?, ?, ?, ?, ?, ?)";
        String[] returnCols = { "ID" };
        PreparedStatement statement = connection.prepareStatement(query, returnCols);
        statement.setString(1, employee.getName());
        statement.setString(2, employee.getAddress());
        statement.setString(3, employee.getPhone());
        statement.setString(4, employee.getEmail());
        statement.setDate(5, employee.getDob());
        statement.setString(6, employee.getGender());

        int status = statement.executeUpdate();

        if (status > 0) {
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                int generatedId = rs.getInt(1);
                employee.setId((generatedId));
            }
            rs.close();
        }

        statement.close();
        connection.close();

        return employee;
    }

    public Employee update(Employee employee) throws SQLException {
        Connection connection = dataSource.getConnection();

        String query = """
                        UPDATE employees 
                        SET name = ?, email = ?, address = ?, phone = ?, dob = ?, gender = ? 
                        WHERE id = ?
                        """;

        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, employee.getName());
        statement.setString(2, employee.getEmail());
        statement.setString(3, employee.getAddress());
        statement.setString(4, employee.getPhone());
        statement.setDate(5, employee.getDob());
        statement.setString(6, employee.getGender());
        statement.setInt(7, employee.getId());

        statement.executeUpdate();

        return employee;
    }

    public void deleteById(int id) throws SQLException {
        Connection connection = dataSource.getConnection();

        String query = "DELETE FROM employees WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, id);
        statement.executeUpdate();
    }
}
