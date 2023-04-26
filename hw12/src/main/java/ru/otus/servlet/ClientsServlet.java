package ru.otus.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.services.TemplateProcessor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ClientsServlet extends HttpServlet {
    private static final String CLIENTS_PAGE_TEMPLATE = "clients.html";

    private static final String TEMPLATE_ATTR_CLIENTS = "clients";

    private final DBServiceClient dbServiceClient;

    private final TemplateProcessor templateProcessor;

    public ClientsServlet(DBServiceClient dbServiceClient, TemplateProcessor templateProcessor) {
        this.dbServiceClient = dbServiceClient;
        this.templateProcessor = templateProcessor;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> paramsMap = new HashMap<>();

        var clients = dbServiceClient.findAll();
        if (clients != null) {
            paramsMap.put(TEMPLATE_ATTR_CLIENTS, clients);
        }

        resp.setContentType("text/html");
        resp.getWriter().println(templateProcessor.getPage(CLIENTS_PAGE_TEMPLATE, paramsMap));
    }
}
