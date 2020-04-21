package fr.evywell.robauth.ticket;

import fr.evywell.common.container.Container;
import fr.evywell.common.container.Service;
import fr.evywell.common.cryptography.AESEncryption;
import fr.evywell.common.cryptography.EncryptionInterface;
import fr.evywell.common.database.Database;
import fr.evywell.common.database.PreparedStatement;
import fr.evywell.common.logger.Log;
import fr.evywell.common.network.Packet;
import fr.evywell.robauth.authentification.AuthFailedPacket;
import fr.evywell.robauth.network.AuthSession;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import static fr.evywell.robauth.database.AuthQuery.AUTH_SEL_CLIENT_INFO_BY_CODE;
import static fr.evywell.robauth.network.AuthServer.*;

public class HandleGameTicketRequest {

    private final TicketTram tram;
    private final AuthSession session;
    private final Database authDb;
    private final EncryptionInterface encryption;

    public HandleGameTicketRequest(TicketTram tram, AuthSession session) {
        this.tram = tram;
        this.session = session;
        this.authDb = (Database) Container.getInstance(Service.SERVICE_DATABASE_AUTH);
        this.encryption = new AESEncryption();
    }

    public void proceed() {
        Log.info(String.format("AUTH_GAME_TICKET initialized for %s", session.getIp()));
        if (!this.session.isAuthenticated()) {
            Log.error("Session non authentifié");
            this.session.send(new AuthFailedPacket(AUTH_ERR_NOT_AUTHENTICATED, "Not authenticated"));
            this.session.kick();
            return;
        }
        String secret = this.getSecretFromGameCode(tram.game_code);
        if (secret == null) {
            Log.error(String.format("Game code Unknown %s", tram.game_code));
            this.session.send(new AuthFailedPacket(AUTH_ERR_GAME_CODE_UNKNOWN, "The game code is unknown"));
            return;
        }
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        String passphrase = this.encryption.encryptFromSecret(
            session.getUuid() + ":" + session.getToken() + ":" + timestamp.getTime(),
            secret
        );

        Packet pck = new Packet();
        pck.setCmd(AUTH_GAME_TICKET_SUCCEED);
        pck.add("ticket", passphrase);
        session.send(pck);
    }

    private String getSecretFromGameCode(String gameCode) {
        PreparedStatement stmtClientInfos = this.authDb.getPreparedStatement(AUTH_SEL_CLIENT_INFO_BY_CODE);
        try {
            stmtClientInfos.setString(1, gameCode);
            stmtClientInfos.execute();

            ResultSet rs = stmtClientInfos.getResultSet();
            if (!rs.next()) {
                return null;
            }
            return rs.getString(1);
        } catch (SQLException e) {
            Log.error("Impossible de récupérer le SECRET à partir du gameCode");
            e.printStackTrace();
            this.session.kick();
        }
        return null;
    }
}
