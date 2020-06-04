package fr.evywell.robgame.game.map.grid.notifier;

import fr.evywell.robgame.game.gameobject.GameObject;
import fr.evywell.robgame.game.gameobject.Player;
import fr.evywell.robgame.game.map.grid.Cell;
import fr.evywell.robgame.game.map.grid.Grid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GridNotifier {

    private Map<Cell, List<Player>> listeningCells;
    private Grid grid;
    private int notifierType;
    private List<GameObject> gameObjects;

    public GridNotifier(Grid grid, int notifierType) {
        this.grid = grid;
        this.notifierType = notifierType;
        this.gameObjects = new ArrayList<>();
        listeningCells = new HashMap<>();
    }

    public void emit(Cell cell, AbstractDeliverVisitor visitor) {
        gameObjects.forEach(go -> {
            if (go.getCell().equals(cell)) {
                visitor.visit(go);
            }
        });
    }

    public void listen(GameObject gameObject) {
        if (gameObjects.contains(gameObject)) {
            return;
        }
        this.gameObjects.add(gameObject);
    }

    public void dismiss(GameObject gameObject) {
        this.gameObjects.remove(gameObject);
    }

    public static class GridNotifierType {
        public static final int
            PLAYER = 0x01,
            CREATURE = 0x02,
            GAMEOBJECT = 0x04,
            ALL = 0x07; // Pour le moment. Le but c'est que ce mask prenne en compte tous les autres
    }
}
