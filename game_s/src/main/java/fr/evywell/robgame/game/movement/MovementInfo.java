package fr.evywell.robgame.game.movement;

import fr.evywell.common.maths.Vector3;
import fr.evywell.robgame.game.entities.ObjectGuid;

public class MovementInfo {

    public ObjectGuid guid;
    public Vector3 position;
    public float orientation;
    public int flags;

    public boolean hasFlag(int flag) {
        return (flags & flag) != 0;
    }

    public void removeFlag(int flag) {
        flags &= ~flag; // On inverse les bits du flag et on compare avec tous les flags
    }

    @Override
    public String toString() {
        return "MovementInfo{" +
                "guid='" + guid + '\'' +
                ", position=" + position +
                ", orientation=" + orientation +
                ", flags=" + flags +
                '}';
    }
}
