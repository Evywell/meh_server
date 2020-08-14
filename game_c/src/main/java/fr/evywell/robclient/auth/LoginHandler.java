package fr.evywell.robclient.auth;

import com.jsoniter.JsonIterator;
import fr.evywell.common.logger.Log;
import fr.evywell.common.network.Packet;
import fr.evywell.common.network.Session;
import fr.evywell.common.opcode.Handler;
import fr.evywell.robclient.Application;

import javax.swing.*;

public class LoginHandler implements Handler {

    private Application app;
    private boolean isLoginSucceed;

    public LoginHandler(Application app, boolean isLoginSucceed) {
        this.isLoginSucceed = isLoginSucceed;
        this.app = app;
    }

    @Override
    public void call(Session session, Object payload, Packet packet) {
        if (!isLoginSucceed) {
            String message = packet.readString();
            Log.error(message);
            JOptionPane.showMessageDialog(null, message, "Erreur d'authentification", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // On demande un ticket pour jouer au jeu
        String token = packet.readString();
        app.setToken(token);
        app.getAuthClient().sendObtainTicket(token);
    }

    @Override
    public Object getPayload(Packet packet) {
        return null;
    }

}
