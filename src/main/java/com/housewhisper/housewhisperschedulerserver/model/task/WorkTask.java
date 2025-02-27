package com.housewhisper.housewhisperschedulerserver.model.task;

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
public class WorkTask {
    private String task_id;
    private WorkTaskDetail details;
    @Builder.Default
    private List<WorkTaskAttachment> attachments = new ArrayList<>();
}
