package fr.evywell.robgame.world.character;

import com.jsoniter.annotation.JsonProperty;

public class InvokeCharacterInWorldPayload {

    @JsonProperty(required = true)
    public String character_uuid;

}
