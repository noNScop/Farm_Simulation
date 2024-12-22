# Farm Simulation

This farm simulation models a virtual environment where different agents (such as **farmers**, **dogs** and **rabbits**), and patches such as (**carrots** and **damaged land**) interact with one another. The simulation operates on a grid-based field, and uses multi-threading to simulate actions happening simultaneously, creating an immersive and dynamic virtual farm environment.

## Features
- **Field Management**: The field is represented as a grid, where various entities (agents) move, interact, and evolve over time.
- **Agents**:
    - **Farmers**: Operate in the field, potentially interacting with rabbits and dogs.
    - **Dogs**: Hunt rabbits on the field, adding a dynamic layer to agent interactions.
    - **Rabbits**: Spawn randomly based on a defined probability and interact with other agents.
    - **Carrots**: Grow and are used by rabbits to eat, influencing the behavior of agents.
- **Multi-threading**: Utilizes concurrent threads to simulate real-time actions by agents (farmers, dogs, rabbits) and field updates.
- **User Interaction**:
    - Interactive menus allow for adjusting simulation settings (field size, agent count, simulation speed, etc.).
    - Control and visibility of a **legend** that explains the state of agents on the field.
    - Ability to start and restart the simulation at any time.
- **Simulation Settings**: Settings such as the field dimensions, number of agents, and simulation speed can be configured dynamically.

## Requirements
- **Java**: The simulation was written in **Java 23**. Older versions might work, but compatibility is not guaranteed.
- **Multi-core Processor**: For better performance, a multi-core processor is recommended due to the heavy usage of multi-threading in this simulation.
- Tested on **MacOS** and **Linux**

## Running the Simulation
Running the simulation is straightforward, the entry class is named **SimulationRunner.java**, once you compile and run the program, a descriptive menu will show up that will allow you to set up and run the simulation.

## Configuration

Simulation settings such as field width, field height, farmer count, rabbit spawn probability, and simulation offset (step time) can be adjusted via the menus, and these values will persist as long as the application is running. Settings can also be saved to a file for future use.
- **Field Dimensions**: The field width and height define the size of the grid (limits are 1 to 100 for both, as displaying larger fields in the terminal gets laggy).
- **Farmer Count**: The number of farmers initially spawned in the field (the limit is from 1 to 1000, having more farmers could break the program, as each farmer and its dog run on a separate thread).
- **Rabbit Spawn Probability**: Defines the probability of rabbits spawning each simulation step (0.0 - 1.0).
- **Step Interval (ms)**: The delay between simulation steps in milliseconds, affecting the simulation speed, it can't be lower than 50 ms, as on higher refresh rates the terminal is not catching up with displaying the field and it is blinking.

## Key Components

- **Field**: The field is managed through a singleton Field class, providing access to grid patches and the simulation environment.
- **Agent Classes**: Specific agents like Farmer, Dog and Rabbit are represented as moving objects interacting in the field. Each agent runs on a separate thread.
- **Patch Classes**: Static objects that can be manipulated by **Agents**, each **Tile** on a **Field** can have only 1 patch at a time.
- **Thread Manager**: Handles synchronization of agents’ actions and the display updates.
- **Display Manager**: Displays the field and menu options, updating the user interface after each simulation step, works on a separate thread so it can update the display at anytime.
- **Input Manager**: Manages user input for interacting with the simulation, works on a separate thread so it can parse input at anytime.

## Project Structure

```bash
Farm_Simulation/
├── conceptual_UML.png
├──── src/
│   ├── agents/        # Agents - moving agents
│   ├── field/         # Classes revolving around field
│   ├── patches/       # Patches - static agents
│   └── simulation/    # Classes responsible for general simulation utilities
└── README.md          # Project description
```

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for more information.