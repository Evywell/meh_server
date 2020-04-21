package fr.evywell.robgame.authentication;

import com.jsoniter.annotation.JsonProperty;

public class ClientTram {

    @JsonProperty(required = true)
    public String secret;

}
