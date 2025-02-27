package com.housewhisper.housewhisperschedulerserver.model.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor // Generates a no-argument constructor
@AllArgsConstructor
@Builder
@Data
public class Client {
    private String client_id;
    private List<ClientMetadata> metadata;
    private ClientPersonal personal;
}
