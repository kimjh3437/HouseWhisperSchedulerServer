package com.housewhisper.housewhisperschedulerserver.controller;

import com.housewhisper.housewhisperschedulerserver.model.client.Client;
import com.housewhisper.housewhisperschedulerserver.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/clients")
public class ClientController {
    @Autowired
    private ClientService clientService;

    @GetMapping("/getall")
    public List<Client> getAllClients() {
        return clientService.getAllClients();
    }
}
