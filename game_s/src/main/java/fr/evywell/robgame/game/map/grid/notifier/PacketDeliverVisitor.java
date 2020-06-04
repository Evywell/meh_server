package fr.evywell.robgame.game.map.grid.notifier;

import fr.evywell.common.network.Packet;
import fr.evywell.robgame.game.gameobject.GameObject;
import fr.evywell.robgame.game.gameobject.Player;

public class PacketDeliverVisitor extends AbstractDeliverVisitor {

    private final Player skipPlayer;

    public PacketDeliverVisitor(GameObject source, Packet packet, Player skipPlayer) {
        super(source, packet);
        this.skipPlayer = skipPlayer;
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
