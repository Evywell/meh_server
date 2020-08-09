package fr.evywell.robgame.game.handlers;

import fr.evywell.common.container.Container;
import fr.evywell.common.network.Packet;
import fr.evywell.common.network.Session;
import fr.evywell.common.opcode.Handler;
import fr.evywell.robgame.Service;
import fr.evywell.robgame.game.entities.ObjectGuid;
import fr.evywell.robgame.game.entities.Player;
import fr.evywell.robgame.game.entities.Unit;
import fr.evywell.robgame.game.spell.*;
import fr.evywell.robgame.network.WorldSession;
import fr.evywell.robgame.world.World;

public class CastSpellHandler implements Handler {

    private SpellManager spellManager;
    private World world;

    public CastSpellHandler() {
        spellManager = (SpellManager) Container.getInstance(Service.SPELL_MANAGER);
        world = (World) Container.getInstance(Service.WORLD);
    }

    public void call(WorldSession session, SpellPayload spellPayload) {
        SpellInfo spellInfo = spellManager.getSpellInfo(spellPayload.spellId);
        if (spellInfo == null) {
            return;
        }

        SpellTarget target = new SpellTarget();
        target.targetGuid = spellPayload.target;

        Spell spell = new Spell(session.getPlayer(), spellInfo);
        spell.prepare(target);
    }

    @Override
    public void call(Session session, Object payload, Packet packet) {
        call((WorldSession) session, (SpellPayload)payload);
    }

    @Override
    public Object getPayload(Packet packet) {
        SpellPayload spell = new SpellPayload();
        spell.spellId = packet.readInt();
        spell.target = ObjectGuid.createFromGuid(packet.readLong());
        return spell;
    }
}
