package Classes;

import Interfaces.IMapObserver;

public class MapStatistics implements IMapObserver{
    public int day;
    public int numberOfAnimals;
    public int numberOfPlants;
    public float averageEnergy;
    public float averageLifeLength;
    public float averageNumberOfChildren;
    private int numberOfDeadAnimals;
    private int sumOfAges;

    public MapStatistics(int numberOfAnimals, int numberOfPlants, int startEnergy) {
        this.day = 0;
        this.numberOfAnimals = numberOfAnimals;
        this.numberOfPlants = numberOfPlants;
        this.averageEnergy = startEnergy;
        this.averageLifeLength = 0;
        this.averageNumberOfChildren = 0;
        this.numberOfDeadAnimals = 0;
        this.sumOfAges = 0;
    }

    private void calculateStats(MapWithBorders map) {
        this.day++;
        this.numberOfAnimals = map.animalsList.size();
        this.numberOfPlants = map.plantsList.size();
        int energy = 0, numberOfChildren = 0;
        for(Animal animal : map.animalsList) {
            if(animal.getEnergy() > 0) {
                numberOfChildren += animal.children.size();
                energy += animal.getEnergy();
            }
        }

        if(this.numberOfAnimals > 0) {
            this.averageEnergy = (float) energy / this.numberOfAnimals;
            this.averageNumberOfChildren = (float) numberOfChildren / this.numberOfAnimals;
        }
        else {
            this.averageEnergy = 0;
            this.averageNumberOfChildren = 0;
        }
    }

    @Override
    public String toString() {
        return "Dzień :" + this.day +
                "\nIlość zwierząt: " + this.numberOfAnimals +
                "\nIlość roślin: " + this.numberOfPlants +
                "\nŚrednia energia: " + this.averageEnergy +
                "\nŚredni wiek: " + this.averageLifeLength +
                "\nŚrednia ilość dzieci: " + this.averageNumberOfChildren;
    }

    @Override
    public void dayPassed(MapWithBorders map) {
        this.calculateStats(map);
    }

    @Override
    public void animalDied(int age) {
        this.numberOfDeadAnimals++;
        this.sumOfAges += age;
        this.averageLifeLength = (float) this.sumOfAges / this.numberOfDeadAnimals;
    }
}
