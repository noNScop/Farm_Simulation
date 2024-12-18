package agents;

/**
 * Represents a movable entity (Agent) capable of performing actions. Each Agent is operating on a separate thread.
 */
public interface Agent extends Runnable{
    public int getX();
    public int getY();
    public String getSymbol();
}
