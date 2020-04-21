package fr.evywell.robgame.game.map;

import fr.evywell.common.container.Container;
import fr.evywell.common.container.Service;
import fr.evywell.common.database.Database;
import fr.evywell.common.database.PreparedStatement;
import fr.evywell.common.logger.Log;
import fr.evywell.robgame.database.WorldQuery;
import fr.evywell.robgame.game.gameobject.Creature;
import fr.evywell.robgame.game.gameobject.CreatureManager;
import fr.evywell.robgame.game.gameobject.GameObject;
import fr.evywell.robgame.game.gameobject.Unit;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Map {

    protected int mapId;
    protected int instanceId;
    protected int maxPlayers;
    protected Grid grid;

    protected List<GameObject> go;

    protected CreatureManager creatureManager;

    public static final int MAX_PLAYERS = 20;

    public Map(int mapId, int instanceId, int maxPlayers) {
        this.mapId = mapId;
        this.instanceId = instanceId;
        this.maxPlayers = maxPlayers;
        this.creatureManager = (CreatureManager) Container.getInstance(fr.evywell.robgame.Service.CREATURE_MANAGER);
        this.go = new ArrayList<>();
    }

    public Cell getUnitGridCell(Unit unit) {
        int gridX = (int) Math.ceil(unit.pos_x / Cell.CELL_WIDTH);
        int gridY = (int) Math.ceil(unit.pos_y / Cell.CELL_WIDTH);
        return grid.getCell(gridX, gridY);
    }

    public void update(int delta) {
        for (GameObject go : this.go) {
            go.update(delta);
        }
    }

    public void attachGrid(Grid grid) {
        this.grid = grid;
    }

    public void loadCreatures() {
        Log.info("Chargement des créatures pour la map " + mapId);
        Database worldDb = (Database) Container.getInstance(Service.SERVICE_DATABASE_WORLD);
        PreparedStatement stmtCreatures = worldDb.getPreparedStatement(WorldQuery.SEL_MAP_CREATURES);
        try {
            stmtCreatures.setInt(1, mapId);
            stmtCreatures.execute();

            ResultSet rs = stmtCreatures.getResultSet();
            Creature.Template t;
            Creature c;
            while (rs.next()) {
                String templateUuid = rs.getString(2);
                t = this.creatureManager.getCreatureTemplate(templateUuid);
                c = new Creature();
                c.mapId = mapId;
                c.name = t.name;
                c.uuid = rs.getString(1);
                c.pos_x = rs.getFloat(3);
                c.pos_y = rs.getFloat(4);
                c.pos_z = rs.getFloat(5);
                c.orientation = rs.getFloat(6);
                Cell cell = this.getUnitGridCell(c);
                this.go.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static class MapStructure {
        int mapId;
        int instanceId;
        int maxPlayers = MAX_PLAYERS;
        int gridW, gridH;
    }

}
