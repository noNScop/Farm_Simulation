package patches;

import field.FieldObserver;

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
            symbol = "C(" + growthLeft + ")";
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