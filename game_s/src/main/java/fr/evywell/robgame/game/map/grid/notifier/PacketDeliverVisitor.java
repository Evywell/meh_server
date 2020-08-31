package fr.evywell.robgame.game.map.grid.notifier;

import fr.evywell.common.network.Packet;
import fr.evywell.robgame.game.entities.GameObject;
import fr.evywell.robgame.game.entities.Player;

public class PacketDeliverVisitor extends AbstractDeliverVisitor {

    private final Player skipPlayer;

    public PacketDeliverVisitor(GameObject source, Packet packet, Player skipPlayer, boolean skipSource) {
        super(source, packet, skipSource);
        this.skipPlayer = skipPlayer;
    }

    public PacketDeliverVisitor(GameObject source, Packet packet, Player skipPlayer) {
        this(source, packet, skipPlayer, true);
    }

    @Override
    public void visit(GameObject go) {
        Player player = (Player) go;
        if (player.equals(skipPlayer)) {
            return;
        }
        // On peut check si le joueur peut voir le gameobject (camouflé par exemple, ou une faction différente)
        sendPacket(player);
    }
}
