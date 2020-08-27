package fr.evywell.robgame.network.entities;

import fr.evywell.robgame.game.entities.GameObject;
import fr.evywell.robgame.game.entities.ObjectGuid;

public class RemoveGameObjectPacket extends UpdateGameObjectPacket {

    public RemoveGameObjectPacket(GameObject go) {
        super(UpdateGameObjectPacket.TYPE_REMOVE);
        putLong(go.guid.getGuid());
    }

    public RemoveGameObjectPacket(ObjectGuid guid) {
        super(UpdateGameObjectPacket.TYPE_REMOVE);
        putLong(guid.getGuid());
    }

}
