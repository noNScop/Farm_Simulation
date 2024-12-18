package simulation;

import java.util.List;
import java.util.LinkedList;

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
            field.emplace(agents.get(i).getX(), agents.get(i).getY(), agents.get(i).getSymbol());
        }
        clearTerminal();
        field.displayGrid();
    }

    private void updateField() {
        while (!fieldObserver.isEmpty()) {
            FieldEvent event = fieldObserver.getNextEvent();

            if (event.getType() == FieldEvent.Type.MOVE) {
                Agent agent = event.getAgent();
                int old_x = event.getX();
                int old_y = event.getY();
                field.remove(old_x, old_y, agent.getSymbol());
                field.emplace(agent.getX(), agent.getY(), agent.getSymbol());
            } else if (event.getType() == FieldEvent.Type.ADD_PATCH) {
                Patch patch = event.getPatch();
                patches.add(patch);
                field.emplace(event.getX(), event.getY(), patch.getSymbol());
            } else if (event.getType() == FieldEvent.Type.UPDATE) {
                Patch patch = event.getPatch();
                if (patch == null) {
                    Agent agent = event.getAgent();
                    field.remove(agent.getX(), agent.getY(), event.getOldSymbol());
                    field.emplace(agent.getX(), agent.getY(), agent.getSymbol());
                } else {
                    field.remove(event.getX(), event.getY(), event.getOldSymbol());
                    field.emplace(event.getX(), event.getY(), patch.getSymbol());
                }
            }
        }
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

        for (Patch patch : patches) {
            patch.update();
        }

        updateField();
        field.displayGrid();
    }

    public void runSimulation() {
        for (int i = 0; i < 100; ++i) {
            try {
                Thread.sleep(1000);
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