package Classes;

import Enums.MapDirection;
import Enums.MoveDirection;
import Interfaces.IMapElement;
import Interfaces.IWorldMap;

import java.util.*;

public class MapWithBorders implements IWorldMap {
    public int width;
    public int height;
    public int startEnergy;
    public int moveEnergy;
    public int plantEnergy;
    public double jungleRatio;
    public Map<Vector2d, LinkedList<Animal>> animals;
    public LinkedList<Animal> animalsList;
    public Map<Vector2d, Plant> plants;
    public LinkedList<Plant> plantsList;


    public MapWithBorders(int width, int height, int startEnergy, int moveEnergy, int plantEnergy, double jungleRatio) {
        this.width = width;
        this.height = height;
        this.startEnergy = startEnergy;
        this.moveEnergy = moveEnergy;
        this.plantEnergy = plantEnergy;
        this.jungleRatio = jungleRatio;
        this.animals = new HashMap<>();
        this.animalsList = new LinkedList<>();
        this.plants = new HashMap<>();
        this.plantsList = new LinkedList<>();
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return position.x < width && position.y < height;
    }

    @Override
    public boolean place(IMapElement mapElement) {
        Vector2d position = mapElement.getPosition();
        if(isOccupied(position)) {
            return false;
        }
        if(mapElement instanceof Plant) {
            if(this.plants.get(position) != null) {
                return false;
            }
            this.plants.put(position, (Plant) mapElement);
            this.plantsList.add((Plant) mapElement);
        }
        else if(mapElement instanceof Animal) {
            LinkedList<Animal> tempList = new LinkedList<>();
            tempList.add((Animal) mapElement);
            this.animals.put(position, tempList);
            this.animalsList.add((Animal) mapElement);
        }

        return true;
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return this.animals.get(position) != null || this.animals.get(position).size() > 0;
    }

    @Override
    public Object objectAt(Vector2d position) {
        LinkedList<Animal> tempList = this.animals.get(position);
        if(tempList == null || tempList.size() == 0) return this.plants.get(position);
        else return tempList.getFirst();
    }

    public void removeDeadAnimals() {
        for(Animal animal : this.animalsList) {
            if(animal.energy == 0) {
                this.animalsList.remove(animal);
                //TODO informowanie hashmapy o zmianie energii do 0
            }
        }
    }

    public void moveAnimals() {
        for(Animal animal : this.animalsList) {
            Random rand = new Random();
            int index = rand.nextInt(MoveDirection.values().length);
            animal.move(MoveDirection.values()[index]);
            //TODO informowanie hashmapy o zmianie pozycji
        }
    }

    private LinkedList<Animal> getStrongestAnimalsOnField(LinkedList<Animal> animalsOnField) {
        if(animalsOnField.size() == 0) {
            return animalsOnField;
        }
        LinkedList<Animal> strongestAnimals = new LinkedList<>();
        Collections.sort(animalsOnField);
        if(animalsOnField.size() == 1 || animalsOnField.get(0).energy > animalsOnField.get(1).energy) {
            strongestAnimals.add(animalsOnField.getFirst());
        }
        else {
            int i = 0;
            while(animalsOnField.get(i).energy == animalsOnField.get(i).energy) {
                strongestAnimals.add(animalsOnField.get(i));
                i++;
            }
        }
        return strongestAnimals;
    }

    public void eat() {
        for(Plant plant : this.plantsList) {
            LinkedList<Animal> animalsOnPlant = animals.get(plant.getPosition());
            if(animalsOnPlant == null || animalsOnPlant.size() == 0) {
                return;
            }
            LinkedList<Animal> strongestAnimals = getStrongestAnimalsOnField(animalsOnPlant);
            for(Animal animal : strongestAnimals) {
                animal.changeEnergy(plantEnergy / strongestAnimals.size());
            }

        }
    }
}
