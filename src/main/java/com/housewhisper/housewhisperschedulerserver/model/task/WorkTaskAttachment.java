package com.housewhisper.housewhisperschedulerserver.model.task;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor // Generates a no-argument constructor
@AllArgsConstructor
@Builder
@Data
public class WorkTaskAttachment {
    private String task_id;
    @Builder.Default
    private String description = "sample description / for scalability / just add entity below.";
}
