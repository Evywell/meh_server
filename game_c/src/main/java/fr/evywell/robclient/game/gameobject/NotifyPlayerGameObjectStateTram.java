package fr.evywell.robclient.game.gameobject;

import com.jsoniter.annotation.JsonProperty;

public class NotifyPlayerGameObjectStateTram {

    @JsonProperty(required = true)
    public String uuid;

    @JsonProperty(required = true)
    public float pos_x;

    @JsonProperty(required = true)
    public float pos_y;

    @JsonProperty(required = true)
    public float pos_z;

}
