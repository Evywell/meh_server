package fr.evywell.robauth.authentification;

import fr.evywell.common.network.Packet;

import static fr.evywell.robauth.network.AuthServer.AUTH_LOGIN_FAILED;

public class AuthFailedPacket extends Packet {

    public AuthFailedPacket() {
        this.setCmd(AUTH_LOGIN_FAILED);
    }

    public AuthFailedPacket(int code, String message) {
        this();
        this.setCode(code);
        this.setMessage(message);
    }

    public void setCode(int code) {
        this.add("code", code);
    }

    public void setMessage(String message) {
        this.add("message", message);
    }
}
