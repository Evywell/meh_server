package fr.evywell.robgame.world.character;

import fr.evywell.common.container.Container;
import fr.evywell.common.container.Service;
import fr.evywell.common.database.Database;
import fr.evywell.common.database.PreparedStatement;
import fr.evywell.common.network.Packet;
import fr.evywell.robgame.database.CharacterQuery;
import fr.evywell.robgame.network.WorldServer;
import fr.evywell.robgame.network.WorldSession;
import fr.evywell.robgame.opcode.Handler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static fr.evywell.robgame.opcode.Opcode.SMSG_ACCOUNT_CHARACTER_LIST;

public class CharacterListHandler implements Handler {

    private Database characterDb;

    public CharacterListHandler() {
        //this.characterDb = (Database) Container.getInstance(Service.SERVICE_DATABASE_CHARACTERS);
    }

    @Override
    public void call(WorldSession session, Object payload, Packet packet) {
        /*
        // Bon, ici la personne est censée être identifiée
        // On récupère la liste des personnage du realm
        PreparedStatement stmt = this.characterDb.getPreparedStatement(CharacterQuery.SEL_CHARACTERS_BY_USER);
        try {
            stmt.setInt(1, ((WorldServer) session.getServer()).getWorld().getRealm().realmId);
            stmt.setInt(2, session.getUserId());
            stmt.execute();
            ResultSet rs = stmt.getResultSet();
            List<Character> characters = new ArrayList<>();
            while (rs.next()) {
                // On parcours les personnages
                Character c = new Character();
                c.guid = rs.getString(1);
                c.name = rs.getString(2);
                characters.add(c); // Mon IDE me met une erreur ici, mais ça passe
            }

            packet.add("characters", characters);
            packet.add("num_characters", characters.size());
            packet.setCmd(SMSG_ACCOUNT_CHARACTER_LIST);
            session.send(packet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
         */
    }

    @Override
    public Class getPayloadTemplate() {
        return CharacterListPayload.class;
    }
}
