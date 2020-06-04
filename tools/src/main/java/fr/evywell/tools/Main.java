package fr.evywell.tools;

import fr.evywell.tools.map_generator.MapGenerator;

import java.io.File;

class Main {

    public static void main(String[] args) {
        String projectPath = System.getProperty("user.dir");
        String resourcesPath = projectPath + File.separator + "resources" + File.separator;

        MapGenerator mapGenerator = new MapGenerator();
        mapGenerator
            .setOutputDir("/Users/evywell/Documents/rob_server/game_s/resources/vmaps/")
            .setFileName("1.vmap")
            .setVersion((short) 33)
            .setMapId((byte) 1)
            .addHeightMap(resourcesPath + "1_heightmap.png")
            .generate();
    }

}