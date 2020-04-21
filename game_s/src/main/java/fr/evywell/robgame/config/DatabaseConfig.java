package fr.evywell.robgame.config;

import org.json.simple.JSONObject;

import java.io.IOException;

public class DatabaseConfig {

    public Database world;
    public Database auth;
    public Database characters;

    public void defineDatabase(String db, JSONObject jsonContent) throws IOException {
        Database database = new Database();
        database.host = (String) jsonContent.get("host");
        database.port = (Long) jsonContent.get("port");
        database.user = (String) jsonContent.get("user");
        database.password = (String) jsonContent.get("password");
        database.database = (String) jsonContent.get("database");
        switch (db) {
            case "world":
                this.world = database;
                break;
            case "auth":
                this.auth = database;
                break;
            case "characters":
                this.characters = database;
                break;
        }
    }

    public class Database {

        public String host;
        public Long port;
        public String user;
        public String password;
        public String database;

    }
}

