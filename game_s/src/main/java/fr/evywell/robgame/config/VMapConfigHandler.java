package fr.evywell.robgame.config;

import fr.evywell.common.config.ConfigHandler;
import org.json.simple.JSONObject;

import java.io.IOException;

public class VMapConfigHandler implements ConfigHandler {
    @Override
    public String getName() {
        return "vmap";
    }

    @Override
    public String getRootName() {
        return null;
    }

    @Override
    public Object handle(JSONObject jsonContent) throws IOException {
        VMapConfig vMapConfig = new VMapConfig();
        vMapConfig.virtualMapFolder = (String) jsonContent.get("virtualMapFolder");
        return vMapConfig;
    }
}
