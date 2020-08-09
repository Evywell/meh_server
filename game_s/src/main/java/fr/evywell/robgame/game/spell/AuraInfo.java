package fr.evywell.robgame.game.spell;

public class AuraInfo {

    public int ID;
    public String name;
    public String description;
    public int flags;
    public EffectInfo[] effects;

    public void setEffects(EffectInfo[] effects) {
        this.effects = effects;
    }
}
