package fr.evywell.common.config;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Config {

    private final String configPath;
    private Map<String, ConfigHandler> handlers;
    private boolean parsed;
    private JSONObject parsedContent;

    public Config(String configPath) {
        this.configPath = configPath;
        this.handlers = new HashMap<>();
        this.parsed = false;
    }

    public Config configHandler(ConfigHandler handler) {
        this.parseFile();
        this.handlers.put(handler.getName(), handler);

        return this;
    }

    public Object get(String handlerName) {
        ConfigHandler handler = this.handlers.get(handlerName);
        try {
            if (handler.getRootName() == null) {
                // Il s'agit de toute la configuration
                return handler.handle(this.parsedContent);
            }

            return handler.handle((JSONObject) this.parsedContent.get(handler.getRootName()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void parseFile() {
        if (this.parsed) {
            return;
        }
        try {
            JSONParser parser = new JSONParser();
            Reader reader = new FileReader(this.configPath);
            this.parsedContent = (JSONObject) parser.parse(reader);
            this.parsed = true;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

}
