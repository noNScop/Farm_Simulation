package simulation;

import simulation.Simulation;
import field.Field;

public class SimulationRunner {
    public static void main(String[] args) {
//        Create a Singleton Field
        Field.createInstance(10, 10);
        Simulation simulation = new Simulation(2);
        simulation.runSimulation();
//        Ensure the field is reset after each simulation
        Field.destroyInstance();
    }
}