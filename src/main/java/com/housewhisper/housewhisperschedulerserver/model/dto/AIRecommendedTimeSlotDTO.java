package com.housewhisper.housewhisperschedulerserver.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor // Generates a no-argument constructor
@AllArgsConstructor
@Builder
@Data
public class AIRecommendedTimeSlotDTO {
    private String clientId;
    private String reason; // Reason for the recommendation
    @Builder.Default
    private List<AvailableTimeSlotsDTO> availableTimeSlots = new ArrayList<>();
}
