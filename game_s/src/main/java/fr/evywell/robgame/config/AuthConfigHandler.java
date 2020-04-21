package fr.evywell.robgame.config;

import fr.evywell.common.config.ConfigHandler;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.Map;

public class AuthConfigHandler implements ConfigHandler {
    @Override
    public String getName() {
        return "auth";
    }

    @Override
    public String getRootName() {
        return "authServer";
    }

    @Override
    public Object handle(JSONObject jsonContent) throws IOException {
        AuthConfig config = new AuthConfig();
        config.ip = (String) jsonContent.get("ip");
        config.port = ((Long) jsonContent.get("port")).intValue();
        config.clientId = (String) jsonContent.get("clientId");
        return config;
    }
}
