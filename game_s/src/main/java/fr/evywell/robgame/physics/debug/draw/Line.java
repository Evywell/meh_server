package fr.evywell.robgame.physics.debug.draw;

import fr.evywell.common.logger.Log;
import fr.evywell.common.maths.Vector3;

import javax.vecmath.Vector3f;
import java.awt.*;

public class Line implements Drawable {

    private final Vector3f from;
    private final Vector3f to;

    public Line(Vector3f from, Vector3f to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public void draw(Graphics2D g) {
        Color c = g.getColor();
        g.setColor(Color.RED);
        Log.debug(String.format("Drawing line %d:%d %d:%d", (int) Math.ceil(from.x) * 10, (int) Math.ceil(from.z) * 10, (int) Math.ceil(to.x) * 10, (int) Math.ceil(to.z) * 10));
        g.drawLine((int) Math.ceil(from.x) * 10, (int) Math.ceil(from.z) * 10, (int) Math.ceil(to.x) * 10, (int) Math.ceil(to.z) * 10);
        //g.drawLine(50, 50, 100, 100);
        g.setColor(c);
    }
}
