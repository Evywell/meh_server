package fr.evywell.tools.map_generator;

import fr.evywell.common.logger.Log;
import fr.evywell.tools.wmo.WMOBuilder;
import fr.evywell.tools.wmo.WorldGameObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MapGenerator {

    private String outputDir;
    private String fileName;
    private short version;
    private byte mapId;
    private List<Heightmap> heightmaps = new ArrayList<>();
    private List<Watermap> watermaps = new ArrayList<>();
    private List<WorldGameObject> worldGameObjects = new ArrayList<>();

    public MapGenerator setOutputDir(String outputDir) {
        this.outputDir = outputDir;
        return this;
    }

    public MapGenerator setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public MapGenerator setVersion(short version) {
        this.version = version;
        return this;
    }

    public MapGenerator setMapId(byte mapId) {
        this.mapId = mapId;
        return this;
    }

    public void generate() {
        try {
            OutputStream outputStream = new FileOutputStream(new File(this.outputDir + this.fileName));
            DataOutputStream dos = new DataOutputStream(outputStream);
            dos.writeShort(version);
            dos.writeByte(mapId);
            // Heightmaps
            for (Heightmap hm : heightmaps) {
                Log.info("Heightmap");
                dos.writeByte(1); // Type
                dos.writeInt(hm.width);
                dos.writeInt(hm.height);
                dos.writeInt(hm.length);
                for (int i = 0; i < hm.data.length; i++) {
                    dos.writeInt(hm.data[i]);
                }
            }
            //Watermaps
            for (Watermap wm : watermaps) {
                Log.info("Watermap");
                dos.writeByte(2); // Type
                dos.writeInt(wm.width);
                dos.writeInt(wm.height);
                dos.writeInt(wm.length);
                dos.writeFloat(wm.waterlevel);
                dos.writeFloat(wm.depth);
                for (int i = 0; i < wm.data.length; i++) {
                    dos.writeInt(wm.data[i]);
                }
            }
            // WMO (World Map Objects)
            for (WorldGameObject go : worldGameObjects) {
                Log.info("Gameobject");
                dos.writeByte(3); // Type

            }
            dos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public MapGenerator addWMO(WMOBuilder builder) {
        worldGameObjects = builder.getWorldGameObjectList();
        return this;
    }

    public MapGenerator addHeightMap(String filePath) {
        try {
            BufferedImage img = this.read(filePath);
            int[] data = this.getData(img);
            Heightmap hm = new Heightmap();
            hm.width = img.getWidth();
            hm.height = img.getHeight();
            hm.length = data.length;
            hm.data = data;
            heightmaps.add(hm);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    public MapGenerator addWaterMap(String filePath, float waterlevel, float depth) {
        try {
            BufferedImage img = this.read(filePath);
            int[] data = this.getData(img);
            Watermap wm = new Watermap();
            wm.width = img.getWidth();
            wm.height = img.getHeight();;
            wm.length = data.length;
            wm.waterlevel = waterlevel;
            wm.depth = depth;
            wm.data = data;
            watermaps.add(wm);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return this;
    }

    private int[] getData(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();
        int[] data = new int[width*height];
        int index = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int gray = img.getRGB(x, y) & 0xFF;
                data[index] = gray;
                index++;
            }
        }
        return data;
    }

    private BufferedImage read(String filePath) throws IOException {
        return ImageIO.read(new File(filePath));
    }

    private static class Heightmap {
        public int width, height, length;
        public int[] data;
    }

    private static class Watermap {
        public int width, height, length;
        public float waterlevel, depth;
        public int[] data;
    }
}
