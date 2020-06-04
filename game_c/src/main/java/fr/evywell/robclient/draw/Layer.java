package fr.evywell.robclient.draw;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Layer extends JComponent {

    private final int width;
    private final int height;
    private List<Drawable> drawables;

    public Layer(int width, int height) {
        this.width = width;
        this.height = height;
        setPreferredSize(new Dimension(width, height));
        this.drawables = new ArrayList<>();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        for (Drawable drawable : drawables) {
            drawable.draw(g2D);
        }
    }

    public void addDrawable(Drawable drawable) {
        this.drawables.add(drawable);
    }
}
