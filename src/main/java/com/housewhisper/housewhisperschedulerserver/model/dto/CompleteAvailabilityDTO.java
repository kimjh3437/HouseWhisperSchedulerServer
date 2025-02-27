package com.housewhisper.housewhisperschedulerserver.model.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor // Generates a no-argument constructor
@AllArgsConstructor
@Builder
@Data
public class CompleteAvailabilityDTO {
    private List<String> agents;
    private int durationInMinutes;
    private LocalDateTime startTimeFrame;
    private LocalDateTime endTimeFrame;
}
