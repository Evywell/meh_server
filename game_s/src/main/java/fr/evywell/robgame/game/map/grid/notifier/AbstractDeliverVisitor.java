package fr.evywell.robgame.game.map.grid.notifier;

import fr.evywell.common.network.Packet;
import fr.evywell.robgame.game.entities.GameObject;
import fr.evywell.robgame.game.entities.Player;

public abstract class AbstractDeliverVisitor extends AbstractVisitor {

    protected final GameObject source;
    protected final Packet packet;

    public AbstractDeliverVisitor(GameObject source, Packet packet) {
        this.source = source;
        this.packet = packet;
    }

    public void sendPacket(Player target) {
        // On ne peut pas envoyer un packet à la source
        if (source.equals(target)) {
            return;
        }
        target.sendPacket(packet);
    }

}
