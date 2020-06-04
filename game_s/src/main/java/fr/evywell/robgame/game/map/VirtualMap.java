package fr.evywell.robgame.game.map;

import fr.evywell.common.config.Config;
import fr.evywell.common.container.Container;
import fr.evywell.common.logger.Log;
import fr.evywell.robgame.Service;
import fr.evywell.robgame.config.VMapConfig;
import fr.evywell.robgame.game.gameobject.Unit;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class VirtualMap {

    public static final String FILE_FORMAT = "%s_vmap.bin";

    private final Map map;
    private String virtualMapFolder;
    private HeightMap heightMap;
    private WatertMap[] watertMaps;
    private int[][] watermapsData;
    private static final byte TYPE_HEIGHTMAP = 1;
    private static final byte TYPE_WATERMAP = 2;

    public VirtualMap(Map map) {
        this.map = map;
        VMapConfig config = (VMapConfig) ((Config) Container.getInstance(Service.CONFIG)).get("vmap");
        this.virtualMapFolder = config.virtualMapFolder;
    }

    public boolean isInWater(Unit unit) {
        if (watermapsData.length > 0 && watermapsData[0].length > 0) {
            float x = unit.pos_x;
            float y = unit.pos_z;
            int posX = (int) Math.floor(watermapsData.length * x / map.getWidth());
            int posY = (int) Math.floor(watermapsData[posX].length * y / map.getHeight());
            int mask = watermapsData[posX][posY];
            if (mask == 0) {
                return false;
            }
            // Il y a au moins une watermap avec des données concernant ce point
            return this.isPointInWater(posX, posY, unit.pos_y, mask);
        }

        return false;
    }

    public void loadMapData() {
        try {
            String filename = String.format(FILE_FORMAT, map.mapId);
            DataInputStream fileData = new DataInputStream(new BufferedInputStream(new FileInputStream(new File(this.virtualMapFolder + filename))));
            short version = fileData.readShort();
            byte mapId = fileData.readByte();
            if (mapId != map.mapId) {
                Log.error("VirtualMap: L'identifiant du fichier ne correspond pas avec l'identifiant de la carte chargée");
                return;
            }
            // Le contenu du fichier
            if (fileData.available() <= 0) {
                Log.error(String.format("VirtualMap: Le fichier %s ne contient aucune donnée", filename));
                return;
            }
            byte type;
            int length;
            List<WatertMap> wms = new ArrayList<>();
            while (fileData.available() > 0) {
                type = fileData.readByte();
                if (type == TYPE_HEIGHTMAP) {
                    int width = fileData.readInt();
                    int height = fileData.readInt();
                    length = fileData.readInt();
                    int[] data = new int[length];
                    for (int i = 0; i < length; i++) {
                        data[i] = fileData.readInt();
                    }
                    this.heightMap = new HeightMap();
                    this.heightMap.width = width;
                    this.heightMap.height = height;
                    this.heightMap.length = length;
                    this.heightMap.data = data;
                    Log.debug(String.format("VirtualMap: load heightmap complete (%d)", data.length));
                } else if (type == TYPE_WATERMAP) {
                    int width = fileData.readInt();
                    int height = fileData.readInt();
                    length = fileData.readInt();
                    float waterlevel = fileData.readFloat();
                    float depth = fileData.readFloat();
                    int[] data = new int[length];
                    for (int i = 0; i < length; i++) {
                        data[i] = fileData.readInt();
                    }
                    WatertMap wm = new WatertMap();
                    wm.width = width;
                    wm.height = height;
                    wm.waterlevel = waterlevel;
                    wm.depth = depth;
                    wm.length = length;
                    wm.data = data;
                    wms.add(wm);
                }
            }
            watertMaps = wms.toArray(new WatertMap[0]);
            // Création des masks pour les watermaps
            this.initializeWaterMaps();
        } catch (IOException e) {
            Log.error(String.format("VirtualMap load data in error for map %d : %s", map.mapId, e.getMessage()));
        }
    }

    private void initializeWaterMaps() {
        // On part du principe que toutes les watermaps ont la même longeur et largeur
        if (!(watertMaps.length > 0)) {
            return;
        }
        int width = watertMaps[0].width;
        int height = watertMaps[0].height;
        watermapsData = new int[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int mask = getMaskForWatermaps(x, y, width);
                watermapsData[x][y] = mask;
            }
        }
    }

    /**
     * Créé un mask permettant de savoir si une watermap possède des informations sur la position
     * @param x position X de l'image
     * @param y position Y de l'image
     * @param width Largeur de l'image
     * @return le mask
     */
    private int getMaskForWatermaps(int x, int y, int width) {
        int index = width * y + x;
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < watertMaps.length; i++) {
            if (watertMaps[i].data[index] > 0) {
                b.insert(0, 1);
            } else {
                b.insert(0, 0);
            }
        }
        return Integer.valueOf(b.toString(), 2);
    }

    /**
     * Retourne vrai si le point est présent dans une zone d'eau
     * @param posX Position X sur la watermap
     * @param posY Position Y sur la watermap
     * @param z Position Y de l'unité dans le monde
     * @param mask Le masque des watermaps
     * @return Vrai si le point est dans l'eau, faux sinon
     */
    private boolean isPointInWater(int posX, int posY, float z, int mask) {
        for (int k = 0; k < watertMaps.length; k++) {
            int localMask =  1 << k;
            int masked_n = mask & localMask;
            int thebit = masked_n >> k;
            if (thebit > 0) {
                // On regarde là hauteur
                int index = posY * watertMaps[k].width + posX;
                int grayscale = watertMaps[k].data[index];
                float depth = watertMaps[k].depth;
                float waterlevel = watertMaps[k].waterlevel;
                float pointDepth = (grayscale / 255f) * depth;
                if(!(z > waterlevel || z < waterlevel - pointDepth)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static class WatertMap {
        public int width, height, length;
        public float waterlevel, depth;
        public int[] data;
    }

    public static class HeightMap {
        public int width, height, length;
        public int[] data;
    }

}
