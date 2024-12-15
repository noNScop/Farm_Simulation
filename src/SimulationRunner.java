import simulation.Simulation;

public class SimulationRunner {
    public static void main(String[] args) {
        Simulation simulation = new Simulation(10, 10, 10);
        simulation.run();
    }
}