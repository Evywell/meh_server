package fr.evywell.robgame.world.character;

import fr.evywell.common.database.Database;
import fr.evywell.common.network.Packet;
import fr.evywell.common.network.Session;
import fr.evywell.common.opcode.Handler;

public class CharacterListHandler implements Handler {

    private Database characterDb;

    public CharacterListHandler() {
        //this.characterDb = (Database) Container.getInstance(Service.SERVICE_DATABASE_CHARACTERS);
    }

    @Override
    public void call(Session session, Object payload, Packet packet) {
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
