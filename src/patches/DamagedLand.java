package patches;

import field.FieldObserver;

public class DamagedLand extends Patch {
    private int damageLeft;
    private final int fullDamage;
    private boolean repairStatus;

    public DamagedLand(FieldObserver fieldObserver) {
        super(fieldObserver);
        repairStatus = false;
        fullDamage = 3;
        damageLeft = fullDamage;
        setSymbol();
    }

    public DamagedLand(FieldObserver fieldObserver, int damageRepairTime) {
        super(fieldObserver);
        repairStatus = false;
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

    public void setRepairStatus(boolean value) {
        repairStatus = value;
    }

    public boolean isRepaired() {
        return damageLeft == 0;
    }

    @Override
    public void update() {
        if (repairStatus) {
            --damageLeft;
            setSymbol();
        }
    }
}