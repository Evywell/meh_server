package fr.evywell.robgame.world.character;

import com.jsoniter.annotation.JsonProperty;

public class CharacterListPayload {

    @JsonProperty(required = true)
    public String realm;

}
