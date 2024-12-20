package agents;

import field.FieldEvent;
import field.FieldObserver;
import simulation.ThreadManager;

import java.util.*;

/**
 * The Dog can be idle ("D") or hunting ("D!"), with behavior varying by state.
 * In the idle state, it moves randomly in a von Neumann neighborhood and scans up to 5 tiles for rabbits.
 * Hunting is triggered either by detecting a rabbit itself or by its owner (Farmer) encountering one.
 * The Dog prioritizes rabbits spotted by its owner unless it is already hunting a previously assigned rabbit.
 */
public class Dog extends Agent {
    private final Farmer owner;
    private boolean ownerOrder;
    private Rabbit prey;

    private static final List<TileCords> MOVES = List.of(
            new TileCords(1, 0),
            new TileCords(-1, 0),
            new TileCords(0, 1),
            new TileCords(0, -1)
    );

    public Dog(FieldObserver fieldObserver, ThreadManager threadManager, Farmer farmer) {
        super(fieldObserver, threadManager);
        owner = farmer;
        ownerOrder = false;
        setSymbol();
    }

    @Override
    protected void setSymbol() {
        if (prey == null) {
            symbol = "D";
        } else {
            symbol = "D!";
        }
    }

    // Checks whether the owner encountered a rabbit in the last turn, the map retains the state from
    // the last turn therefore it can be accessed directly, the Farmer however could have already moved during
    // the current turn, therefore it (Farmer) stores its location from the previous round as oldX and oldY
    private void checkOwner() {
        Rabbit newPrey = (Rabbit) field.getAgent(owner.getOldX(), owner.getOldY(), Rabbit.class);
        if (newPrey != null) {
            ownerOrder = true;
            prey = newPrey;
            setSymbol();
        }
    }

    @Override
    public void step() {
        // if dog has no active order from its master, check if he didn't spot a rabbit,
        // otherwise focus on the task
        if (!ownerOrder) {
            checkOwner();
        }
        // check if they prey hasn't been hunted down already by another dog
        if (prey != null && prey.isDestroyed()) {
            prey = null;
            ownerOrder = false;
            setSymbol();
        }

        if (prey != null || scanSurroundings()) {
            hunt();
        } else {
            move();
        }
    }

    private void hunt() {
        // it is possible that the rabbit will change its position after its coordinates are read
        // however it doesn't really matter - the dog will hunt it down anyway. It can be treated as
        // a nondeterministic attempt of escape, impossible to succeed though
        int prey_x = prey.getX();
        int prey_y = prey.getY();
        // the Dog doesn't move if the rabbit is on the same tile as he is
        if (prey_x != x || prey_y != y) {
            // Store position of a dog before its move
            FieldEvent event = new FieldEvent(FieldEvent.Type.MOVE, x, y, this);
            // Move towards the prey, diagonal moves allowed
            if (prey_x > x) {
                x += 1;
            } else if (prey_x < x) {
                x -= 1;
            }

            if (prey_y > y) {
                y += 1;
            } else if (prey_y < y) {
                y -= 1;
            }
            // Add the event once the move operation succeeds, if prey is in the same
            fieldObserver.addEvent(event);
        }
        // if after the move (or not if it was not necessary) the rabbit is on the same tile as the Dog
        // remove the rabbit from the map, and reset the state of the Dog
        if (prey_x == x && prey_y == y) {
            fieldObserver.addEvent(new FieldEvent(FieldEvent.Type.REMOVE_AGENT, x, y, prey));
            prey = null;
            ownerOrder = false;
            setSymbol();
        }
    }

    private boolean isValidTile(int x, int y) {
        return 0 <= x && x < field.getWidth() && 0 <= y && y < field.getHeight();
    }

    // Returns true if it finds rabbit in a radius of 5
    private boolean scanSurroundings() {
        Queue<SurroundingTile> queue = new LinkedList<>();
        Set<TileCords> visited = new HashSet<>();
        visited.add(new TileCords(x, y));
        queue.add(new SurroundingTile(x, y, 0));

        while (!queue.isEmpty()) {
            SurroundingTile currentTile = queue.poll();
            int x = currentTile.x();
            int y = currentTile.y();
            int distance = currentTile.distance();
            Rabbit newPrey = (Rabbit) field.getAgent(x, y, Rabbit.class);
            // if found the rabbit, set it as a prey and return true
            if (newPrey != null) {
                prey = newPrey;
                setSymbol();
                return true;
            } else if (distance < 5) {
                for (TileCords m : MOVES) {
                    int newX = x + m.x();
                    int newY = y + m.y();
                    TileCords tile = new TileCords(newX, newY);

                    if (isValidTile(newX, newY) && visited.add(tile)) {
                        queue.add(new SurroundingTile(newX, newY, distance+1));
                    }
                }
            }
        }
        // no rabbit in a range of 5 tiles
        return false;
    }
}

record SurroundingTile(int x, int y, int distance) {
}

record TileCords(int x, int y) {
}