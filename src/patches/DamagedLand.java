package patches;

import agents.Farmer;
import field.FieldObserver;

/**
 * The DamagedLand patch ("X") represents a damaged tile requiring repair.
 * It starts with `fullDamage` and decreases with each update if a Farmer is assigned to repair it.
 * Once fully repaired (`damageLeft == 0`), the land is no longer damaged. The symbol updates based on remaining damage.
 * "X(x)" shows remaining damage, and "X" denotes fully damaged land.
 */
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