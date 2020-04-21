package fr.evywell.robauth.config;

import fr.evywell.common.config.ConfigHandler;
import org.json.simple.JSONObject;

import java.io.IOException;

public class AuthConfigHandler implements ConfigHandler {
    @Override
    public String getName() {
        return "auth";
    }

    @Override
    public String getRootName() {
        return null;
    }

    @Override
    public Object handle(JSONObject jsonContent) throws IOException {
        AuthConfig authConfig = new AuthConfig();
        authConfig.port = ((Long) jsonContent.get("port")).intValue();
        return authConfig;
    }

    public static class AuthConfig {
        public int port;
    }
}
