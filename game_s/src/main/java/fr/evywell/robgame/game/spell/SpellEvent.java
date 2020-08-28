package fr.evywell.robgame.game.spell;

import fr.evywell.common.event.Event;

public class SpellEvent extends Event {

    private Spell spell;

    public SpellEvent(Spell spell) {
        this.spell = spell;
    }

    @Override
    public boolean execute(int localTime, int internalTime) {
        spell.update(localTime);
        return spell.getSpellState() == Spell.SpellState.SPELL_STATE_DONE;
    }
}
