package fr.evywell.robclient.auth;

import com.jsoniter.annotation.JsonProperty;

public class LoginTramSuccess {

    @JsonProperty(required = true)
    public String token;

}
