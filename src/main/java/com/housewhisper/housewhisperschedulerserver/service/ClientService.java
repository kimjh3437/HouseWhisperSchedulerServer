package com.housewhisper.housewhisperschedulerserver.service;

import com.housewhisper.housewhisperschedulerserver.model.client.Client;
import com.housewhisper.housewhisperschedulerserver.model.client.ClientPersonal;
import com.housewhisper.housewhisperschedulerserver.repository.DataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ClientService {
    @Autowired
    private DataRepository dataRepository;

    public void saveClient(Client client) {
        dataRepository.getClients().put(client.getClient_id(), client);
    }

    public Client getClient(String clientId) {
        return dataRepository.getClients().get(clientId);
    }

    public void removeClients() {
        dataRepository.getClients().clear();
    }

    public List<Client> getAllClients() {
        return dataRepository.getClients().values().stream().collect(Collectors.toList());
    }
}