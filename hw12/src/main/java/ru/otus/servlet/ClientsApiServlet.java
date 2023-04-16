package ru.otus.servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.crm.model.Client;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.dao.UserDao;
import ru.otus.model.User;

import java.io.IOException;


public class ClientsApiServlet extends HttpServlet {

    private final DBServiceClient dbServiceClient;
    private final Gson gson;

    public ClientsApiServlet(DBServiceClient dbServiceClient, Gson gson) {
        this.dbServiceClient = dbServiceClient;
        this.gson = gson;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var newClient = gson.fromJson(request.getReader(), Client.class);

        dbServiceClient.saveClient(newClient);
    }
}
