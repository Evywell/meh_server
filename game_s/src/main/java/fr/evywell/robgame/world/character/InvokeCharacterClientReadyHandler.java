package fr.evywell.robgame.world.character;

import fr.evywell.common.container.Container;
import fr.evywell.common.network.Packet;
import fr.evywell.common.network.Session;
import fr.evywell.common.opcode.EmptyTram;
import fr.evywell.common.opcode.Handler;
import fr.evywell.robgame.network.WorldSession;
import fr.evywell.robgame.opcode.Opcode;
import fr.evywell.robgame.world.World;

public class InvokeCharacterClientReadyHandler implements Handler {

    private World world;

    public InvokeCharacterClientReadyHandler() {
        this.world = (World) Container.getInstance(fr.evywell.robgame.Service.WORLD);
    }

    @Override
    public void call(Session session, Object payload, Packet packet) {
        this.world.spawnPlayerInWorld((WorldSession) session);
        Packet pck = new Packet();
        pck.setCmd(Opcode.SMSG_INVOKE_CHARACTER_CLIENT_READY);
        session.send(pck);
    }

    @Override
    public Class getPayloadTemplate() {
        return EmptyTram.class;
    }
}
