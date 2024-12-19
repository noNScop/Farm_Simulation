package patches;

import agents.Farmer;
import field.FieldObserver;

public class DamagedLand extends Patch {
    private int damageLeft;
    private final int fullDamage;
    private Farmer repairFarmer;

    public DamagedLand(FieldObserver fieldObserver) {
        super(fieldObserver);
        fullDamage = 3;
        damageLeft = fullDamage;
        setSymbol();
    }

    public DamagedLand(FieldObserver fieldObserver, int damageRepairTime) {
        super(fieldObserver);
        fullDamage = damageRepairTime;
        damageLeft = fullDamage;
        setSymbol();
    }

    @Override
    protected void setSymbol() {
        if (damageLeft == fullDamage) {
            symbol = "X";
        } else {
            symbol = "X(" + damageLeft + ")";
        }
    }

    public void setRepairFarmer(Farmer farmer) {
        repairFarmer = farmer;
    }

    public Farmer getRepairFarmer() {
        return repairFarmer;
    }

    public boolean isRepaired() {
        return damageLeft == 0;
    }

    @Override
    public void update() {
        if (repairFarmer != null) {
            --damageLeft;
            setSymbol();
        }
    }
}