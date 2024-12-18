package simulation;

import java.nio.file.Path;
import java.util.List;
import java.util.LinkedList;
import java.util.concurrent.CyclicBarrier;

import agents.Agent;
import agents.Patch;
import agents.Farmer;
import field.Field;
import field.FieldEvent;
import field.FieldObserver;

public class Simulation {
    public Field field;
    private FieldObserver fieldObserver;
    public List<Agent> agents = new LinkedList<>();
    public List<Patch> patches = new LinkedList<>();

    public Simulation(int farmers) {
        field = Field.getInstance();
        fieldObserver = new FieldObserver();

        for (int i = 0; i < farmers; ++i) {
            this.agents.add(new Farmer(fieldObserver));
            field.emplaceAgent(agents.get(i).getX(), agents.get(i).getY(), agents.get(i).getSymbol());
        }
        clearTerminal();
        field.displayGrid();
    }

    private void updateField() {
        List<FieldEvent> events = fieldObserver.getEvents();
        for (FieldEvent event : events) {
            if (event.getType() == FieldEvent.Type.MOVE) {
                Agent agent = event.getAgent();
                int old_x = event.getX();
                int old_y = event.getY();
                field.removeAgent(old_x, old_y, agent.getSymbol());
                field.emplaceAgent(agent.getX(), agent.getY(), agent.getSymbol());
            }
        }
        fieldObserver.clearEvents();
    }

    public void step() {
        Thread[] threads = new Thread[agents.size()];
        for (int i = 0; i < agents.size(); ++i) {
            threads[i] = new Thread(agents.get(i));
            threads[i].start();
        }

        // Wait for all threads to complete
        for (Thread thread : threads) {
            try {
                thread.join(); // Wait for this thread to finish
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restore the interrupt status
                System.out.println("Main thread was interrupted.");
            }
        }

        updateField();
        field.displayGrid();
    }

    public void runSimulation() {
        for (int i = 0; i < 100; ++i) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            clearTerminal();
            step();
        }
    }

    private static void clearTerminal() {
        // print line in case the terminal doesn't support ANSI escape code e.g. in IDE
        System.out.println();
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}