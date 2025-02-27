package com.housewhisper.housewhisperschedulerserver.controller;

import com.housewhisper.housewhisperschedulerserver.model.agent.Agent;
import com.housewhisper.housewhisperschedulerserver.model.task.WorkTask;
import com.housewhisper.housewhisperschedulerserver.service.AgentService;
import com.housewhisper.housewhisperschedulerserver.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/agents")
public class AgentController {
    @Autowired
    private AgentService agentService;

    @Autowired
    private TaskService taskService;

    @GetMapping("/getall")
    public List<Agent> getAllAgents() {
        return agentService.getAllAgents();
    }

    @GetMapping("/alltasks")
    public List<WorkTask> getAllTasks() {
        return taskService.getAllTasks();
    }
}
