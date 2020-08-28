package fr.evywell.common.event;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventManager {

    private int internalTime;
    private Map<Integer, List<Event>> events;

    public EventManager() {
        internalTime = 0;
        events = new ConcurrentHashMap<>();
    }

    public void addEvent(Event event, int time) {
        if (!events.containsKey(time)) {
            events.put(time, new CopyOnWriteArrayList<>());
        }
        List<Event> e = events.get(time);
        e.add(event);
    }

    public void update(int localTime) {
        internalTime += localTime;
        Set<Integer> keys = events.keySet();
        for (int time : keys) {
            if (time > internalTime) {
                continue;
            }
            // Si la liste est vide, on la supprime
            List<Event> events = this.events.get(time);
            if (events.isEmpty()) {
                this.events.remove(time);
                continue;
            }
            for (Event event : events) {
                if (!event.execute(localTime, internalTime)) {
                    addEvent(event, calculateTime(1));
                }
                events.remove(event);
            }
        }
    }

    public int calculateTime(int offset) {
        return internalTime + offset;
    }

}
