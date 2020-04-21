package fr.evywell.robgame.game.gameobject;

public class Creature extends Unit {

    public String name;

    public static class Template {
        public String uuid, name;
    }

    @Override
    public void update(int delta) {
        super.update(delta);
    }
}
