package io.d4tzz.dbproject.servlet;

import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.io.IOException;

public class ThymeleafServlet extends HttpServlet {
    protected final String HTTP_SERVLET_REQUEST_ATTRIBUTE = "HTTP_SERVLET_REQUEST";
    protected final String HTTP_SERVLET_RESPONSE_ATTRIBUTE = "HTTP_SERVLET_RESPONSE";

    @Inject
    protected TemplateEngine templateEngine;

    @Inject
    protected JakartaServletWebApplication application;

    @Override
    public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext context = getServletContext();
        context.setAttribute(HTTP_SERVLET_REQUEST_ATTRIBUTE, req);
        context.setAttribute(HTTP_SERVLET_RESPONSE_ATTRIBUTE, resp);

        super.service(req, resp);

        context.removeAttribute(HTTP_SERVLET_REQUEST_ATTRIBUTE);
        context.removeAttribute(HTTP_SERVLET_RESPONSE_ATTRIBUTE);
    }

    private HttpServletRequest getHttpServletRequest() throws ServletException {
        ServletContext context = getServletContext();
        HttpServletRequest request = (HttpServletRequest) context.getAttribute(HTTP_SERVLET_REQUEST_ATTRIBUTE);

        if (request == null) {
            throw new ServletException("Servlet does not have a request attribute");
        }

        return request;
    }

    private HttpServletResponse getHttpServletResponse() throws ServletException {
        ServletContext context = getServletContext();
        HttpServletResponse response = (HttpServletResponse) context.getAttribute(HTTP_SERVLET_RESPONSE_ATTRIBUTE);

        if (response == null) {
            throw new ServletException("Servlet does not have a response attribute");
        }

        return response;
    }

    private IWebExchange buildWebExchange() throws ServletException {
        return this.application.buildExchange(getHttpServletRequest(), getHttpServletResponse());
    }

    protected WebContext buildWebContext() throws ServletException {
        return new WebContext(buildWebExchange());
    }

    protected String processTemplate(String templateName, WebContext context) {
        return templateEngine.process(templateName, context);
    }
}
