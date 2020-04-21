package fr.evywell.robauth.authentification;

import fr.evywell.common.container.Container;
import fr.evywell.common.container.Service;
import fr.evywell.common.database.Database;
import fr.evywell.common.database.PreparedStatement;
import fr.evywell.common.logger.Log;
import fr.evywell.common.network.Packet;
import fr.evywell.common.utils.RandomString;
import fr.evywell.robauth.database.AuthQuery;
import fr.evywell.robauth.network.AuthSession;

import java.sql.ResultSet;
import java.sql.SQLException;

import static fr.evywell.robauth.network.AuthServer.*;

public class ClientAuthChallenge {

    private ClientTram tram;
    private AuthSession session;
    private Database authDb;

    public ClientAuthChallenge(ClientTram tram, AuthSession session) {
        this.tram = tram;
        this.session = session;
        this.authDb = (Database) Container.getInstance(Service.SERVICE_DATABASE_AUTH);
    }

    public void proceed() {
        Log.info(String.format("AUTH_CLIENT_CHALLENGE initialized for %s", session.getIp()));
        // 1. On récupère les infos concernant le client
        PreparedStatement stmtInfosClient = this.authDb.getPreparedStatement(AuthQuery.AUTH_SEL_CLIENT_INFO);
        try {
            stmtInfosClient.setString(1, this.tram.client_id);
            stmtInfosClient.execute();
            ResultSet rs = stmtInfosClient.getResultSet();
            if (!rs.next()) {
                // Aucune correspondance
                Log.info(String.format("Tentative d'authentification de serveur de jeu infructueuse, raison: Unknown client ID %s", tram.client_id));
                this.session.send(new AuthFailedPacket(AUTH_ERR_BAD_CLIENT_ID, "Bad Client Id"));
                this.session.kick();
                return;
            }
            // 2. On vérifie si l'IP est whitelist
            String whitelistedIp = rs.getString(4);
            if (!whitelistedIp.equals(session.getIp())) {
                // 3a. L'ip n'est pas whitelist, on arrête là
                Log.info(String.format("Tentative d'authentification de serveur de jeu infructueuse, raison: Bad IP Address"));
                this.session.send(new AuthFailedPacket(AUTH_ERR_BAD_IP_ADDRESS, "Bad IP Address"));
                this.session.kick();
            }
            // 3b. L'ip est whitelist, on continue
            // 4. On définie la session comme serveur de jeu
            this.session.setGameServer(true);
            this.session.setAuthenticated(true);
            // 5. On génère une clé secret
            String secret = (new RandomString(150)).nextString();
            // 6. On enregistre en base la clé secret
            PreparedStatement stmtUpdSecret = this.authDb.getPreparedStatement(AuthQuery.AUTH_UPD_CLIENT_SECRET);
            stmtUpdSecret.setString(1, secret); // secret
            stmtUpdSecret.setInt(2, rs.getInt(1)); // id
            stmtUpdSecret.execute();

            // 7. On envoie le paquet de succès
            Packet pck = new Packet();
            pck.setCmd(AUTH_CLIENT_LOGIN_SUCCEED);
            pck.add("secret", secret);
            this.session.send(pck);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
