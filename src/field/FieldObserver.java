package field;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The FieldObserver class is responsible for managing and storing field events.
 * It maintains a thread-safe queue of events related to changes in the field, such as
 * agent movements or patch additions/removals. This class allows the addition and retrieval
 * of events in a FIFO (First In, First Out) manner.

 * It provides the following key functionalities:
 * 1. Adding new events to the queue.
 * 2. Retrieving and removing the next event from the queue.
 * 3. Checking if there are any events left to process.
 */
public class FieldObserver {
    private final Queue<FieldEvent> events;

    public FieldObserver() {
        events = new ConcurrentLinkedQueue<>();
    }

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
