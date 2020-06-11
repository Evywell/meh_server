package fr.evywell.robgame.physics.debug.draw;

import javax.vecmath.Vector3f;
import java.awt.*;

public class Point implements Drawable {

    private Vector3f point;

    public Point(Vector3f point) {
        this.point = point;
    }

    @Override
    public void draw(Graphics2D g) {
        Color c = g.getColor();
        g.setColor(Color.RED);
        g.drawOval((int) Math.ceil(point.x) * 10, (int) Math.ceil(point.z) * 10, 2, 2);
        g.setColor(c);
    }
}
