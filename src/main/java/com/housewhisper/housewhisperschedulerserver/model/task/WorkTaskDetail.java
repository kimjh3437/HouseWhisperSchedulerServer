package com.housewhisper.housewhisperschedulerserver.model.task;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor // Generates a no-argument constructor
@AllArgsConstructor
@Builder
@Data
public class WorkTaskDetail {
    private String task_id;
    private String taskType;
    private LocalDateTime taskStartTime;
    private LocalDateTime taskEndTime;
    private String description;
    @Builder.Default
    private List<String> clients = new ArrayList<>();
    @Builder.Default
    private List<String> agents = new ArrayList<>();
}
