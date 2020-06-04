package fr.evywell.robgame.game.map.grid.notifier;

import fr.evywell.common.network.Packet;
import fr.evywell.robgame.game.gameobject.GameObject;
import fr.evywell.robgame.game.gameobject.Player;

public abstract class AbstractDeliverVisitor {

    protected final GameObject source;
    protected final Packet packet;

    public AbstractDeliverVisitor(GameObject source, Packet packet) {
        this.source = source;
        this.packet = packet;
    }

    public abstract void visit(GameObject go);

    public void sendPacket(Player target) {
        // On ne peut pas envoyer un packet à la source
        if (source.equals(target)) {
            return;
        }
        target.sendPacket(packet);
    }

}
