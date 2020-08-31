package fr.evywell.robgame.game.entities;

import fr.evywell.common.event.EventManager;
import fr.evywell.common.logger.Log;
import fr.evywell.common.network.Packet;
import fr.evywell.robgame.game.map.grid.Grid;
import fr.evywell.robgame.game.map.grid.notifier.CombatLogVisitor;
import fr.evywell.robgame.game.spell.Spell;
import fr.evywell.robgame.game.spell.SpellInfo;
import fr.evywell.robgame.game.spell.SpellTarget;
import fr.evywell.robgame.network.combat.CombatLogPacket;
import fr.evywell.robgame.network.combat.HealthUpdatePacket;
import fr.evywell.robgame.network.combat.KillPacket;

public class Unit extends GameObject {

    private final EventManager eventManager;
    protected int health = 2000; // TODO: Retirer ça
    protected float speed = 1.0f;
    public String name;

    public Unit() {
        super();
        eventManager = new EventManager();
    }

    @Override
    public void update(int delta) {
        super.update(delta);
        eventManager.update(delta); // Le système de Spell
    }

    public float getSpeed() {
        return speed; // TODO: Changer cette façon de récuperer la statistique (un système avec des flags, etc.)
    }

    public void castSpell(SpellInfo spellInfo, SpellTarget target) {
        Spell spell = new Spell(this, spellInfo);
        spell.prepare(target);
    }

    public int dealDamage(Unit victim, int damages) {
        victim.setHealth(victim.getHealth() - damages);

        if (victim.getHealth() <= 0) {
            kill(victim);
        } else {
            HealthUpdatePacket packet = new HealthUpdatePacket(victim.guid, victim.getHealth());
            sendPacketToSet(packet, null);
        }

        return damages;
    }

    public void kill(Unit victim) {
        // Le système de loot, etc.
        Player player = getPlayer(); // Le joueur qui a tué

        // TODO: Envoi du packet
        sendPacketToSet(new KillPacket(this, victim), null);
    }

    public boolean updatePosition(float posX, float posY, float posZ, float orientation) {
        if (!Grid.isValidPosition(posX, posY, posZ, orientation)) {
            Log.error(String.format("Unit::updatePosition Invalid positions (%f;%f;%f)", posX, posY, posZ));
            return false;
        }
        // TODO: Regarder si la position actuelle est vraiment différente de la nouvelle (avec la marge)
        this.pos_x = posX;
        this.pos_y = posY;
        this.pos_z = posZ;
        this.orientation = orientation;
        this.map.moveGameObjectInMap(this);
        return true;
    }

    public void sendDamageLog(Spell spell, int damages) {
        // Construction du packet
        CombatLogPacket combatLogPacket = new CombatLogPacket();
        combatLogPacket.source = spell.getCaster().guid;
        combatLogPacket.target = spell.getTargets().targetGuid;
        combatLogPacket.damages = damages;
        sendCombatLog(combatLogPacket);
    }

    public void sendCombatLog(CombatLogPacket packet) {
        // On envoie le tout par l'intermédiaire d'un visitor
        CombatLogVisitor visitor = new CombatLogVisitor(packet);
        this.cell.visitPlayersWithNeighboring(visitor);
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getHealth() {
        return health;
    }

    @Override
    public int getType() {
        return GameObjectType.UNIT;
    }

    public Player getPlayer() {
        if (this instanceof Player) {
            return (Player) this;
        }
        return null;
    }

    @Override
    public void putIntoPacket(Packet src) {
        super.putIntoPacket(src);
        src.putString(name);
    }

    public EventManager getEventManager() {
        return eventManager;
    }

}
