package agents;

import field.FieldEvent;
import field.FieldObserver;

public class Carrot implements Patch {
    private int x;
    private int y;
    private String symbol;
    private int growthLeft;
    private FieldObserver fieldObserver;

    public Carrot(FieldObserver fieldObserver, int x, int y) {
        this.fieldObserver = fieldObserver;
        this.x = x;
        this.y = y;
        growthLeft = 10;
        setSymbol();
    }

    public Carrot(FieldObserver fieldObserver, int x, int y, int growthTime) {
        this.fieldObserver = fieldObserver;
        this.x = x;
        this.y = y;
        growthLeft = growthTime;
        setSymbol();
    }

    private void setSymbol() {
        if (growthLeft == 0) {
            symbol = "C";
        } else {
            symbol = "C(" + growthLeft + ")";
        }
    }

    @Override
    public String getSymbol() {
        return symbol;
    }

    @Override
    public void update() {
        if (growthLeft != 0) {
            String oldSymbol = symbol;

            --growthLeft;
            setSymbol();

            fieldObserver.addEvent(new FieldEvent(x, y, oldSymbol, this));
        }
    }
}