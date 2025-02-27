package com.housewhisper.housewhisperschedulerserver.service;

import com.housewhisper.housewhisperschedulerserver.model.agent.Agent;
import com.housewhisper.housewhisperschedulerserver.model.agent.AgentPersonal;
import com.housewhisper.housewhisperschedulerserver.model.client.Client;
import com.housewhisper.housewhisperschedulerserver.model.client.ClientMetadata;
import com.housewhisper.housewhisperschedulerserver.model.client.ClientPersonal;
import com.housewhisper.housewhisperschedulerserver.model.task.WorkTask;
import com.housewhisper.housewhisperschedulerserver.model.task.WorkTaskDetail;
import net.fortuna.ical4j.model.DateTime;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;


@Service
public class InitService {
    @Autowired
    private ChatModel chatModel;

    @Autowired
    private AgentService agentService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private TaskService taskService;

    public void init() {
        generateRandomAgents(5);
        generateRandomClients(5);
        generateRandomTasks();
    }

    private void generateRandomAgents(int numberOfAgents) {
        agentService.removeAllAgents();
        for (int i = 0; i < numberOfAgents; i++) {
            String agentId = "agent" + i;
            AgentPersonal agentPersonal = AgentPersonal.builder()
                    .agent_id(agentId)
                    .firstname("Agent")
                    .lastname(String.valueOf(i + 1))
                    .email("agent" + (i + 1) + "@example.com")
                    .build();
            Agent agent = Agent.builder()
                    .personal(agentPersonal)
                    .agent_id(agentId)
                    .build();
            agentService.saveAgent(agent);
        }
    }

    private void generateRandomClients(int numberOfClients) {
        clientService.removeClients();
        for (int i = 0; i < numberOfClients; i++) {
            String clientId = "client" + i;
            ClientPersonal clientPersonal = ClientPersonal.builder()
                    .client_id(clientId)
                    .firstname("Client")
                    .lastname(String.valueOf(i + 1))
                    .email("client" + (i + 1) + "@example.com")
                    .build();
            Random random = new Random();
            List<ClientMetadata> clientMetadata = new ArrayList<>();
            for (int j = 0; j < 3; j++) { // Generate 3-4 random metadata entries
                String description = generateRandomDescription();
                ClientMetadata metadata = ClientMetadata.builder()
                        .client_id(clientId)
                        .description(description)
                        .event_time(new DateTime(System.currentTimeMillis() + random.nextInt(1000000000)))
                        .build();
                clientMetadata.add(metadata);
            }
            Client client = Client.builder()
                    .client_id(clientId)
                    .personal(clientPersonal)
                    .metadata(clientMetadata)
                    .build();
            clientService.saveClient(client);
        }
    }

    private void generateRandomTasks() {
        taskService.removeTasks();
        List<String> clientIds = clientService.getAllClients().stream()
                .map(Client::getClient_id)
                .toList();
        List<String> agentIds = agentService.getAllAgents().stream()
                .map(Agent::getAgent_id)
                .toList();
        Random random = new Random();

        for (int i = 0; i < 20; i++) {
            String randomClientId = clientIds.get(random.nextInt(clientIds.size()));
            int numberOfAgents = random.nextInt(agentIds.size()) + 1;
            List<String> randomAgentIds = new ArrayList<>();

            // Generate random start time within the next 7 days
            long currentTimeMillis = System.currentTimeMillis();
            long randomStartTimeMillis = currentTimeMillis + TimeUnit.DAYS.toMillis(random.nextInt(7));
            LocalDateTime taskStartTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(randomStartTimeMillis), ZoneId.systemDefault());

            // Generate random duration (30min, 60min, 90min, 120min)
            int[] durations = {30, 60, 90, 120};
            int randomDuration = durations[random.nextInt(durations.length)];
            LocalDateTime taskEndTime = taskStartTime.plusMinutes(randomDuration);

            for (String agentId : agentIds) {
                Agent agent = agentService.getAgent(agentId);
                List<String> taskIds = agent.getTasks();

                TreeMap<LocalDateTime, LocalDateTime> taskIntervals = new TreeMap<>();
                for (String taskId : taskIds) {
                    WorkTask task = taskService.getTask(taskId);
                    LocalDateTime existingTaskStartTime = task.getDetails().getTaskStartTime();
                    LocalDateTime existingTaskEndTime = task.getDetails().getTaskEndTime();
                    taskIntervals.put(existingTaskStartTime, existingTaskEndTime);
                }

                if (!hasOverlap(taskIntervals, taskStartTime, taskEndTime)) {
                    randomAgentIds.add(agentId);
                    if (randomAgentIds.size() == numberOfAgents) {
                        break;
                    }
                }
            }

            WorkTaskDetail taskDetail = WorkTaskDetail.builder()
                    .taskType("type1")
                    .description("Description for task " + i)
                    .taskStartTime(taskStartTime)
                    .taskEndTime(taskEndTime)
                    .clients(List.of(randomClientId))
                    .agents(randomAgentIds)
                    .build();

            WorkTask task = WorkTask.builder()
                    .task_id("task" + i)
                    .details(taskDetail)
                    .build();

            taskService.saveTask(task);

            // Add task ID to each agent's tasks
            for (String agentId : randomAgentIds) {
                agentService.addTaskToAgent(agentId, task.getTask_id());
            }
        }
    }

    private boolean hasOverlap(TreeMap<LocalDateTime, LocalDateTime> taskIntervals, LocalDateTime startTime, LocalDateTime endTime) {
        LocalDateTime floorKey = taskIntervals.floorKey(startTime);
        if (floorKey != null && taskIntervals.get(floorKey).isAfter(startTime)) {
            return true;
        }

        LocalDateTime ceilingKey = taskIntervals.ceilingKey(startTime);
        if (ceilingKey != null && ceilingKey.isBefore(endTime)) {
            return true;
        }

        return false;
    }

    public String generateRandomDescription() {
        String prompt = "Generate one sentence random description for a client's preferences, habits, and whether they are a morning or evening person.";
        ChatResponse response = chatModel.call(new Prompt(prompt));
        if (response != null && response.getResult() != null && response.getResult().getOutput() != null) {
            return response.getResult().getOutput().getText();
        }
        return "";
    }
}