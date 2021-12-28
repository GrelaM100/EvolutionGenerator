package Interfaces;

import Classes.Animal;
import Classes.Plant;
import Classes.Vector2d;

import java.util.LinkedList;

public interface IWorldMap {
    boolean canMoveTo(Vector2d position);
    boolean place(IMapElement mapElement);
    boolean isOccupied(Vector2d position);
    Object objectAt(Vector2d position);
    int getWidth();
    int getHeight();
    LinkedList<Plant> getPlantsList();
    int getStartEnergy();
    LinkedList<Animal> getAnimalsList();
    void removeDeadAnimals(int day);
    void moveAnimals();
    void eat();
    void reproduceAnimals();
    void createPlants();
    void dayPassed();
    void addObserver(IMapObserver observer);
    boolean isInJungle(Vector2d position);
}
