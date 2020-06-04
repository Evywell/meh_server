package fr.evywell.robgame.game.gameobject;

import fr.evywell.common.logger.Log;
import fr.evywell.robgame.game.map.grid.Grid;

public class Unit extends GameObject {

    protected float speed = 1.0f;

    public float getSpeed() {
        return speed; // TODO: Changer cette façon de récuperer la statistique (un système avec des flags, etc.)
    }

    public boolean updatePosition(float posX, float posY, float posZ, float orientation) {
        if (!Grid.isValidPosition(posX, posY, posZ, orientation)) {
            Log.error(String.format("Unit::updatePosition Invalid positions (%f;%f;%f)", posX, posY, posZ));
            return false;
        }
        // TODO: Regarder si la position actuelle est vraiment différente de la nouvelle (avec la marge)
        this.pos_x = posX;
        this.pos_y = posY;
        this.pos_z = posZ;
        this.orientation = orientation;
        this.map.moveGameObjectInMap(this);
        return true;
    }
}
