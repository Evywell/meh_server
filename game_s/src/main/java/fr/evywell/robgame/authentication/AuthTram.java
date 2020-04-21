package fr.evywell.robgame.authentication;

import com.jsoniter.annotation.JsonProperty;

public class AuthTram {

    @JsonProperty(required = true)
    public String ticket;

    @JsonProperty(required = true)
    public String token;

}
