package fr.evywell.robgame.game.map;

import fr.evywell.common.container.Container;
import fr.evywell.common.container.Service;
import fr.evywell.common.database.Database;
import fr.evywell.common.database.PreparedStatement;
import fr.evywell.common.logger.Log;
import fr.evywell.robgame.database.WorldQuery;
import fr.evywell.robgame.game.entities.*;
import fr.evywell.robgame.game.map.grid.Cell;
import fr.evywell.robgame.game.map.grid.Grid;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Map {

    protected final int mapId;
    protected final int instanceId;
    protected final int maxPlayers;
    protected final int width;
    protected final int height;
    protected final float sightDistance;
    protected Grid grid;
    protected VirtualMap vmap;

    protected java.util.Map<Integer, Player> players;
    protected java.util.Map<Integer, Unit> creatures;
    protected java.util.Map<Integer, GameObject> gos;

    protected CreatureManager creatureManager;

    public static final int MAX_PLAYERS = 20;
    public static final float DEFAULT_SIGHT_RANGE = 20.0f;

    public Map(int mapId, int instanceId, int maxPlayers, int width, int height, float sightDistance) {
        this.mapId = mapId;
        this.instanceId = instanceId;
        this.maxPlayers = maxPlayers;
        this.width = width;
        this.height = height;
        this.sightDistance = sightDistance;
        this.creatureManager = (CreatureManager) Container.getInstance(fr.evywell.robgame.Service.CREATURE_MANAGER);
        this.players = new HashMap<>();
        this.creatures = new HashMap<>();
        this.gos = new HashMap<>();
    }

    public Map(int mapId, int instanceId, int maxPlayers, int width, int height) {
        this(mapId, instanceId, maxPlayers, width, height, Map.DEFAULT_SIGHT_RANGE);
    }


    public void update(int delta) {
        for (GameObject go : this.players.values()) {
            go.update(delta);
        }
        for (GameObject go : this.creatures.values()) {
            go.update(delta);
        }
        for (GameObject go : this.gos.values()) {
            go.update(delta);
        }
    }

    public void addToMap(GameObject go) {
        switch (go.guid.getType()) {
            case ObjectGuid.PLAYER:
                players.put(go.guid.getUuid(), (Player) go);
                break;
            case ObjectGuid.CREATURE:
                creatures.put(go.guid.getUuid(), (Unit) go);
                break;
            case ObjectGuid.GAME_OBJECT:
                gos.put(go.guid.getUuid(), go);
                break;
        }
        grid.addToGrid(go);
        go.setMap(this);
    }

    public GameObject findGameObject(ObjectGuid guid) {
        int uuid = guid.getUuid();
        switch (guid.getType()) {
            case ObjectGuid.PLAYER:
                return players.getOrDefault(uuid, null);
            case ObjectGuid.CREATURE:
                return creatures.getOrDefault(uuid, null);
            case ObjectGuid.GAME_OBJECT:
                return gos.getOrDefault(uuid, null);
        }
        return null;
    }

    public GameObject[] getGameObjectsInAreaOf(GameObject go) {
        Cell[] cells = go.getCell().getNeighboring();
        List<GameObject> gameObjects = new ArrayList<>();
        for (Cell cell : cells) {
            List<GameObject> cellGos = cell.getGameObjects();
            if (cellGos.isEmpty()) {
                continue;
            }
            // On récupère les game objects de la cellule
            if (cell.equals(go.getCell())) {
                if (cellGos.size() == 1) {
                    // Le seul Go dans la cellule est celui passé en paramètre
                    continue;
                }
                // Si la cellule du gameobject passé en paramètres correspond à la cellule en cours
                // On trie les game objects pour pas qu'elle n'apparaisse dans la liste
                int i = cellGos.indexOf(go);
                // Si le go est le dernier de la liste, on prend TOUT sauf le dernier
                if (i == cellGos.size() - 1) {
                    gameObjects.addAll(cellGos.subList(0, cellGos.size() - 2));
                // Si c'est le premier, on prend TOUT sauf le premier
                } else if (i == 0 && cellGos.size() > 1) {
                    gameObjects.addAll(cellGos.subList(1, cellGos.size() - 1));
                } else {
                    gameObjects.addAll(cellGos.subList(0, i - 1));
                    gameObjects.addAll(cellGos.subList(i + 1, cellGos.size() - 1));
                }
            } else {
                gameObjects.addAll(cellGos);
            }
        }

        GameObject[] gos = new GameObject[gameObjects.size()];
        return gameObjects.toArray(gos);
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
                int templateId = rs.getInt(2);
                t = this.creatureManager.getCreatureTemplate(templateId);
                c = new Creature();
                c.mapId = mapId;
                c.name = t.name;
                c.guid = ObjectGuid.createCreature(rs.getInt(1));
                c.sightDistance = t.sightDistance;
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

    public int getMapId() {
        return mapId;
    }

    public float getSightDistance() {
        return this.sightDistance;
    }

    public static class MapStructure {
        int mapId;
        int instanceId;
        int maxPlayers = MAX_PLAYERS;
        int gridW, gridH;
        float sightDistance = Map.DEFAULT_SIGHT_RANGE;
    }

}
