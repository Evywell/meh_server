package fr.evywell.robgame.game.map.grid.notifier;

import fr.evywell.robgame.game.entities.GameObject;
import fr.evywell.robgame.game.entities.Player;

public class VisibilityChangesVisitor extends AbstractVisitor {

    private GameObject go;

    public VisibilityChangesVisitor(GameObject go) {
        this.go = go;
    }

    @Override
    public void visit(GameObject go) {
        if (this.go.equals(go)) {
            return;
        }
        if (!(go instanceof Player)) {
            return;
        }
        ((Player) go).updateVisibilityOf(this.go);
    }

}
