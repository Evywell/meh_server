package fr.evywell.robgame.game.spell;

import fr.evywell.robgame.game.entities.Player;
import fr.evywell.robgame.game.entities.Unit;

public class Spell {

    private Unit caster;
    private Unit target;
    private SpellInfo spellInfo;

    public Spell(Unit caster, SpellInfo info) {
        this.caster = caster;
        this.spellInfo = info;
    }

    public void prepare(SpellTarget target) {
        
    }

    public void effectInstantKill() {
        if (caster == null || target == null) {
            return;
        }
        caster.dealDamage(target, target.getHealth());
    }

    public static class Effect {
        public static final int
            APPLY_AURA = 1,
            SCHOOL_DAMAGE = 2,
            DECREASE_MOVEMENT_SPEED_PERCENT = 3,
            INSTANT_KILL = 4;
    }

}

