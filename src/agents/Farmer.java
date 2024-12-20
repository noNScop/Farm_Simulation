package agents;

import field.FieldEvent;
import field.FieldObserver;
import patches.Carrot;
import patches.DamagedLand;
import patches.Patch;
import simulation.ThreadManager;

import java.util.concurrent.locks.Condition;

public class Farmer extends Agent {
//    store coordinates before thr turn, so dog can correctly check if farmer encountered
//    rabbit (in previous simulation step)
    private int oldX;
    private int oldY;

    public Farmer(FieldObserver fieldObserver, ThreadManager threadManager) {
        super(fieldObserver, threadManager);
        oldX = x;
        oldY = y;
        setSymbol();
    }

    @Override
    protected void setSymbol() {
        symbol = "F";
    }

    @Override
    public void step() {
        oldX = x;
        oldY = y;
        Patch patch = field.getPatch(x, y);
        if (patch == null) {
            plantCarrots();
        } else if (patch instanceof DamagedLand land) {
            fixLand(land);
        } else {
            move();
        }
    }

    private void plantCarrots() {
        fieldObserver.addEvent(new FieldEvent(FieldEvent.Type.ADD_PATCH, x, y, new Carrot(fieldObserver)));
    }

    private void fixLand(DamagedLand land) {
        Farmer repairFarmer = land.getRepairFarmer();
        if (repairFarmer == this) {
            if (land.isRepaired()) {
                fieldObserver.addEvent(new FieldEvent(FieldEvent.Type.REMOVE_PATCH, x, y, field.getPatch(x, y)));
            }
        } else if (repairFarmer == null) {
            land.setRepairFarmer(this);
        }
    }

    public int getOldX() {
        return oldX;
    }

    public int getOldY() {
        return oldY;
    }
}