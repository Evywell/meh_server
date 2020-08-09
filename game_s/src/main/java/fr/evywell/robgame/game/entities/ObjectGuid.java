package fr.evywell.robgame.game.entities;

import fr.evywell.common.container.Container;
import fr.evywell.robgame.Service;
import fr.evywell.robgame.world.World;

public class ObjectGuid {

    private long high;
    private int low;

    public static final byte PLAYER = 1;
    public static final byte CREATURE = 2;
    public static final byte GAME_OBJECT = 3;

    public ObjectGuid(long high, int low) {
        this.high = high;
        this.low = low;
    }

    public byte getType() {
        return (byte) (high >> 16);
    }

    public int getUuid() {
        return low;
    }

    public long getGuid() {
        return (high << 33) | low;
    }

    @Override
    public String toString() {
        return "ObjectGuid{" +
                "high=" + high +
                ", uuid=" + getUuid() +
                '}';
    }

    public static ObjectGuid createPlayer(int id) {
        World world = (World) Container.getInstance(Service.WORLD);
        int realm = world.getRealm().realmId;
        long high = (PLAYER << 16) | realm;
        return new ObjectGuid(high, id);
    }

    public static ObjectGuid createCreature(int id) {
        World world = (World) Container.getInstance(Service.WORLD);
        int realm = world.getRealm().realmId;
        long high = (CREATURE << 16) | realm;
        return new ObjectGuid(high, id);
    }

    public static ObjectGuid createFromGuid(long guid) {
        long high = guid >> 33;
        int low = (int) (guid);
        return new ObjectGuid(high, low);
    }

}
