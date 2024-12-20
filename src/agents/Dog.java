package agents;

import field.FieldEvent;
import field.FieldObserver;
import simulation.ThreadManager;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;

public class Dog extends Agent {
    private final Farmer owner;
    private boolean ownerOrder;
    private Rabbit prey;

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
//        if dog has no active order from its master, check if he didn't spot a rabbit,
//        otherwise focus on the task
        if (!ownerOrder) {
            checkOwner();
        }
//        check if they prey hasn't been hunted down already by another dog
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
        int prey_x = prey.getX();
        int prey_y = prey.getY();
//        Store position of a dog before its move
        FieldEvent event = new FieldEvent(FieldEvent.Type.MOVE, x, y, this);
//        Move towards prey, diagonal moves allowed
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
//            Add the event once the move operation succeeds
        fieldObserver.addEvent(event);

        if (prey_x == x && prey_y == y) {
            fieldObserver.addEvent(new FieldEvent(FieldEvent.Type.REMOVE_AGENT, x, y, prey));
            prey = null;
            ownerOrder = false;
            setSymbol();
        }
    }

    private boolean isValidTile(int x, int y) {
        return 0 <= x && x < fieldColumns && 0 <= y && y < fieldRows;
    }

//    Returns true if it finds rabbit in a radius of 5
    private boolean scanSurroundings() {
        List<TileCords> moves = new ArrayList<>();
        moves.add(new TileCords(1, 0));
        moves.add(new TileCords(-1, 0));
        moves.add(new TileCords(0, 1));
        moves.add(new TileCords(0, -1));

        Queue<SurroundingTile> queue = new LinkedList<>();
        List<TileCords> visited = new ArrayList<>();
        visited.add(new TileCords(x, y));
        queue.add(new SurroundingTile(x, y, 0));

        while (!queue.isEmpty()) {
            SurroundingTile currentTile = queue.poll();
            int x = currentTile.x();
            int y = currentTile.y();
            int distance = currentTile.distance();
            Rabbit newPrey = (Rabbit) field.getAgent(x, y, Rabbit.class);

            if (newPrey != null) {
                prey = newPrey;
                setSymbol();
                return true;
            } else if (distance < 5) {
                for (TileCords m : moves) {
                    int newX = x + m.x();
                    int newY = y + m.y();
                    TileCords tile = new TileCords(newX, newY);

                    if (isValidTile(newX, newY) && !visited.contains(tile)) {
                        visited.add(tile);
                        queue.add(new SurroundingTile(newX, newY, distance+1));
                    }
                }
            }
        }
        return false;
    }
}

record SurroundingTile(int x, int y, int distance) {
}

record TileCords(int x, int y) {
}