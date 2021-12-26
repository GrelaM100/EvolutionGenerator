package Classes;

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
    public Map<Vector2d, LinkedList<Animal>> animals = new HashMap<>();
    public LinkedList<Animal> animalsList = new LinkedList<>();
    public Map<Vector2d, Plant> plants = new HashMap<>();
    public LinkedList<Plant> plantsList = new LinkedList<>();
    public Vector2d jungleLeftLower;
    public Vector2d jungleRightUpper;


    public MapWithBorders(int width, int height, int startEnergy, int moveEnergy, int plantEnergy, double jungleRatio) {
        this.width = width;
        this.height = height;
        this.startEnergy = startEnergy;
        this.moveEnergy = moveEnergy;
        this.plantEnergy = plantEnergy;
        this.jungleRatio = jungleRatio;
        this.calculateJungleCorners();
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return position.x < width && position.x >= 0 && position.y < height && position.y >= 0;
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
            this.addAnimal(position, (Animal) mapElement);
            this.animalsList.add((Animal) mapElement);
        }
        return true;
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        if(this.animals.get(position) != null) {
            return this.animals.get(position).size() > 0;
        }
        return false;
    }

    @Override
    public Object objectAt(Vector2d position) {
        LinkedList<Animal> tempList = this.animals.get(position);
        if(tempList == null || tempList.size() == 0) return this.plants.get(position);
        else return tempList.getFirst();
    }

    private void calculateJungleCorners() {
        Vector2d middlePoint = new Vector2d(this.width / 2, this.height / 2);
        int jungleWidth = (int) (Math.sqrt(this.jungleRatio) * this.width);
        int jungleHeight = (int) (Math.sqrt(this.jungleRatio) * this.height);
        this.jungleLeftLower = middlePoint.subtract(new Vector2d(jungleWidth / 2, jungleHeight / 2));
        this.jungleRightUpper = middlePoint.add(new Vector2d(jungleWidth / 2, jungleHeight / 2));
    }

    private void addAnimal(Vector2d position, Animal animalToAdd) {
        LinkedList<Animal> tempList = this.animals.get(position);
        if(tempList == null) {
            tempList = new LinkedList<>();
        }
        tempList.add(animalToAdd);
        this.animals.put(position, tempList);
    }

    private void removeAnimal(Animal animalToRemove) {
        Vector2d position = animalToRemove.getPosition();
        this.animals.get(position).remove(animalToRemove);
        if(this.animals.get(position).size() == 0) {
            this.animals.remove(position);
        }
    }

    public void removeDeadAnimals() {
        LinkedList<Animal> tempList = (LinkedList<Animal>) this.animalsList.clone();
        for(Animal animal : tempList) {
            if(animal.getEnergy() == 0) {
                this.removeAnimal(animal);
                this.animalsList.remove(animal);
            }
        }
    }

    public void moveAnimals() {
        for(Animal animal : this.animalsList) {
            Random rand = new Random();
            int index = rand.nextInt(MoveDirection.values().length);
            this.removeAnimal(animal);
            animal.move(MoveDirection.values()[index]);
            animal.changeEnergy(-this.moveEnergy);
            this.addAnimal(animal.getPosition(), animal);
        }
    }

    private LinkedList<Animal> getStrongestAnimalsOnField(LinkedList<Animal> animalsOnField, int numberOfStrongest) {
        if(animalsOnField.size() == 0) {
            return animalsOnField;
        }
        LinkedList<Animal> strongestAnimals = new LinkedList<>();
        Collections.sort(animalsOnField);
        if(animalsOnField.size() == 1) {
            strongestAnimals.add(animalsOnField.getFirst());
        }
        else {
            int j = 0;
            for(int i = 0; i < numberOfStrongest; i++) {
                strongestAnimals.add(animalsOnField.get(j));
                if(j + 1 < animalsOnField.size() - 1) {
                    if(animalsOnField.get(j).getEnergy() == animalsOnField.get(j + 1).getEnergy()) {
                        i--;
                    }
                }
                j++;
            }
        }

        return strongestAnimals;
    }

    public void eat() {
        for(Plant plant : this.plantsList) {
            LinkedList<Animal> animalsOnPlant = animals.get(plant.getPosition());
            if(animalsOnPlant == null || animalsOnPlant.size() == 0) {
                continue;
            }
            LinkedList<Animal> strongestAnimals = this.getStrongestAnimalsOnField(animalsOnPlant, 1);
            for(Animal animal : strongestAnimals) {
                animal.changeEnergy(this.plantEnergy / strongestAnimals.size());
            }
        }
    }

    public void reproduceAnimals() {
        for(LinkedList<Animal> animalsOnField : this.animals.values()) {
            if(animalsOnField.size() > 1) {
                LinkedList<Animal> strongestAnimals = this.getStrongestAnimalsOnField(animalsOnField, 2);
                Animal firstParent = strongestAnimals.getFirst();
                Animal secondParent = strongestAnimals.get(1);
                if(firstParent.canReproduce(this.startEnergy) && secondParent.canReproduce(this.startEnergy)) {
                    Animal child = firstParent.reproduce(secondParent);
                    this.addAnimal(firstParent.getPosition(), child);
                    this.animalsList.add(child);
                }
            }
        }
    }

    private boolean isInJungle(Vector2d point) {
        return point.precedes(this.jungleRightUpper) && point.follows(this.jungleLeftLower);
    }

    private void createPlantOnRandomField(Vector2d lowerLeft, Vector2d upperRight, boolean isJungle) {
        Random rand = new Random();
        int counter = 0;
        int area = (upperRight.x - lowerLeft.x + 1) * (upperRight.y - lowerLeft.y + 1);
        while(counter < 2 * area) {
            int randomX = rand.nextInt(upperRight.x - lowerLeft.x + 1) + lowerLeft.x;
            int randomY = rand.nextInt(upperRight.y - lowerLeft.y + 1) + lowerLeft.y;
            Vector2d randomPosition = new Vector2d(randomX, randomY);
            if(!isJungle) {
                if(this.isInJungle(randomPosition)) {
                    counter++;
                    continue;
                }
            }
            Plant plant = new Plant(randomPosition);
            if(this.place(plant)) {
                return;
            }
            counter++;
        }
    }

    public void createPlants() {
        this.createPlantOnRandomField(this.jungleLeftLower, this.jungleRightUpper, true);
        this.createPlantOnRandomField(new Vector2d(0, 0), new Vector2d(this.width - 1, this.height - 1), false);
    }
}
