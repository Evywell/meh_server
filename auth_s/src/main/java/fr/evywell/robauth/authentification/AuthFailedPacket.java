package fr.evywell.robauth.authentification;

import fr.evywell.common.network.Packet;

import static fr.evywell.robauth.network.AuthServer.AUTH_LOGIN_FAILED;

public class AuthFailedPacket extends Packet {

    public AuthFailedPacket() {
        super(AUTH_LOGIN_FAILED);
    }

    public AuthFailedPacket(int code, String message) {
        super(code);
        putString(message);
    }

}
