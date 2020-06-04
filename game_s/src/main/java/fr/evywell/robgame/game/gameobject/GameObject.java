package fr.evywell.robgame.game.gameobject;

import fr.evywell.common.maths.Vector3;
import fr.evywell.common.network.Packet;
import fr.evywell.robgame.game.map.grid.Cell;
import fr.evywell.robgame.game.map.grid.notifier.GridNotifier;
import fr.evywell.robgame.game.map.Map;
import fr.evywell.robgame.game.map.grid.notifier.PacketDeliverVisitor;

public class GameObject {

    protected GridNotifier notifier;
    protected Map map;
    protected Cell cell;

    public String uuid;
    public int mapId;
    public float pos_x, pos_y, pos_z, orientation;

    public void update(int delta) {}

    public void move(float off_x, float off_y, float off_z) {
        this.pos_x += off_x;
        this.pos_y += off_y;
        this.pos_z += off_z;

        this.map.moveGameObjectInMap(this);
    }

    public void move(Vector3 v) {
        move(v.x, v.y, v.z);
    }

    public void sendPacketToSet(Packet packet, Player skipPlayer) {
        // On créé un visiteur qui va se charger de toute la partie callback
        PacketDeliverVisitor visitor = new PacketDeliverVisitor(this, packet, skipPlayer);
        Cell[] cells = this.cell.getNeighboring();
        for (Cell cell : cells) {
            cell.visitPlayers(visitor);
        }
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public Cell getCell() {
        return cell;
    }

    public void setCell(Cell cell) {
        this.cell = cell;
    }

    @Override
    public String toString() {
        return "{" +
                "uuid='" + uuid + '\'' +
                ", mapId=" + mapId +
                ", pos_x=" + pos_x +
                ", pos_y=" + pos_y +
                ", pos_z=" + pos_z +
                ", orientation=" + orientation +
                '}';
    }
}
