package fr.evywell.robgame.physics;

import com.bulletphysics.linearmath.DebugDrawModes;
import com.bulletphysics.linearmath.IDebugDraw;
import fr.evywell.common.logger.Log;
import fr.evywell.robgame.physics.debug.display.Window;
import fr.evywell.robgame.physics.debug.draw.Circle;
import fr.evywell.robgame.physics.debug.draw.Layer;
import fr.evywell.robgame.physics.debug.draw.Line;
import fr.evywell.robgame.physics.debug.draw.Point;

import javax.swing.*;
import javax.vecmath.Vector3f;

public class DrawDebug extends IDebugDraw {

    private Layer layer;
    private Window window;

    public DrawDebug(Window window) {
        JComponent panel = window.getPanel();
        layer = new Layer(window.getWidth(), window.getHeight());
        panel.add(layer);
        window.show();
        this.window = window;
    }

    public void repaint() {
        layer.repaint();
    }

    public void drawCircle(Vector3f center, float radius) {
        layer.addDrawable(new Circle(center, radius));
    }

    @Override
    public void drawLine(Vector3f from, Vector3f to, Vector3f color) {
        layer.addDrawable(new Line(from, to));
    }

    @Override
    public void drawContactPoint(Vector3f PointOnB, Vector3f normalOnB, float distance, int lifeTime, Vector3f color) {
        layer.addDrawable(new Point(PointOnB));
    }

    @Override
    public void reportErrorWarning(String warningString) {

    }

    @Override
    public void draw3dText(Vector3f location, String textString) {

    }

    @Override
    public void setDebugMode(int debugMode) {

    }

    @Override
    public int getDebugMode() {
        return DebugDrawModes.DRAW_WIREFRAME;
    }
}
