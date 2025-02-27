package com.housewhisper.housewhisperschedulerserver.model.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.fortuna.ical4j.model.DateTime;

@NoArgsConstructor // Generates a no-argument constructor
@AllArgsConstructor
@Builder
@Data
public class ClientMetadata {
    private String client_id;
    private String description;
    private DateTime event_time;
}
