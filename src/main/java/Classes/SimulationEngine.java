package Classes;

import Interfaces.IEngine;
import Interfaces.IMapObserver;
import Interfaces.IWorldMap;

import java.util.Random;

import static java.lang.Thread.sleep;

public class SimulationEngine implements IEngine, Runnable {
    public MapWithBorders map;
    public MapStatistics stats;
    public int moveDelay;
    private boolean running = true;
    private boolean paused = false;
    private final Object pauseLock = new Object();

    public SimulationEngine(MapWithBorders map, int numberOfAnimals, int moveDelay, IMapObserver gui) {
        this.map = map;
        this.placeAnimalsOnMap(numberOfAnimals);
        this.moveDelay = moveDelay;
        this.stats = new MapStatistics(numberOfAnimals, this.map.plantsList.size(), this.map.startEnergy);
        this.map.addObserver(gui);
        this.map.addObserver(this.stats);
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
        while(this.running) {
            synchronized (this.pauseLock) {
                if(!this.running) {
                    break;
                }
                if(this.paused) {
                    try {
                        synchronized (this.pauseLock) {
                            this.pauseLock.wait();
                        }
                    }
                    catch(InterruptedException ex) {
                        break;
                    }
                    if(!this.running) {
                        break;
                    }
                }
                map.removeDeadAnimals(this.stats.day);
                map.moveAnimals();
                map.eat();
                map.reproduceAnimals();
                map.createPlants();
                map.dayPassed();
                try {
                    sleep(moveDelay);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
    }

    public void stop() {
        running = false;
        this.resume();
    }

    public void pause() {
        paused = true;
    }

    public void resume() {
        synchronized (pauseLock) {
            paused = false;
            pauseLock.notifyAll();
        }
    }
}
