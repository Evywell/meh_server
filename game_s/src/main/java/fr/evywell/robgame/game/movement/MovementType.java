package fr.evywell.robgame.game.movement;

public class MovementType {

    public static final int
        MOVEMENT_NONE = 0x00,
        MOVEMENT_WALK = 0x01,
        MOVEMENT_FORWARD = 0x02,
        MOVEMENT_BACKWARD = 0x04,
        MOVEMENT_TURN_LEFT = 0x08,
        MOVEMENT_TURN_RIGHT = 0x10, // 16
        MOVEMENT_STRAFE_LEFT = 0x20, // 32
        MOVEMENT_STRAFE_RIGHT = 0x40 // 64
    ;
}
