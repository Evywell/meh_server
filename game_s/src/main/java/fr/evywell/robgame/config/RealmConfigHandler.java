package fr.evywell.robgame.config;

import fr.evywell.common.config.ConfigHandler;
import org.json.simple.JSONObject;

import java.io.IOException;

public class RealmConfigHandler implements ConfigHandler {
    @Override
    public String getName() {
        return "realm";
    }

    @Override
    public String getRootName() {
        return null;
    }

    @Override
    public Object handle(JSONObject jsonContent) throws IOException {
        RealmConfig realmConfig = new RealmConfig();
        realmConfig.realmId = ((Long) jsonContent.get("realmId")).intValue();
        realmConfig.realmName = (String) jsonContent.get("realmName");
        realmConfig.ip = (String) jsonContent.get("ip");
        realmConfig.port = ((Long) jsonContent.get("port")).intValue();
        return realmConfig;
    }
}
