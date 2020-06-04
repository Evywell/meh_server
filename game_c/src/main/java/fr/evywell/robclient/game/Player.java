package fr.evywell.robclient.game;

import fr.evywell.common.logger.Log;
import fr.evywell.robclient.draw.Drawable;

import java.awt.*;

public class Player implements Drawable {

    public String uuid, name;
    public int mapId;
    public float pos_x, pos_y, pos_z;

    @Override
    public void draw(Graphics2D g) {
        Color originalColor = g.getColor();
        Log.debug("Pos x=" + pos_x + " Pos z=" + pos_z);
        g.setColor(Color.BLUE);
        int pos_x = Math.round(Map.MULTIPLIER * this.pos_x), pos_z = Math.round(Map.MULTIPLIER * this.pos_z);
        Log.info(String.format("Draw in %s;%s", pos_x, pos_z));
        g.drawOval(pos_x, pos_z, 20, 20);
        g.fillOval(pos_x, pos_z, 20, 20);
        g.setColor(originalColor);
    }
}
