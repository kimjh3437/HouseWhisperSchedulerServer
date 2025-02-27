package com.housewhisper.housewhisperschedulerserver.controller;

import com.housewhisper.housewhisperschedulerserver.model.dto.AIRecommendedTimeSlotDTO;
import com.housewhisper.housewhisperschedulerserver.model.dto.AvailableTimeSlotsDTO;
import com.housewhisper.housewhisperschedulerserver.model.dto.CheckAvailabilityDTO;
import com.housewhisper.housewhisperschedulerserver.model.dto.CompleteAvailabilityDTO;
import com.housewhisper.housewhisperschedulerserver.service.SchedulingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/schedule")
public class SchedulingController {

    @Autowired
    private SchedulingService schedulingService;

    /**
     * Endpoint to check the availability of agents for a given time slot.
     * @param dto CheckAvailabilityDTO containing agent IDs and the time slot to check.
     * @return ResponseEntity with a boolean indicating availability.
     */
    @PostMapping("/check-availability")
    public ResponseEntity<Boolean> checkAvailability(@RequestBody CheckAvailabilityDTO dto) {
        boolean isAvailable = schedulingService.checkAvailability(dto);
        return new ResponseEntity<>(isAvailable, HttpStatus.OK);
    }

    /**
     * Endpoint to find available time slots for agents within a specified time frame.
     * @param dto CompleteAvailabilityDTO containing agent IDs, duration, and time frame.
     * @return ResponseEntity with a list of available time slots.
     */
    @PostMapping("/find-available-time-slots")
    public ResponseEntity<List<AvailableTimeSlotsDTO>> findAvailableTimeSlots(@RequestBody CompleteAvailabilityDTO dto) {
        List<AvailableTimeSlotsDTO> availableTimeSlots = schedulingService.findAvailableTimeSlots(dto);
        if (availableTimeSlots.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(availableTimeSlots, HttpStatus.OK);
        }
    }

    /**
     * Endpoint to find available agents for the given time slots.
     * @param availableTimeSlots List of AvailableTimeSlotsDTO representing the time slots.
     * @return ResponseEntity with a list of available agent IDs.
     */
    @PostMapping("/find-available-agents")
    public ResponseEntity<List<String>> findAvailableAgents(@RequestBody List<AvailableTimeSlotsDTO> availableTimeSlots) {
        List<String> availableAgents = schedulingService.findAvailableAgents(availableTimeSlots);
        if (availableAgents.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(availableAgents, HttpStatus.OK);
        }
    }

    /**
     * Endpoint to get AI-recommended time slots for a client based on available time slots.
     * @param aiRecommendedTimeSlotDTO AIRecommendedTimeSlotDTO containing client ID and available time slots.
     * @return ResponseEntity with AIRecommendedTimeSlotDTO containing recommended time slots and reasoning.
     */
    @PostMapping("/ai-recommended-time-slots")
    public ResponseEntity<AIRecommendedTimeSlotDTO> getAIRecommendedTimeSlots(
            @RequestBody AIRecommendedTimeSlotDTO aiRecommendedTimeSlotDTO) {
        String clientId = aiRecommendedTimeSlotDTO.getClientId();
        List<AvailableTimeSlotsDTO> availableTimeSlots = aiRecommendedTimeSlotDTO.getAvailableTimeSlots();
        AIRecommendedTimeSlotDTO recommendedTimeSlots = schedulingService.getAIRecommendedTimeSlots(clientId, availableTimeSlots);
        return new ResponseEntity<>(recommendedTimeSlots, HttpStatus.OK);
    }
}