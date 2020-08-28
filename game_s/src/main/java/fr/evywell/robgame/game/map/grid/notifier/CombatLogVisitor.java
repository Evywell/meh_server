package fr.evywell.robgame.game.map.grid.notifier;

import fr.evywell.robgame.game.entities.GameObject;
import fr.evywell.robgame.game.entities.Player;
import fr.evywell.robgame.network.combat.CombatLogPacket;

public class CombatLogVisitor extends AbstractVisitor {

    private CombatLogPacket packet;

    public CombatLogVisitor(CombatLogPacket packet) {
        this.packet = packet;
    }

    @Override
    public void visit(GameObject go) {
        // On ne peut envoyer les logs de combat qu'aux joueurs
        if (!(go instanceof Player)) {
            return;
        }
        ((Player) go).sendPacket(packet);
    }

}
