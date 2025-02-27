package com.housewhisper.housewhisperschedulerserver.controller;

import com.housewhisper.housewhisperschedulerserver.model.task.WorkTask;
import com.housewhisper.housewhisperschedulerserver.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping
    public ResponseEntity<WorkTask> createTask(@RequestBody WorkTask task) {
        taskService.saveTask(task);
        return ResponseEntity.ok(task);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<WorkTask> getTask(@PathVariable String taskId) {
        WorkTask task = taskService.getTask(taskId);
        if (task != null) {
            return ResponseEntity.ok(task);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/getall")
    public ResponseEntity<List<WorkTask>> getAllTasks() {
        List<WorkTask> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    @PutMapping("/{taskId}/assignClient/{clientId}")
    public ResponseEntity<Void> assignClientToTask(@PathVariable String taskId, @PathVariable String clientId) {
        taskService.assignClientToTask(taskId, clientId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{taskId}/assignAgent/{agentId}")
    public ResponseEntity<Void> assignAgentToTask(@PathVariable String taskId, @PathVariable String agentId) {
        taskService.assignAgentToTask(taskId, agentId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable String taskId) {
        taskService.removeTasks();
        return ResponseEntity.noContent().build();
    }
}
