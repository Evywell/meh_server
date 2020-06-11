package fr.evywell.robgame.physics.debug.draw;

import java.awt.*;

public class FilledRectangle implements Drawable {
    @Override
    public void draw(Graphics2D g) {
        g.fillRect(8, 8, 50, 50);
    }
}
