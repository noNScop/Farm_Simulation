package field;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class FieldObserver {
    private final Queue<FieldEvent> events;

    public FieldObserver() {
        events = new ConcurrentLinkedQueue<>();
    }

//    synchronized because multiple agents can add events simultaneously
    public void addEvent(FieldEvent event) {
        events.offer(event);
    }

    public FieldEvent getNextEvent() {
        return events.poll();
    }

    public boolean isEmpty() {
        return events.isEmpty();
    }
}
