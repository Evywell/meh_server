package fr.evywell.robgame.game.movement;

import fr.evywell.common.maths.Vector3;

public class MovementInfo {

    public String uuid;
    public Vector3 position;
    public float orientation;
    public int flags;

    public boolean hasFlag(int flag) {
        return (flags & flag) != 0;
    }

    public void removeFlag(int flag) {
        flags &= ~flag; // On inverse les bits du flag et on compare avec tous les flags
    }

}
