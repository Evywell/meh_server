package fr.evywell.robgame.game.map;

import fr.evywell.common.container.Container;
import fr.evywell.common.container.Service;
import fr.evywell.common.database.Database;
import fr.evywell.common.database.PreparedStatement;
import fr.evywell.common.logger.Log;
import fr.evywell.robgame.database.WorldQuery;
import fr.evywell.robgame.game.gameobject.*;
import fr.evywell.robgame.game.map.grid.Cell;
import fr.evywell.robgame.game.map.grid.Grid;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Map {

    protected final int mapId;
    protected final int instanceId;
    protected final int maxPlayers;
    protected final int width;
    protected final int height;
    protected Grid grid;
    protected VirtualMap vmap;

    protected List<GameObject> go;

    protected CreatureManager creatureManager;

    public static final int MAX_PLAYERS = 20;

    public Map(int mapId, int instanceId, int maxPlayers, int width, int height) {
        this.mapId = mapId;
        this.instanceId = instanceId;
        this.maxPlayers = maxPlayers;
        this.width = width;
        this.height = height;
        this.creatureManager = (CreatureManager) Container.getInstance(fr.evywell.robgame.Service.CREATURE_MANAGER);
        this.go = new ArrayList<>();
    }

    public void update(int delta) {
        for (GameObject go : this.go) {
            go.update(delta);
        }
    }

    public void addToMap(GameObject go) {
        this.go.add(go);
        grid.addToGrid(go);
        go.setMap(this);
        Cell cell = this.grid.getCellFromGameObject(go);
        go.setCell(cell);
    }

    public void addPlayerToMap(Player p) {
        this.addToMap(p);
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
                c.setMap(this);
                this.addToMap(c);
                Log.debug(String.format("Creature %s ajoutée", c.toString()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void moveGameObjectInMap(GameObject go) {
        grid.moveGameObject(go);
    }

    public void setVirtualMap(VirtualMap vmap) {
        this.vmap = vmap;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public static class MapStructure {
        int mapId;
        int instanceId;
        int maxPlayers = MAX_PLAYERS;
        int gridW, gridH;
    }

}
