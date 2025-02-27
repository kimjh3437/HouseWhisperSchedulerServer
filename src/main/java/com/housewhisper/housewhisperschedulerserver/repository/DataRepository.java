package com.housewhisper.housewhisperschedulerserver.repository;

import com.housewhisper.housewhisperschedulerserver.model.agent.Agent;
import com.housewhisper.housewhisperschedulerserver.model.client.Client;
import com.housewhisper.housewhisperschedulerserver.model.task.WorkTask;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class DataRepository {
    private final Map<String, Agent> agents = new HashMap<>();
    private final Map<String, Client> clients = new HashMap<>();
    private final Map<String, WorkTask> tasks = new HashMap<>();

    public Map<String, Agent> getAgents() {
        return agents;
    }

    public Map<String, Client> getClients() {
        return clients;
    }

    public Map<String, WorkTask> getTasks() {
        return tasks;
    }
}