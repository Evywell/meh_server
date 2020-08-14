package fr.evywell.robauth.network;

import com.jsoniter.JsonIterator;
import fr.evywell.common.logger.Log;
import fr.evywell.common.network.MalformedRequestException;
import fr.evywell.common.network.Packet;
import fr.evywell.common.network.Server;
import fr.evywell.common.network.Session;
import fr.evywell.robauth.authentification.AuthTram;
import fr.evywell.robauth.authentification.ClientAuthChallenge;
import fr.evywell.robauth.authentification.ClientTram;
import fr.evywell.robauth.authentification.LogonChallenge;
import fr.evywell.robauth.ticket.HandleGameTicketRequest;
import fr.evywell.robauth.ticket.TicketTram;

import java.io.IOException;

public class AuthServer extends Server
{
    public static final int AUTH_LOGON_CHALLENGE = 0;
    public static final int AUTH_CLIENT_CHALLENGE = 1;
    public static final int AUTH_GAME_TICKET = 2;

    // Flag paquets réponse
    public static final int AUTH_LOGIN_FAILED = 0;
    public static final int AUTH_LOGIN_SUCCEED = 1;
    public static final int AUTH_CLIENT_LOGIN_SUCCEED = 2;
    public static final int AUTH_GAME_TICKET_SUCCEED = 3;

    // Errors
    public static final int AUTH_ERR_BAD_CREDENTIALS = 0;
    public static final int AUTH_ERR_BANNED_USER = 1;
    public static final int AUTH_ERR_BAD_CLIENT_ID = 2;
    public static final int AUTH_ERR_BAD_IP_ADDRESS = 3;
    public static final int AUTH_ERR_NOT_AUTHENTICATED = 4;
    public static final int AUTH_ERR_GAME_CODE_UNKNOWN = 5;

    public AuthServer(int port) {
        super(port);
    }

    @Override
    public void beforeStarting() {
        // Entête du serveur d'authentification
        System.out.println("\n" +
            "██████╗  ██████╗ ██████╗     █████╗ ██╗   ██╗████████╗██╗  ██╗\n" +
            "██╔══██╗██╔═══██╗██╔══██╗   ██╔══██╗██║   ██║╚══██╔══╝██║  ██║\n" +
            "██████╔╝██║   ██║██████╔╝   ███████║██║   ██║   ██║   ███████║\n" +
            "██╔══██╗██║   ██║██╔══██╗   ██╔══██║██║   ██║   ██║   ██╔══██║\n" +
            "██║  ██║╚██████╔╝██████╔╝   ██║  ██║╚██████╔╝   ██║   ██║  ██║\n" +
            "╚═╝  ╚═╝ ╚═════╝ ╚═════╝    ╚═╝  ╚═╝ ╚═════╝    ╚═╝   ╚═╝  ╚═╝\n");
    }

    @Override
    public void afterStopping() {
        Log.info("Serveur d'authentification arrêté avec succès");
    }

    @Override
    public void dispatch(int _cmd, Packet packet, Session session) throws MalformedRequestException {
        if (_cmd < 0) {
            throw new MalformedRequestException(String.format("La command %s n'existe pas", _cmd));
        }
        // Un ptit switch case
        switch (_cmd) {
            case AUTH_LOGON_CHALLENGE:
                AuthTram authTram = new AuthTram();
                authTram.login = packet.readString();
                authTram.password = packet.readString();
                (new LogonChallenge(authTram, (AuthSession) session)).proceed();
                break;
            case AUTH_CLIENT_CHALLENGE:
                ClientTram clientTram = new ClientTram();
                clientTram.client_id = packet.readString();
                (new ClientAuthChallenge(clientTram, (AuthSession) session)).proceed();
                break;
            case AUTH_GAME_TICKET:
                TicketTram ticketTram = new TicketTram();
                ticketTram.game_code = packet.readString();
                ticketTram.token = packet.readString();
                (new HandleGameTicketRequest(ticketTram, (AuthSession) session)).proceed();
                break;
            default:
                throw new MalformedRequestException(String.format("La command %s n'existe pas", _cmd));
        }
    }

    public void kickByToken(String token) {
        for (Session s : this.getSessions()) {
            if (((AuthSession) s).isAuthenticated() && token.equals(((AuthSession) s).getToken())) {
                s.kick();
            }
        }
    }

    public void kickAll() {
        for (Session s : this.getSessions()) {
            s.kick();
        }
    }
}
