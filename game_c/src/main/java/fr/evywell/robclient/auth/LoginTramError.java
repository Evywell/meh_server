package fr.evywell.robclient.auth;

import com.jsoniter.annotation.JsonProperty;

public class LoginTramError {

    @JsonProperty(required = true)
    public int code;

    @JsonProperty(required = true)
    public String message;

}
