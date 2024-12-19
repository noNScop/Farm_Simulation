package agents;

import field.FieldEvent;
import field.FieldObserver;
import patches.Carrot;
import patches.DamagedLand;
import patches.Patch;

public class Farmer extends Agent {
    public Farmer(FieldObserver fieldObserver) {
        super(fieldObserver);
        setSymbol();
    }

    @Override
    protected void setSymbol() {
        symbol = "F";
    }

    @Override
    public void run() {
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
}