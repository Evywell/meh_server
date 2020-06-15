package fr.evywell.tools.wmo;

import fr.evywell.common.maths.Vector3;

import java.util.ArrayList;
import java.util.List;

public class WMOBuilder {

    private List<WorldGameObject> worldGameObjectList = new ArrayList<>();

    public WMOBuilder addBox(Vector3 halfExtended, Vector3 position) {
        worldGameObjectList.add(new BoxObject(halfExtended, position));
        return this;
    }

    public List<WorldGameObject> getWorldGameObjectList() {
        return worldGameObjectList;
    }
}
