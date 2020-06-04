package fr.evywell.sandbox;

import fr.evywell.common.config.Config;
import fr.evywell.common.container.Container;
import fr.evywell.common.logger.Log;
import fr.evywell.robgame.config.VMapConfigHandler;
import fr.evywell.robgame.game.gameobject.Unit;
import fr.evywell.robgame.game.map.Map;
import fr.evywell.robgame.game.map.VirtualMap;
import fr.evywell.tools.map_generator.MapGenerator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        String projectPath = System.getProperty("user.dir");
        String resourcesPath = projectPath + File.separator + "sandbox" + File.separator + "resources" + File.separator;
        Config config = new Config(resourcesPath + "config.json");
        config.configHandler(new VMapConfigHandler());
        Container.setInstance(fr.evywell.robgame.Service.CONFIG, config);

        MapGenerator mapGenerator = new MapGenerator();
        mapGenerator
                .setOutputDir(resourcesPath + "/vmaps/")
                .setFileName("1_vmap.bin")
                .setVersion((short) 1)
                .setMapId((byte) 1)
                .addWaterMap(resourcesPath + "watermaps/1_4.png", 50, 20)
                .addWaterMap(resourcesPath + "watermaps/1_1.png", 50, 20)
                .generate();

        Unit u = new Unit();
        u.pos_x = 85;
        u.pos_y = 45.9f;
        u.pos_z = 34;

        Map map = new Map(1, 0, 10, 1000, 1000);
        VirtualMap vm = new VirtualMap(map);
        vm.loadMapData();
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(new File(resourcesPath + "a.txt")));

            for (int i = 0; i < 1000; i ++) {
                for (int j = 0; j < 1000; j ++) {
                    u.pos_x = i;
                    u.pos_z = j;
                    writer.write(String.format("%s \n", vm.isInWater(u) ? "oui": "non"));
                }
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}