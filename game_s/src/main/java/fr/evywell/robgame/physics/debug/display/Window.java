package fr.evywell.robgame.physics.debug.display;

import javax.swing.*;
import java.awt.*;

public class Window {

    private int width, height;
    private String title;
    private JFrame mainFrame;
    private JPanel panel;

    public Window(String title, int width, int height) {
        this.width = width;
        this.height = height;
        this.title = title;
        this.initWindow();
    }

    public Window(String title) {
        this(title, 1280, 720);
    }

    public void show() {
        this.mainFrame.setVisible(true);
    }

    public void hide() {
        this.mainFrame.setVisible(false);
    }

    public JPanel getPanel() {
        return this.panel;
    }

    public JFrame getMainFrame() {
        return this.mainFrame;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    private void initWindow() {
        JFrame frame = new JFrame(this.title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setPreferredSize(new Dimension(width, height));
        frame.add(panel);
        frame.pack();
        this.mainFrame = frame;
    }
}
