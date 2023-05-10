package ru.otus.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.model.Client;
import ru.otus.service.ClientService;

@RestController
public class ClientRestController {
    private final ClientService clientService;

    public ClientRestController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping("/api/client")
    public void createClient(@RequestBody Client client) {
        client.setNew(true);
        clientService.saveClient(client);
    }
}
