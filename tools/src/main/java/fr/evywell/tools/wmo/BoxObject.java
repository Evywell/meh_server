package fr.evywell.tools.wmo;

import fr.evywell.common.maths.Vector3;

public class BoxObject extends WorldGameObject {

    private Vector3 halfExtended, position;

    public BoxObject(Vector3 halfExtended, Vector3 position) {
        this.halfExtended = halfExtended;
        this.position = position;
    }

    public Vector3 getHalfExtended() {
        return halfExtended;
    }

    public Vector3 getPosition() {
        return position;
    }
}
