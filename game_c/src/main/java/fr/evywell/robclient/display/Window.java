package fr.evywell.robclient.display;

import fr.evywell.robclient.game.Map;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Window {

    private int width, height;
    private String title;
    private JFrame mainFrame;
    private JPanel panel;
    private TextArea textArea;

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

    public TextArea getTextArea() {
        return textArea;
    }

    private void initWindow() {
        JFrame frame = new JFrame(this.title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setPreferredSize(new Dimension(width, height));
        panel.setLayout(new BorderLayout());
        textArea = new TextArea();
        panel.add(textArea, BorderLayout.EAST);
        frame.add(panel);
        frame.pack();
        this.mainFrame = frame;
    }

}
