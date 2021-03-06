package fr.evywell.robgame.authentication;

import fr.evywell.common.network.Packet;

import static fr.evywell.robgame.opcode.Opcode.WORLD_AUTHENTICATION_CHALLENGE_FAILED;

public class AuthFailedPacket extends Packet {

    public AuthFailedPacket() {
        super(WORLD_AUTHENTICATION_CHALLENGE_FAILED);
    }

    public AuthFailedPacket(int code, String message) {
        this();
        this.setCode(code);
        this.setMessage(message);
    }

    public void setCode(int code) {
        this.putInt(code);
    }

    public void setMessage(String message) {
        this.putString(message);
    }
}
