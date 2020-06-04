package fr.evywell.robclient.game.character;

import com.jsoniter.annotation.JsonProperty;

import java.util.Map;

public class InvokeCharacterInWorldTram {

    @JsonProperty(required = true)
    public Map<String, Object> player;

}
