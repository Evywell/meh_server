package fr.evywell.common.container;

import java.util.HashMap;
import java.util.Map;

public class Container {
    private Map<Integer, Object> map;
    private static Container _instance;

    private Container() {
        this.map = new HashMap<>();
    }

    public static void setInstance(int key, Object obj) {
        getInstance().map.put(key, obj);
    }

    public static Object getInstance(int key) {
        return getInstance().map.get(key);
    }

    private static Container getInstance() {
        if (_instance == null) {
            _instance = new Container();
        }

        return _instance;
    }
}
