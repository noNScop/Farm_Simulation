package agents;

import field.FieldEvent;
import field.FieldObserver;
import patches.Carrot;

public class Farmer extends Agent {
    public Farmer(FieldObserver fieldObserver) {
        super(fieldObserver);
    }

    protected void setSymbol() {
        symbol = "F";
    }

    @Override
    public void run() {
        if (field.hasPatch(x, y, Carrot.class)) {
            move();
        } else {
            plantCarrots();
        }
    }

    public void plantCarrots() {
        fieldObserver.addEvent(new FieldEvent(FieldEvent.Type.ADD_PATCH, x, y, new Carrot(fieldObserver, x, y)));
    }
}