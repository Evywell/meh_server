package fr.evywell.robgame.authentication;

import fr.evywell.common.container.Container;
import fr.evywell.common.container.Service;
import fr.evywell.common.cryptography.AESEncryption;
import fr.evywell.common.cryptography.EncryptionInterface;
import fr.evywell.common.database.Database;
import fr.evywell.common.database.PreparedStatement;
import fr.evywell.common.logger.Log;
import fr.evywell.common.network.Packet;
import fr.evywell.robgame.database.AuthQuery;
import fr.evywell.robgame.network.WorldServer;
import fr.evywell.robgame.network.WorldSession;
import fr.evywell.robgame.world.character.Character;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static fr.evywell.robgame.database.CharacterQuery.SEL_CHARACTERS_BY_USER;
import static fr.evywell.robgame.opcode.Opcode.SMSG_ACCOUNT_CHARACTER_LIST;
import static fr.evywell.robgame.opcode.Opcode.WORLD_AUTHENTICATION_CHALLENGE;

public class AuthenticationChallenge {

    private final AuthTram tram;
    private final WorldSession session;
    private final String secret;
    private final EncryptionInterface encryption;
    private final Database chardb;

    public AuthenticationChallenge(AuthTram tram, WorldSession session, String secret) {
        this.tram = tram;
        this.session = session;
        this.secret = secret;
        this.encryption = new AESEncryption();
        this.chardb = (Database) Container.getInstance(Service.SERVICE_DATABASE_CHARACTERS);
    }

    public void proceed() {
        Log.info(String.format("WORLD_AUTHENTICATION_CHALLENGE initialized for %s", session.getIp()));
        if (session.isAuthenticated()) {
            Log.error("Session already authenticated, skipped");
            return;
        }

        String ticket = encryption.decryptWithSecret(tram.ticket, secret);
        if (ticket == null) {
            Log.error(String.format("Impossible de vérifier un ticket <%s>", tram.ticket));
            session.send(this.getErrorInTicketPacket());
            return;
        }

        String[] ticketParts = ticket.split(":");
        if (ticketParts.length != 3) {
            Log.error(String.format("Impossible de vérifier un ticket <%s>, taille invalide", tram.ticket));
            session.send(this.getErrorInTicketPacket());
            return;
        }
        String uuid = ticketParts[0];
        String token = ticketParts[1];
        String timestamp = ticketParts[2];

        if (!tram.token.equals(token)) {
            Log.error(String.format("Impossible de vérifier un ticket <%s>, token invalid", tram.ticket));
            session.send(this.getErrorInTicketPacket());
            return;
        }

        // Récupération des personnages du compte
        PreparedStatement stmtAccountCharacters = chardb.createPreparedStatement(SEL_CHARACTERS_BY_USER);
        try {
            stmtAccountCharacters.setString(1, uuid);
            stmtAccountCharacters.execute();

            ResultSet rs = stmtAccountCharacters.getResultSet();
            List<Character> characters = new ArrayList<>();
            while (rs.next()) {
                // On parcours les personnages
                Character c = new Character();
                c.uuid = rs.getString(1);
                c.name = rs.getString(2);
                characters.add(c); // Mon IDE me met une erreur ici, mais ça passe
            }

            session.setAuthenticated(true);
            session.setUserUUID(uuid);

            Packet pck = new Packet();
            pck.setCmd(SMSG_ACCOUNT_CHARACTER_LIST);
            pck.add("num_characters", characters.size());
            pck.add("characters", characters);
            session.send(pck);
            Log.info(String.format("Utilisateur <%s> authentifié avec succès", uuid));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        /*

        // On recherche dans la base s'il y a un token valide
        PreparedStatement stmt = this.authDb.getPreparedStatement(AuthQuery.SEL_ACCOUNT_BY_TOKEN);
        try {
            stmt.setString(1, this.tram.token);
            stmt.execute();
            ResultSet rs = stmt.getResultSet();
            if (!rs.next()) {
                // Aucun résultat pour ce token
                this.session.send(new AuthFailedPacket(1, "Token d'authentification invalide"));
                return;
            }
            // On regarde si la personne est bannie
            String banDate = rs.getString(3);
            if (!"".equals(banDate) && banDate != null) {
                Date d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(banDate);
                if ((new Date()).getTime() < d.getTime()) {
                    // Compte encore banni
                    this.session.send(new AuthFailedPacket(2, "Votre compte à été temporairement suspendu"));
                    return;
                }
            }
            // Tout est bon, on connecte la personne au serveur de jeu
            this.session.setAuthenticated(true);
            this.session.setUserId(rs.getInt(1));
            ((WorldServer) this.session.getServer()).getWorld().addSession(this.session);
            Packet p = new Packet();
            p.setCmd(WORLD_AUTHENTICATION_CHALLENGE);
            p.add("status", 1);
            this.session.send(p);
            Log.info(String.format("Utilisateur <%s:%s> authentifié avec succès", rs.getString(2), this.tram.token));
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        }

         */
    }

    private Packet getErrorInTicketPacket() {
        AuthFailedPacket pck = new AuthFailedPacket();
        pck.setCode(1);
        pck.setMessage("Unable to verify the ticket");
        return pck;
    }

}
