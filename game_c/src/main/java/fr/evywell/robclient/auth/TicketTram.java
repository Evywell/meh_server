package fr.evywell.robclient.auth;

import com.jsoniter.annotation.JsonProperty;

public class TicketTram {

    @JsonProperty(required = true)
    public String ticket;

}
