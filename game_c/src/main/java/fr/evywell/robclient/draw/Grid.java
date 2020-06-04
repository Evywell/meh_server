package fr.evywell.robclient.draw;

import fr.evywell.robclient.game.Map;

import java.awt.*;

public class Grid implements Drawable {

    private final int width;
    private final int height;
    private final Color color;

    public Grid(int width, int height, Color color) {
        this.width = width;
        this.height = height;
        this.color = color;
    }

    @Override
    public void draw(Graphics2D g) {
        Color originalColor = g.getColor();
        g.setColor(color);
        for (int x = 0; x < width; x++) {
            g.drawLine(x * Map.MULTIPLIER, 0, x * Map.MULTIPLIER, height * Map.MULTIPLIER);
        }
        for (int y = 0; y < height; y++) {
            g.drawLine(0, y * Map.MULTIPLIER, width * Map.MULTIPLIER, y * Map.MULTIPLIER);
        }
        g.setColor(originalColor);
    }

}
