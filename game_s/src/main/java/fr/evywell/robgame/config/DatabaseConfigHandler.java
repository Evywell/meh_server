package fr.evywell.robgame.config;

import fr.evywell.common.config.ConfigHandler;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.Map;

public class DatabaseConfigHandler implements ConfigHandler {

    @Override
    public String getName() {
        return "database";
    }

    @Override
    public String getRootName() {
        return "databases";
    }

    @Override
    public Object handle(JSONObject jsonContent) {
        DatabaseConfig databaseConfig = new DatabaseConfig();
        ((Map<String, JSONObject>) jsonContent).forEach((key, value) -> {
            try {
                databaseConfig.defineDatabase(key, value);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return databaseConfig;
    }
}
