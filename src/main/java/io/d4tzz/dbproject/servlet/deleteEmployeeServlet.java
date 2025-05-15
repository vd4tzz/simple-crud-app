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
import java.sql.SQLException;

@WebServlet("/delete-employee")
public class deleteEmployeeServlet extends ThymeleafServlet {
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

        Employee employee = null;
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
        resp.setContentType("text/html");
        resp.getWriter().print(processTemplate("delete_employee", ctx));
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");

        int id = Integer.parseInt(idParam);

        try {
            employeeRepo.deleteById(id);
        } catch (SQLException e) {
            throw new ServletException(e);
        }

        resp.sendRedirect(req.getContextPath() + "/employees");
    }
}
