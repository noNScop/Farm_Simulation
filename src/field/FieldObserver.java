package field;

import agents.Carrot;
import agents.Farmer;
import jdk.jfr.Event;

import java.util.ArrayList;
import java.util.List;

public class FieldObserver {
    private List<FieldEvent> events;

    public FieldObserver() {
        events = new ArrayList<>();
    }

//    synchronized because multiple agents can add events simultaneously
    public synchronized void addEvent(FieldEvent event) {
        events.add(event);
    }

    public List<FieldEvent> getEvents() {
        return events;
    }

    public void clearEvents() {
        events = new ArrayList<>();
    }
}
