package fr.evywell.robauth.authentification;

import fr.evywell.common.utils.Password;
import fr.evywell.robauth.database.AuthQuery;
import fr.evywell.robauth.network.AuthServer;
import fr.evywell.robauth.network.AuthSession;
import fr.evywell.common.container.Container;
import fr.evywell.common.container.Service;
import fr.evywell.common.database.Database;
import fr.evywell.common.database.PreparedStatement;
import fr.evywell.common.logger.Log;
import fr.evywell.common.network.Packet;
import fr.evywell.common.utils.RandomString;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static fr.evywell.robauth.network.AuthServer.*;

public class LogonChallenge {

    private final AuthTram tram;
    private final AuthSession session;
    private Database authDb;

    private RandomString randomString; // Générateur de token

    public LogonChallenge(AuthTram tram, AuthSession session) {
        this.tram = tram;
        this.session = session;
        this.authDb = (Database) Container.getInstance(Service.SERVICE_DATABASE_AUTH);
        this.randomString = new RandomString();
    }

    public void proceed() {
        Log.info(String.format("AUTH_LOGON_CHALLENGE initialized for %s", session.getIp()));

        PreparedStatement stmt = this.authDb.getPreparedStatement(AuthQuery.AUTH_SEL_ACCOUNT_INFO);
        try {
            stmt.setString(1, tram.login);
            stmt.execute();
            ResultSet rs = stmt.getResultSet();
            if (!rs.next()) {
                // Aucune correspondance
                Log.info(String.format("Tentative de connexion infructueuse pour %s:%s, raison: No user with %s username", tram.login, tram.password, tram.login));
                this.session.send(new AuthFailedPacket(AUTH_ERR_BAD_CREDENTIALS, "Bad Credentials"));
                return;
            }
            // On fait correspondre les mots de passe
            String dbPass = rs.getString(4);
            String salt = rs.getString(5);
            String hashedPassword = tram.password + ":" + salt;
            Log.info(hashedPassword);

            Password pass = new Password();
            if (!pass.verify(hashedPassword, dbPass)) {
                Log.info(String.format("Tentative de connexion infructueuse pour %s:%s, raison: Wrong password", tram.login, tram.password));
                this.session.send(new AuthFailedPacket(AUTH_ERR_BAD_CREDENTIALS, "Bad Credentials"));
                return;
            }

            // On regarde si le compte est banni
            String banDate = rs.getString(6);
            if (!"".equals(banDate) && banDate != null) {
                Date d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(banDate);
                if ((new Date()).getTime() < d.getTime()) {
                    // Compte encore banni
                    Log.info(String.format("Tentative de connexion infructueuse pour %s:%s, raison: User %s is still banned", tram.login, tram.password, tram.login));
                    this.session.send(new AuthFailedPacket(AUTH_ERR_BANNED_USER, "Your account have been suspended"));
                    return;
                }
            }

            String token = rs.getString(7);
            if (!"".equals(token) && token != null) {
                // Si une personne est déjà connectée, on la kick
                ((AuthServer) this.session.getServer()).kickByToken(token);
            }

            // Tout va bien
            int userId = rs.getInt(1);
            // Génération du token d'authentification
            token = this.randomString.nextString();

            // On sauvegarde le token en base
            PreparedStatement tokenStmt = this.authDb.getPreparedStatement(AuthQuery.AUTH_UPD_ACCOUNT_TOKEN);
            tokenStmt.setString(1, token);
            tokenStmt.setInt(2, userId);
            tokenStmt.execute();

            session.setUuid(rs.getString(2)); // UUID
            session.setToken(token);
            session.setAuthenticated(true);

            // On envoie le token
            Log.info(String.format("Connexion de <%s> effectuée avec succès. Token: <%s>", tram.login, token));
            Packet pck = new Packet();
            pck.setCmd(AUTH_LOGIN_SUCCEED);
            pck.add("token", token);
            this.session.send(pck);
        } catch (SQLException e) {
            e.printStackTrace();
            this.session.kick();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
