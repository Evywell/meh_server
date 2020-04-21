package fr.evywell.robauth.ticket;

import com.jsoniter.annotation.JsonProperty;

public class TicketTram {

    @JsonProperty(required = true)
    public String game_code;

    @JsonProperty(required = true)
    public String token;

}
