package com.housewhisper.housewhisperschedulerserver.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor // Generates a no-argument constructor
@AllArgsConstructor
@Builder
@Data
public class AvailableTimeSlotsDTO {
    private LocalDateTime start_time;
    private LocalDateTime end_time;
}
