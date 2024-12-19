package agents;

import field.FieldEvent;
import field.FieldObserver;
import patches.Carrot;
import patches.Patch;

public class Rabbit extends Agent {
    private boolean isEating;

    public Rabbit(FieldObserver fieldObserver) {
        super(fieldObserver);

        isEating = false;
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
        Patch patch = field.getPatch(x, y);
        if (patch instanceof Carrot carrots && carrots.isRipe()) {
            eatCarrots();
        } else {
            move();
        }
    }

    public void eatCarrots() {
        isEating = true;
        setSymbol();
        fieldObserver.addEvent(new FieldEvent(FieldEvent.Type.REMOVE_PATCH, x, y, field.getPatch(x, y)));
        fieldObserver.addEvent(new FieldEvent(FieldEvent.Type.ADD_PATCH, x, y, new Carrot(fieldObserver)));
    }
}