package fr.evywell.robgame.game.spell;

public class SpellInfo {

    public int ID;
    public String name;
    public String description;
    public int cost;
    public byte resourceType;
    public int cooldown;
    public int range;
    public int castTime;
    public int gcd;
    public int gcdCategory;
    public float duration;
    public byte school;
    public int flags;

    private EffectInfo[] effects;

    public SpellInfo() {}

    public SpellInfo(EffectInfo[] effects) {
        this.effects = effects;
    }

    public void setEffects(EffectInfo[] effects) {
        this.effects = effects;
    }

    public EffectInfo[] getEffects() {
        return effects;
    }
}
