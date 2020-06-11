package fr.evywell.robgame.physics.debug.draw;

import fr.evywell.common.logger.Log;

import javax.vecmath.Vector3f;
import java.awt.*;

public class Circle implements Drawable {

    private final Vector3f center;
    private final float radius;

    public Circle(Vector3f center, float radius) {
        this.center = center;
        this.radius = radius;
    }

    @Override
    public void draw(Graphics2D g) {
        int width = (int) Math.ceil(radius * 2) * 10;
        int x = (int) Math.ceil((center.x - radius) * 10);
        int y = (int) Math.ceil((center.z - radius) * 10);
        Log.debug(String.format("DRAWING CIRCLE %d:%d %d", x, y, width));
        g.drawOval(x, y, width, width);
    }

}
