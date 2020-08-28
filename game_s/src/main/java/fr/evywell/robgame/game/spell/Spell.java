package fr.evywell.robgame.game.spell;

import fr.evywell.common.logger.Log;
import fr.evywell.robgame.game.entities.Unit;

public class Spell {

    private SpellState spellState;
    private Unit caster;
    private Unit target;
    private SpellInfo spellInfo;
    private SpellTarget targets;
    private int castTimer;

    // TEMP
    private int totalDamages;

    public static final int
        SPELL_CAST_TARGET_SINGLE = 0x01,
        SPELL_CAST_TARGET_AREA = 0x02;

    public Spell(Unit caster, SpellInfo info) {
        this.caster = caster;
        this.spellInfo = info;
        spellState = SpellState.SPELL_STATE_NONE;
        castTimer = 0;
        totalDamages = 0;
    }

    public void prepare(SpellTarget target) {
        // TODO: Vérifier la capacité du lanceur
        // Initialisation des targets
        initTargets(target);

        spellState = SpellState.SPELL_STATE_PREPARING;

        SpellEvent spellEvent = new SpellEvent(this);
        caster.getEventManager().addEvent(spellEvent, caster.getEventManager().calculateTime(1));
        // TODO: Est-ce que le sort est désactivé ?
        // TODO: Load des scripts
        castTimer = spellInfo.castTime;
    }

    public void update(int delta) {
        // Vérification des mouvement du joueur
        // TODO
        // En fonction du statut du sort, on fait des trucs
        switch (spellState) {
            case SPELL_STATE_PREPARING:
                // Le mec est toujours en train de cast
                if (castTimer > 0) {
                    if (delta >= castTimer) {
                        castTimer = 0;
                    } else {
                        castTimer -= delta;
                    }
                }
                if (castTimer == 0) {
                    Log.debug("Boom"); // On va remplacer ça hein ;)
                    this.applyEffects();
                    spellState = SpellState.SPELL_STATE_DONE;
                    // TODO: Il est temps de lancer le sort
                }
                break;
        }
    }

    public void effectInstantKill() {
        if (caster == null || target == null) {
            return;
        }
        caster.dealDamage(target, target.getHealth());
    }

    public void effectSchoolDamage(int schoolMask, int damage) {
        if (caster == null || target == null) {
            return;
        }
        int finalDamage = caster.dealDamage(target, damage);
        totalDamages += finalDamage;
    }

    public SpellState getSpellState() {
        return spellState;
    }

    private void initTargets(SpellTarget target) {
        int targetMask = 0;
        // Pour chaque effet, on ajoute le targetMask
        // Le scénario type est un spell qui a deux effets: le premier cible une personne et le second est une AOE
        for (EffectInfo effectInfo : spellInfo.getEffects()) {
            targetMask |= effectInfo.targetMask;
        }
        target.targetMask = targetMask;
        if ((targetMask & SPELL_CAST_TARGET_SINGLE) != 0) {
            target.mainTarget = caster.getMap().findGameObject(target.targetGuid);
        }
        // TODO: Ajouter le cas de l'AOE en sauvegardant l'origine de l'AOE
    }

    private void applyEffects() {
        for (EffectInfo effectInfo : spellInfo.getEffects()) {
            Effect effect = new Effect(this, effectInfo);
            effect.apply();
        }
        // On envoie le log
        
    }

    public static class Effect {
        public static final int
            APPLY_AURA = 1,
            SCHOOL_DAMAGE = 2,
            DECREASE_MOVEMENT_SPEED_PERCENT = 3,
            INSTANT_KILL = 4;

        private Spell spell;
        private EffectInfo effectInfo;

        public Effect(Spell spell, EffectInfo effectInfo) {
            this.spell = spell;
            this.effectInfo = effectInfo;
        }

        public void apply() {
            switch (effectInfo.ID) {
                case Effect.SCHOOL_DAMAGE:
                    int schoolMask = effectInfo.value1;
                    int value = effectInfo.value2;
                    spell.effectSchoolDamage(schoolMask, value);
                    break;
            }
        }

    }

    public enum SpellState { SPELL_STATE_NONE, SPELL_STATE_PREPARING, SPELL_STATE_DONE }

}

