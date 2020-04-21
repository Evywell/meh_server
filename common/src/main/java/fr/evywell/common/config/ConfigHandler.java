package fr.evywell.common.config;

import org.json.simple.JSONObject;

import java.io.IOException;

public interface ConfigHandler {

    String getName();
    String getRootName();
    Object handle(JSONObject jsonContent) throws IOException;

}
