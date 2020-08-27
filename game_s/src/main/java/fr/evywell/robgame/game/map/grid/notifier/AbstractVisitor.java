package fr.evywell.robgame.game.map.grid.notifier;

import fr.evywell.robgame.game.entities.GameObject;

public abstract class AbstractVisitor {

    public abstract void visit(GameObject go);

}
