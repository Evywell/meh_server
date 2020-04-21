package fr.evywell.robauth.authentification;

import com.jsoniter.annotation.JsonProperty;

public class ClientTram {

    @JsonProperty(required = true)
    public String client_id;

}
