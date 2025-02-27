package com.housewhisper.housewhisperschedulerserver.service;

import com.housewhisper.housewhisperschedulerserver.model.agent.Agent;
import com.housewhisper.housewhisperschedulerserver.model.client.Client;
import com.housewhisper.housewhisperschedulerserver.model.client.ClientMetadata;
import com.housewhisper.housewhisperschedulerserver.model.dto.AIRecommendedTimeSlotDTO;
import com.housewhisper.housewhisperschedulerserver.model.dto.AvailableTimeSlotsDTO;
import com.housewhisper.housewhisperschedulerserver.model.dto.CheckAvailabilityDTO;
import com.housewhisper.housewhisperschedulerserver.model.dto.CompleteAvailabilityDTO;
import com.housewhisper.housewhisperschedulerserver.model.task.WorkTask;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SchedulingService {

    @Autowired
    private ChatModel chatModel;
    @Autowired
    private ClientService clientService;

    @Autowired
    private AgentService agentService;

    @Autowired
    private TaskService taskService;

    /**
     * Checks the availability of agents for a given time slot.
     * @param dto CheckAvailabilityDTO containing agent IDs and the time slot to check.
     * @return boolean indicating whether all agents are available.
     */
    public boolean checkAvailability(CheckAvailabilityDTO dto) {
        List<String> agentIds = dto.getAgents();
        LocalDateTime startTime = dto.getStartTime();
        LocalDateTime endTime = dto.getEndTime();

        for (String agentId : agentIds) {
            Agent agent = agentService.getAgent(agentId);
            List<String> taskIds = agent.getTasks();

            TreeMap<LocalDateTime, LocalDateTime> taskIntervals = new TreeMap<>();

            for (String taskId : taskIds) {
                WorkTask task = taskService.getTask(taskId);
                LocalDateTime taskStartTime = task.getDetails().getTaskStartTime();
                LocalDateTime taskEndTime = task.getDetails().getTaskEndTime();
                taskIntervals.put(taskStartTime, taskEndTime);
            }

            if (hasOverlap(taskIntervals, startTime, endTime)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Finds available time slots for agents within a specified time frame.
     * @param dto CompleteAvailabilityDTO containing agent IDs, duration, and time frame.
     * @return List of AvailableTimeSlotsDTO representing the available time slots.
     */
    public List<AvailableTimeSlotsDTO> findAvailableTimeSlots(CompleteAvailabilityDTO dto) {
        List<String> agentIds = dto.getAgents();
        int durationInMinutes = dto.getDurationInMinutes();
        LocalDateTime startTimeFrame = dto.getStartTimeFrame();
        LocalDateTime endTimeFrame = dto.getEndTimeFrame();

        List<AvailableTimeSlotsDTO> availableTimeSlots = new ArrayList<>();

        List<TreeMap<LocalDateTime, LocalDateTime>> allAgentsTaskIntervals = new ArrayList<>();

        for (String agentId : agentIds) {
            Agent agent = agentService.getAgent(agentId);
            List<String> taskIds = agent.getTasks();

            TreeMap<LocalDateTime, LocalDateTime> taskIntervals = new TreeMap<>();
            for (String taskId : taskIds) {
                WorkTask task = taskService.getTask(taskId);
                LocalDateTime taskStartTime = task.getDetails().getTaskStartTime();
                LocalDateTime taskEndTime = task.getDetails().getTaskEndTime();
                taskIntervals.put(taskStartTime, taskEndTime);
            }
            allAgentsTaskIntervals.add(taskIntervals);
        }

        LocalDateTime currentStartTime = startTimeFrame;
        while (currentStartTime.isBefore(endTimeFrame)) {
            LocalDateTime currentEndTime = currentStartTime.plusMinutes(durationInMinutes);

            if (currentEndTime.isAfter(endTimeFrame)) {
                break;
            }

            boolean allAvailable = true;
            for (TreeMap<LocalDateTime, LocalDateTime> taskIntervals : allAgentsTaskIntervals) {
                if (hasOverlap(taskIntervals, currentStartTime, currentEndTime)) {
                    allAvailable = false;
                    break;
                }
            }

            if (allAvailable) {
                availableTimeSlots.add(new AvailableTimeSlotsDTO(currentStartTime, currentEndTime));
            }

            currentStartTime = currentStartTime.plusMinutes(durationInMinutes);
        }

        return availableTimeSlots;
    }

    /**
     * Finds available agents for the given time slots.
     * @param availableTimeSlots List of AvailableTimeSlotsDTO representing the time slots.
     * @return List of available agent IDs.
     */
    public List<String> findAvailableAgents(List<AvailableTimeSlotsDTO> availableTimeSlots) {
        List<String> availableAgents = new ArrayList<>();
        List<Agent> allAgents = agentService.getAllAgents();

        for (Agent agent : allAgents) {
            List<String> taskIds = agent.getTasks();
            TreeMap<LocalDateTime, LocalDateTime> taskIntervals = new TreeMap<>();

            for (String taskId : taskIds) {
                WorkTask task = taskService.getTask(taskId);
                LocalDateTime taskStartTime = task.getDetails().getTaskStartTime();
                LocalDateTime taskEndTime = task.getDetails().getTaskEndTime();
                taskIntervals.put(taskStartTime, taskEndTime);
            }

            boolean isAvailable = true;
            for (AvailableTimeSlotsDTO timeSlot : availableTimeSlots) {
                LocalDateTime slotStartTime = timeSlot.getStart_time();
                LocalDateTime slotEndTime = timeSlot.getEnd_time();

                if (hasOverlap(taskIntervals, slotStartTime, slotEndTime)) {
                    isAvailable = false;
                    break;
                }
            }

            if (isAvailable) {
                availableAgents.add(agent.getAgent_id());
            }
        }

        return availableAgents;
    }

    /**
     * Gets AI-recommended time slots for a client based on available time slots.
     * @param clientId ID of the client.
     * @param availableTimeSlots List of available time slots.
     * @return AIRecommendedTimeSlotDTO containing recommended time slots and reasoning.
     */
    public AIRecommendedTimeSlotDTO getAIRecommendedTimeSlots(String clientId, List<AvailableTimeSlotsDTO> availableTimeSlots) {
        Client client = clientService.getClient(clientId);
        List<ClientMetadata> metadataList = client.getMetadata();

        String metadataDescriptions = metadataList.stream()
                .map(ClientMetadata::getDescription)
                .collect(Collectors.joining(", "));

        String timeSlotDescriptions = availableTimeSlots.stream()
                .map(slot -> slot.getStart_time() + " to " + slot.getEnd_time())
                .collect(Collectors.joining(", "));

        String prompt = "Based on the following client metadata descriptions for" + client.getPersonal().getFirstname() + ": " + metadataDescriptions +
                " and the available time slots: " + timeSlotDescriptions +
                ", recommend the best time slots. Return the indices of the recommended time slots as a comma-separated list with no spaces. " +
                "Add a reasoning at the end separated by '|'. The response must strictly follow the format: '1,2,3|Reasoning text'.";

        ChatResponse response = chatModel.call(new Prompt(prompt));

        String result = response.getResult().getOutput().getText();
        String[] parts = result.split("\\|");
        String indicesPart = parts[0];
        String reasoning = parts.length > 1 ? parts[1].trim() : "";

        List<Integer> indices = Arrays.stream(indicesPart.split(","))
                .filter(this::isInteger)
                .map(Integer::parseInt)
                .filter(index -> index >= 0 && index < availableTimeSlots.size()) // Ensure index is within bounds
                .collect(Collectors.toList());

        List<AvailableTimeSlotsDTO> recommendedTimeSlots = indices.stream()
                .map(availableTimeSlots::get)
                .collect(Collectors.toList());

        return AIRecommendedTimeSlotDTO.builder()
                .clientId(clientId)
                .availableTimeSlots(recommendedTimeSlots)
                .reason(reasoning)
                .build();
    }

    /**
     * Parses the response from the AI model to extract the recommended time slots.
     * @param response ChatResponse from the AI model.
     * @param availableTimeSlots List of available time slots.
     * @return List of recommended AvailableTimeSlotsDTO.
     */
    private List<AvailableTimeSlotsDTO> parseResponse(ChatResponse response, List<AvailableTimeSlotsDTO> availableTimeSlots) {
        String result = response.getResult().getOutput().getText();
        String[] parts = result.split("\\|");
        String indicesPart = parts[0];
        List<Integer> indices = Arrays.stream(indicesPart.split(","))
                .filter(this::isInteger)
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        return indices.stream()
                .map(availableTimeSlots::get)
                .collect(Collectors.toList());
    }

    private boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        try {
            Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * Parses the reasoning from the AI model response.
     * @param response ChatResponse from the AI model.
     * @return String containing the reasoning.
     */
    private String parseReasoning(ChatResponse response) {
        String result = response.getResult().getOutput().getText();
        String[] parts = result.split("\\|");
        return parts.length > 1 ? parts[1].trim() : "";
    }

    /**
     * Checks if there is an overlap between the given time interval and existing task intervals.
     * @param taskIntervals TreeMap of task intervals.
     * @param startTime Start time of the interval to check.
     * @param endTime End time of the interval to check.
     * @return boolean indicating whether there is an overlap.
     */
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
}