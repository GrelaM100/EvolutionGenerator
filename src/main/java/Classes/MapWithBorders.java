package Classes;

import Interfaces.IMapElement;
import Interfaces.IWorldMap;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class MapWithBorders implements IWorldMap {
    public int width;
    public int height;
    public int startEnergy;
    public int moveEnergy;
    public int plantEnergy;
    public double jungleRatio;
    public Map<Vector2d, LinkedList<Animal>> animals = new HashMap<>();
    public LinkedList<Animal> animalsList;
    public Map<Vector2d, Grass> tufts = new HashMap<>();
    public LinkedList<Grass> tuftsList;


    public MapWithBorders(int width, int height, int startEnergy, int moveEnergy, int plantEnergy, double jungleRatio) {
        this.width = width;
        this.height = height;
        this.startEnergy = startEnergy;
        this.moveEnergy = moveEnergy;
        this.plantEnergy = plantEnergy;
        this.jungleRatio = jungleRatio;
    }


    @Override
    public boolean canMoveTo(Vector2d position) {
        return position.x < width && position.y < height;
    }

    @Override
    public boolean place(Animal animal) {
        Object object = objectAt(animal.getPosition());
        if(object instanceof Animal) {
            throw new IllegalArgumentException("Position " + animal.getPosition().toString() + " is already occupied");
        }
        LinkedList<Animal> tempList = animals.get(animal.getPosition());
        tempList.add(animal);
        return true;
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return animals.get(position) != null;
    }

    @Override
    public Object objectAt(Vector2d position) {
        LinkedList<Animal> tempList = animals.get(position);
        if(tempList == null) return tufts.get(position);
        else if(tempList.size() == 0) return tufts.get(position);
        else return tempList.getFirst();
    }
}
