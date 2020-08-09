package fr.evywell.robgame.game.entities;

import fr.evywell.common.maths.Vector3;
import fr.evywell.common.network.Packet;
import fr.evywell.robgame.game.map.grid.Cell;
import fr.evywell.robgame.game.map.grid.notifier.GridNotifier;
import fr.evywell.robgame.game.map.Map;
import fr.evywell.robgame.game.map.grid.notifier.PacketDeliverVisitor;

import java.util.HashMap;

public class GameObject {

    protected GridNotifier notifier;
    protected Map map;
    protected Cell cell;

    public ObjectGuid guid;
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

    public GameObject[] getGameObjectsInArea() {
        return this.map.getGameObjectsInAreaOf(this);
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
                "guid='" + guid + '\'' +
                ", mapId=" + mapId +
                ", pos_x=" + pos_x +
                ", pos_y=" + pos_y +
                ", pos_z=" + pos_z +
                ", orientation=" + orientation +
                '}';
    }

    public int getType() {
        return GameObjectType.GAME_OBJECT;
    }

    public java.util.Map<String, Object> toMap() {
        java.util.Map<String, Object> go = new HashMap<>();
        go.put("guid", guid.getGuid());
        go.put("map_id", mapId);
        go.put("pos_x", pos_x);
        go.put("pos_y", pos_y);
        go.put("pos_z", pos_z);
        go.put("orientation", orientation);
        go.put("type", getType());

        return go;
    }

    public void putIntoPacket(Packet src) {
        src.putLong(guid.getGuid());
        src.putInt(mapId);
        src.putFloat(pos_x);
        src.putFloat(pos_y);
        src.putFloat(pos_z);
        src.putFloat(orientation);
        src.putInt(getType());
    }

}
