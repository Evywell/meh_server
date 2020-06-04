package fr.evywell.robclient.game;

import fr.evywell.robclient.draw.Grid;
import fr.evywell.robclient.draw.Layer;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Map {

    private final Game game;
    private final Player player;
    private final JComponent component;
    private final int width;
    private final int height;
    private List<Layer> layers;

    public static final float TILE_SIZE = 5f;
    public static final int MULTIPLIER = 24;

    public Map(Game game, Player player, JComponent component, int width, int height) {
        this.game = game;
        this.player = player;
        this.component = component;
        this.width = width;
        this.height = height;
        this.layers = new ArrayList<>();
    }

    public void build() {
        // On construit la map
        Layer mainLayer = new Layer(component.getWidth(), component.getHeight());
        component.add(mainLayer);
        this.layers.add(mainLayer);

        this.buildGrid();
        // On dessine le joueur
        mainLayer.addDrawable(player);
    }

    private void buildGrid() {
        Grid g = new Grid(width, height, Color.RED);
        this.layers.get(0).addDrawable(g);
    }
}
