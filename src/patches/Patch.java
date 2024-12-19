package patches;

import field.FieldObserver;

/**
 * Represents a stationary environment element (Patch).
 */
public abstract class Patch {
    protected String symbol;
    protected final FieldObserver fieldObserver;

    protected Patch(FieldObserver fieldObserver) {
        this.fieldObserver = fieldObserver;
    }

    public String getSymbol() {
        return symbol;
    }

    protected abstract void setSymbol();
    public abstract void update();
}
