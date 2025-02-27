package com.housewhisper.housewhisperschedulerserver.service;

import com.housewhisper.housewhisperschedulerserver.model.agent.Agent;
import com.housewhisper.housewhisperschedulerserver.repository.DataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AgentService {
    @Autowired
    private DataRepository dataRepository;

    public void saveAgent(Agent agent) {
        dataRepository.getAgents().put(agent.getAgent_id(), agent);
    }

    public Agent getAgent(String agentId) {
        return dataRepository.getAgents().get(agentId);
    }

    public List<Agent> getAllAgents() {
        return new ArrayList<>(dataRepository.getAgents().values());
    }

    public void removeAllAgents() {
        dataRepository.getAgents().clear();
    }

    public void addTaskToAgent(String agentId, String taskId) {
        Agent agent = getAgent(agentId);
        if (agent != null) {
            if (!agent.getTasks().contains(taskId)) {
                agent.getTasks().add(taskId);
            }
            saveAgent(agent);
        }
    }
}