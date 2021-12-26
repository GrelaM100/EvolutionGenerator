package Classes;

import Interfaces.IEngine;
import Interfaces.IWorldMap;

import java.util.Random;

public class SimulationEngine implements IEngine {
    private MapWithBorders map;

    SimulationEngine(MapWithBorders map, int numberOfAnimals) {
        this.map = map;
        this.placeAnimalsOnMap(numberOfAnimals);
    }

    private void placeAnimalsOnMap(int numberOfAnimals) {
        Random rand = new Random();
        for(int i = 0; i < numberOfAnimals; i++) {

            Animal animal = new Animal(this.map, new Vector2d(rand.nextInt(this.map.width), rand.nextInt(this.map.height)),
                    this.map.startEnergy);
            this.map.place(animal);
        }
    }

    @Override
    public void run() {
        while(true) {
            map.removeDeadAnimals();
            map.moveAnimals();
            map.eat();
            map.reproduceAnimals();
            map.createPlants();
        }
    }
}
