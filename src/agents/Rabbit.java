package agents;

import field.FieldEvent;
import field.FieldObserver;
import patches.Carrot;
import patches.DamagedLand;
import patches.Patch;

public class Rabbit extends Agent {
    private boolean isEating;
    private final double stopEatingProbability;

    public Rabbit(FieldObserver fieldObserver) {
        super(fieldObserver);

        isEating = false;
        stopEatingProbability = 0.3;
        setSymbol();
    }

    public Rabbit(FieldObserver fieldObserver, double stopEatingProbability) {
        super(fieldObserver);

        isEating = false;
        this.stopEatingProbability = stopEatingProbability;
        setSymbol();
    }

    @Override
    protected void setSymbol() {
        if (!isEating) {
            symbol = "R";
        } else {
            symbol = "R(E)";
        }
    }

    @Override
    public void run() {
        if (isEating) {
            if (stopEatingProbability > rand.nextDouble()) {
                stopEatingCarrots();
            }
            return;
        }

        Patch patch = field.getPatch(x, y);
        if (patch instanceof Carrot carrots && carrots.isRipe()) {
            eatCarrots();
        } else {
            move();
        }
    }

    private void eatCarrots() {
        isEating = true;
        setSymbol();
        fieldObserver.addEvent(new FieldEvent(FieldEvent.Type.REMOVE_PATCH, x, y, field.getPatch(x, y)));
        fieldObserver.addEvent(new FieldEvent(FieldEvent.Type.ADD_PATCH, x, y, new DamagedLand(fieldObserver)));
    }

    private void stopEatingCarrots() {
        isEating = false;
        setSymbol();
    }
}