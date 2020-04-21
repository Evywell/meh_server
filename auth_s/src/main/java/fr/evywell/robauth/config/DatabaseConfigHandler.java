package fr.evywell.robauth.config;

import fr.evywell.common.config.ConfigHandler;
import org.json.simple.JSONObject;

import java.io.IOException;

public class DatabaseConfigHandler implements ConfigHandler {
    @Override
    public String getName() {
        return "database";
    }

    @Override
    public String getRootName() {
        return "database";
    }

    @Override
    public Object handle(JSONObject jsonContent) throws IOException {
        DatabaseConfig databaseConfig = new DatabaseConfig();
        databaseConfig.host = (String) jsonContent.get("host");
        databaseConfig.port = (Long) jsonContent.get("port");
        databaseConfig.user = (String) jsonContent.get("user");
        databaseConfig.password = (String) jsonContent.get("password");
        databaseConfig.database = (String) jsonContent.get("database");
        return databaseConfig;
    }

    public static class DatabaseConfig {
        public String host;
        public Long port;
        public String user;
        public String password;
        public String database;
    }
}
