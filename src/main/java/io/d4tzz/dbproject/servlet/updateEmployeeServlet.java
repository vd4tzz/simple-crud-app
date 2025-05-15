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

@WebServlet("/update-employee")
public class updateEmployeeServlet extends ThymeleafServlet {
    @Inject
    private EmployeeRepo employeeRepo;

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing id parameter");
            return;
        }

        int id = Integer.parseInt(idParam);

        Employee employee;
        try {
            employee = employeeRepo.findById(id);
        } catch (SQLException e) {
            throw new ServletException(e);
        }

        if (employee == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Employee not found");
            return;
        }

        WebContext ctx = buildWebContext();
        ctx.setVariable("employee", employee);
        ctx.setVariable("title", "Update Employee");
        ctx.setVariable("sub_title", "Please fill in the form below to update a employee");
        resp.setContentType("text/html");
        resp.getWriter().print(processTemplate("employee_form", ctx));
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing id parameter");
            return;
        }

        int id = Integer.parseInt(idParam);

        Employee oldEmployee = null;
        try {
            oldEmployee = employeeRepo.findById(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (oldEmployee == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Employee not found");
            return;
        }

        Employee employee = new Employee();
        employee.setName(req.getParameter("name"));
        employee.setAddress(req.getParameter("address"));
        employee.setEmail(req.getParameter("email"));
        employee.setAddress(req.getParameter("address"));
        employee.setDob(Date.valueOf(req.getParameter("dob")));
        employee.setPhone(req.getParameter("phone"));
        employee.setGender(req.getParameter("gender"));
        employee.setId(id);

        try {
            employee = employeeRepo.update(employee);
        } catch (SQLException e) {
            throw new ServletException(e);
        }

        WebContext ctx = buildWebContext();
        ctx.setVariable("employee", employee);
        ctx.setVariable("oldEmployee", oldEmployee);
        resp.setContentType("text/html");
        resp.getWriter().print(processTemplate("employee_updated_success", ctx));
    }
}
