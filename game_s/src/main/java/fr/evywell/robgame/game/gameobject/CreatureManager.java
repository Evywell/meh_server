package fr.evywell.robgame.game.gameobject;

import fr.evywell.common.container.Container;
import fr.evywell.common.container.Service;
import fr.evywell.common.database.Database;
import fr.evywell.robgame.database.WorldQuery;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class CreatureManager extends GameObjectManager {

    private Map<Integer, Creature.Template> cachedCreatures = new HashMap<>();

    public Creature.Template getCreatureTemplate(int uuid) {
        return cachedCreatures.get(uuid);
    }

    public void initializeCachedCreatures() {
        Database worldDb = (Database) Container.getInstance(Service.SERVICE_DATABASE_WORLD);
        Statement stmtGetCachedCreatures = worldDb.executeStatement(WorldQuery.SEL_CREATURES_TEMPLATE);
        try {
            ResultSet rs = stmtGetCachedCreatures.getResultSet();
            Creature.Template ct;
            while (rs.next()) {
                ct = new Creature.Template();
                ct.id = rs.getInt(1);
                ct.name = rs.getString(2);
                cachedCreatures.put(ct.id, ct);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
