package io.d4tzz.dbproject.servlet;

import io.d4tzz.dbproject.model.Employee;
import io.d4tzz.dbproject.repo.EmployeeRepo;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.context.WebContext;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;

@WebServlet("/add-employee")
public class addEmployeeServlet extends ThymeleafServlet {
    @Inject
    private EmployeeRepo employeeRepo;

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        WebContext ctx = buildWebContext();
        Employee employee = new Employee();
        ctx.setVariable("employee", employee);

        ctx.setVariable("title", "Add New Employee");
        ctx.setVariable("sub_title", "Please fill in the form below to register a new employee");

        resp.setContentType("text/html");
        resp.getWriter().print(processTemplate("employee_form", ctx));
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Employee employee = new Employee();
        employee.setName(req.getParameter("name"));
        employee.setAddress(req.getParameter("address"));
        employee.setEmail(req.getParameter("email"));
        employee.setAddress(req.getParameter("address"));
        employee.setDob(Date.valueOf(req.getParameter("dob")));
        employee.setPhone(req.getParameter("phone"));
        employee.setGender(req.getParameter("gender"));

        try {
            employee = employeeRepo.save(employee);
        } catch (SQLException e) {
            throw new ServletException(e);
        }

        WebContext ctx = buildWebContext();
        ctx.setVariable("employee", employee);
        resp.setContentType("text/html");
        resp.getWriter().print(processTemplate("employee_created_success", ctx));
    }
}
