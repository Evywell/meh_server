package fr.evywell.robgame.game.gameobject;

import fr.evywell.common.logger.Log;
import fr.evywell.common.timer.IntervalTimer;

public class Creature extends Unit {

    private IntervalTimer timer;

    public String name;

    public Creature() {
        this.timer = new IntervalTimer(1000);
    }

    @Override
    public void update(int delta) {
        super.update(delta);
        timer.update(delta);

        if (timer.passed()) {
            // Notifier la cellule
            //this.move(2.5f, 0, 1);
           // Log.info(String.format("Position de la créature %s:%s", pos_x, pos_z));
            timer.reset();
        }
    }

    public static class Template {
        public String uuid, name;
    }
}
