package agents;

public class Carrot implements Patch {
    private int x;
    private int y;
    private String symbol;
    private int growthLeft;

    public Carrot(int x, int y) {
        this.x = x;
        this.y = y;
        growthLeft = 10;
        setSymbol();
    }

    public Carrot(int x, int y, int growthTime) {
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

    public void turn() {
        if (growthLeft != 0) {
            --growthLeft;
            setSymbol();
        }
    }
}