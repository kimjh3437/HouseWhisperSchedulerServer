package com.housewhisper.housewhisperschedulerserver.model.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor // Generates a no-argument constructor
@AllArgsConstructor
@Builder
@Data
public class ClientPersonal {
    private String client_id;
    private String firstname;
    private String lastname;
    private String email;
}
