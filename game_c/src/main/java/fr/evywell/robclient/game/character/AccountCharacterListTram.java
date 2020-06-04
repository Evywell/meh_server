package fr.evywell.robclient.game.character;

import com.jsoniter.annotation.JsonProperty;

import java.util.List;

public class AccountCharacterListTram {

    @JsonProperty(required = true)
    public int num_characters;

    @JsonProperty(required = true)
    public List<Character> characters;

}
