package fr.evywell.robauth.authentification;

import com.jsoniter.annotation.JsonProperty;

public class AuthTram {

    @JsonProperty(required = true)
    public String login;

    @JsonProperty(required = true)
    public String password;

}
