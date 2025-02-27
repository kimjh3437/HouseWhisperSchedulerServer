package com.housewhisper.housewhisperschedulerserver.model.agent;

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
public class Agent {
    private String agent_id;
    private AgentPersonal personal;
    @Builder.Default
    private List<String> tasks = new ArrayList<>();
}
