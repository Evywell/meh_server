package fr.evywell.robgame.network.entities;


import fr.evywell.robgame.game.entities.GameObject;

public class AddGameObjectPacket extends UpdateGameObjectPacket {

    public AddGameObjectPacket(GameObject go) {
        super(UpdateGameObjectPacket.TYPE_ADD);
        go.putIntoPacket(this);
    }

}
