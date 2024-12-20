package patches;

import field.FieldObserver;

/**
 * The Carrot patch (",") grows over time, reducing its growth time until it ripens ("C").
 * Once ripe, it can be eaten by rabbits. The growth process is tracked with `growthLeft`, which
 * decreases every update. The patch becomes ready to eat when `growthLeft` reaches zero.
 */
public class Carrot extends Patch {
    private int growthLeft;

    public Carrot(FieldObserver fieldObserver) {
        super(fieldObserver);
        growthLeft = 10;
        setSymbol();
    }

    public Carrot(FieldObserver fieldObserver, int growthTime) {
        super(fieldObserver);
        growthLeft = growthTime;
        setSymbol();
    }

    @Override
    protected void setSymbol() {
        if (growthLeft == 0) {
            symbol = "C";
        } else {
            symbol = ",";
        }
    }

    public boolean isRipe() {
        return growthLeft == 0;
    }

    @Override
    public void update() {
        if (growthLeft != 0) {
            --growthLeft;
            setSymbol();
        }
    }
}