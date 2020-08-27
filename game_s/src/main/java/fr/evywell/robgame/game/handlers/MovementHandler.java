package fr.evywell.robgame.game.handlers;

import fr.evywell.common.logger.Log;
import fr.evywell.common.network.Packet;
import fr.evywell.robgame.game.entities.Player;
import fr.evywell.robgame.game.movement.MovementInfo;
import fr.evywell.robgame.game.movement.MovementPayload;
import fr.evywell.robgame.game.movement.MovementUpdate;
import fr.evywell.robgame.network.WorldSession;
import fr.evywell.robgame.opcode.AbstractHandler;

public class MovementHandler extends AbstractHandler {

    public void call(WorldSession session, MovementPayload movement, Packet packet) {
        Player player = session.getPlayer();
        MovementInfo movementInfo = movement.toMovementInfo();
        movementInfo.guid = player.guid;
        // On vérifie si le mouvement est possible (il ne peut pas tourner à gauche et à droite en même temps, etc.)
        player.validateMovement(movementInfo);
        // On effectue le mouvement (màj de la position du joueur)
        if(!player.updatePosition(
            movementInfo.position.x,
            movementInfo.position.y,
            movementInfo.position.z,
            movementInfo.orientation)
        ) {
            return;
        }
        Log.debug("Mise à jour de la position: " + movementInfo);
        // On tue le joueur s'il est en dessous de la map (Y < 0 il me semble, mais on va mettre un offset dans le doute)
        if (player.pos_y < -2f) {
            // TODO: Tuer le joueur
        }
        // On notifie toutes les personnes aux alentours
        player.sendPacketToSet(new MovementUpdate(movementInfo), player);
    }

    @Override
    public void call(WorldSession session, Object payload, Packet packet) {
        call(session, (MovementPayload) payload, packet);
    }

    @Override
    public Object getPayload(Packet packet) {
        MovementPayload payload = new MovementPayload();
        payload.guid = packet.readInt(); // ça ne sert à rien car override dans le call()
        payload.posX = packet.readFloat();
        payload.posY = packet.readFloat();
        payload.posZ = packet.readFloat();
        payload.orientation = packet.readFloat();
        payload.flags = packet.readInt();
        return payload;
    }
}
