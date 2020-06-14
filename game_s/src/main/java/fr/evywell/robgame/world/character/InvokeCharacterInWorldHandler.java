package fr.evywell.robgame.world.character;

import fr.evywell.common.container.Container;
import fr.evywell.common.container.Service;
import fr.evywell.common.database.Database;
import fr.evywell.common.database.PreparedStatement;
import fr.evywell.common.logger.Log;
import fr.evywell.common.network.Packet;
import fr.evywell.common.network.Session;
import fr.evywell.common.opcode.Handler;
import fr.evywell.robgame.database.CharacterQuery;
import fr.evywell.robgame.game.gameobject.GameObject;
import fr.evywell.robgame.game.gameobject.GameObjectInfoPacket;
import fr.evywell.robgame.game.gameobject.Player;
import fr.evywell.robgame.network.WorldSession;
import fr.evywell.robgame.opcode.Opcode;
import fr.evywell.robgame.world.World;

import java.sql.ResultSet;
import java.sql.SQLException;

public class InvokeCharacterInWorldHandler implements Handler {

    private Database characterDb;
    private World world;

    public InvokeCharacterInWorldHandler() {
        this.characterDb = ((Database) Container.getInstance(Service.SERVICE_DATABASE_CHARACTERS));
        this.world = (World) Container.getInstance(fr.evywell.robgame.Service.WORLD);
    }

    @Override
    public void call(Session session, Object payload, Packet packet) {
        InvokeCharacterInWorldPayload tram = (InvokeCharacterInWorldPayload) payload;
        Log.info("Invoke Player un World: " + tram.character_uuid);
        PreparedStatement stmtCharacter = this.characterDb.getPreparedStatement(CharacterQuery.SEL_CHARACTER_BY_UUID);
        try {
            stmtCharacter.setString(1, ((WorldSession) session).getUserUUID());
            stmtCharacter.setString(2, tram.character_uuid);
            stmtCharacter.execute();

            ResultSet rs = stmtCharacter.getResultSet();
            if (!rs.next()) {
                // Aucune correspondance en base de données
                return;
            }
            Player p = new Player((WorldSession) session);
            p.uuid = rs.getString(1);
            p.name = rs.getString(2);;
            p.mapId = rs.getInt(3);
            p.pos_x = rs.getInt(4);
            p.pos_y = rs.getInt(5);
            p.pos_z = rs.getInt(6);

            ((WorldSession) session).setPlayer(p);

            // On envoie les informations sur le joueur et on le place dans un queue
            session.send(new InvokeCharacterInWorldPacket(p));
            world.putPlayerInQueue((WorldSession) session, p);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Class getPayloadTemplate() {
        return InvokeCharacterInWorldPayload.class;
    }
}
