package fr.evywell.robgame.game.map;

import fr.evywell.common.container.Container;
import fr.evywell.common.container.Service;
import fr.evywell.common.database.Database;
import fr.evywell.common.database.PreparedStatement;
import fr.evywell.common.logger.Log;
import fr.evywell.robgame.database.WorldQuery;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapManager {

    private List<Map> maps;
    private java.util.Map<Integer, Map.MapStructure> cachedMaps;

    public MapManager() {
        this.maps = new ArrayList<>();
        this.cachedMaps = new HashMap<>();
    }

    public void initializeCachedMaps() {
        Database dbWorld = (Database) Container.getInstance(Service.SERVICE_DATABASE_WORLD);
        Statement stmtGetMaps = dbWorld.executeStatement(WorldQuery.SEL_MAPS_IN_WORLD);
        try {
            ResultSet rs = stmtGetMaps.getResultSet();
            Map.MapStructure structure;
            while (rs.next()) {
                structure = new Map.MapStructure();
                structure.mapId = rs.getInt(1);
                structure.maxPlayers = rs.getInt(2);
                structure.instanceId = 0;
                structure.gridW = rs.getInt(3);
                structure.gridH = rs.getInt(4);
                cachedMaps.put(structure.mapId, structure);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(int delta) {
        for (Map m : maps) {
            m.update(delta);
        }
    }

    public Map createMap(int mapId, int instanceId, int maxPlayer) throws Exception {
        if (!cachedMaps.containsKey(mapId)) {
            throw new Exception(String.format("Impossible de créer la carte %d car inconnue", mapId));
        }
        Map.MapStructure structure = cachedMaps.get(mapId);
        Map m = new Map(mapId, instanceId, maxPlayer);
        Log.info("Chargement de la carte mapId=" + mapId + " instanceId=" + instanceId);
        // On construit la grille
        Grid g = new Grid(structure.gridW, structure.gridH);
        g.fillGrid();
        m.attachGrid(g);
        // Initialisation des créatures et gameobjects
        m.loadCreatures();
        this.maps.add(m);
        return m;
    }

    public Map createMap(int mapId, int instanceId) throws Exception {
        Map.MapStructure mapStructure;
        // On récupère les infos de la map par rapport à son ID
        if (cachedMaps.containsKey(mapId)) {
            mapStructure = cachedMaps.get(mapId);
            return createMap(mapId, instanceId, mapStructure.maxPlayers);
        }
        throw new Exception(String.format("Impossible de créer la carte %d car inconnue", mapId));
    }

    public Map createMap(int mapId) throws Exception {
        Map.MapStructure mapStructure;
        // On récupère les infos de la map par rapport à son ID
        if (cachedMaps.containsKey(mapId)) {
            mapStructure = cachedMaps.get(mapId);
            return createMap(mapId, mapStructure.instanceId, mapStructure.maxPlayers);
        }
        throw new Exception(String.format("Impossible de créer la carte %d car inconnue", mapId));
    }

}
