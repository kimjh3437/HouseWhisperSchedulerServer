package com.housewhisper.housewhisperschedulerserver.service;

import com.housewhisper.housewhisperschedulerserver.model.task.WorkTask;
import com.housewhisper.housewhisperschedulerserver.model.task.WorkTaskDetail;
import com.housewhisper.housewhisperschedulerserver.repository.DataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {
    @Autowired
    private DataRepository dataRepository;

    public void saveTask(WorkTask task) {
        dataRepository.getTasks().put(task.getTask_id(), task);
    }

    public void removeTasks() {
        dataRepository.getTasks().clear();
    }

    public WorkTask getTask(String taskId) {
        return dataRepository.getTasks().get(taskId);
    }

    public List<WorkTask> getAllTasks() {
        return new ArrayList<>(dataRepository.getTasks().values());
    }

    public void assignClientToTask(String taskId, String clientId) {
        WorkTask task = dataRepository.getTasks().get(taskId);
        if (task != null && !task.getDetails().getClients().contains(clientId)) {
            WorkTaskDetail detail = task.getDetails();
            detail.getClients().add(clientId);
        }
    }

    public void assignAgentToTask(String taskId, String agentId) {
        WorkTask task = dataRepository.getTasks().get(taskId);
        if (task != null && !task.getDetails().getAgents().contains(agentId)) {
            WorkTaskDetail detail = task.getDetails();
            detail.getAgents().add(agentId);
        }
    }
}