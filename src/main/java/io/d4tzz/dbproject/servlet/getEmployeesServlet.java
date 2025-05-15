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
import java.util.List;

@WebServlet("/employees")
public class getEmployeesServlet extends ThymeleafServlet {
    @Inject
    private EmployeeRepo employeeRepo;

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sizeParam = req.getParameter("size");
        String pageParam = req.getParameter("page");

        if (pageParam == null || pageParam.isEmpty()) {
            pageParam = "0";
        }

        if (sizeParam == null || sizeParam.isEmpty()) {
            sizeParam = "5";
        }

        int size = Integer.parseInt(sizeParam);
        int page = Integer.parseInt(pageParam);

        List<Employee> employees;
        boolean isMaxPage;
        try {
            employees = employeeRepo.findAll(size, page);
            isMaxPage = employeeRepo.isMaxPage(size, page) == null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        WebContext ctx = buildWebContext();
        ctx.setVariable("employees", employees);
        ctx.setVariable("isMaxPage", isMaxPage);
        ctx.setVariable("page", page + 1);

        resp.setContentType("text/html");
        resp.getWriter().print(processTemplate("employee", ctx));
    }
}
