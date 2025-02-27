package com.housewhisper.housewhisperschedulerserver.model.agent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor // Generates a no-argument constructor
@AllArgsConstructor
@Builder
@Data
public class AgentPersonal {
    private String agent_id;
    private String firstname;
    private String lastname;
    private String email;
}
