package Classes;

import Interfaces.IMapObserver;
import Interfaces.IWorldMap;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class MapStatistics implements IMapObserver{
    public int day;
    public int numberOfAnimals;
    public int numberOfPlants;
    public float averageEnergy;
    public float averageLifeLength;
    public float averageNumberOfChildren;
    public Genotype dominantGenotype;
    private int numberOfDeadAnimals;
    private int sumOfAges;
    private final ArrayList<float[]> statisticsHistory = new ArrayList<>();

    public MapStatistics(int numberOfAnimals, int numberOfPlants, int startEnergy) {
        this.day = 0;
        this.numberOfAnimals = numberOfAnimals;
        this.numberOfPlants = numberOfPlants;
        this.averageEnergy = startEnergy;
        this.averageNumberOfChildren = 0;
        this.numberOfDeadAnimals = 0;
        this.sumOfAges = 0;
        this.averageLifeLength = 0;
        this.statisticsHistory.add(new float[] {0, this.numberOfDeadAnimals, this.numberOfPlants,
                startEnergy, 0, averageNumberOfChildren});
    }

    private void calculateStats(IWorldMap map) {
        this.day++;
        this.numberOfAnimals = map.getAnimalsList().size();
        this.numberOfPlants = map.getPlantsList().size();
        int energy = 0, numberOfChildren = 0;
        for(Animal animal : map.getAnimalsList()) {
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

        this.getDominantGenotype(map.getAnimalsList());
        float[] dayStatistics = new float[]{this.day, this.numberOfAnimals, this.numberOfPlants, this.averageEnergy,
                this.averageLifeLength, this.averageNumberOfChildren};

        this.statisticsHistory.add(dayStatistics);
    }

    private void getDominantGenotype(LinkedList<Animal> animals) {
        int frequency = 0;
        List<Genotype> genotypesToCheck = new LinkedList<>();
        for(Animal animal : animals) {
            genotypesToCheck.add(animal.getGenotype());
        }
        for(Genotype genotype : genotypesToCheck) {
            int currentFrequency = Collections.frequency(genotypesToCheck, genotype);
            if(currentFrequency > frequency) {
                this.dominantGenotype = genotype;
                frequency = currentFrequency;
            }
        }

    }

    public void saveToCSV(String filename) {
        try(PrintWriter writer = new PrintWriter(filename)) {
            StringBuilder sb = new StringBuilder();
            String columnNames = "Dzień,Liczba zwierząt,Liczba roślin,Średnia energia," +
                    "Średnia długość życia,Średnia ilość dzieci\n";
            sb.append(columnNames);
            for(float[] dayData : this.statisticsHistory) {
                for(int i = 0; i < dayData.length; i++) {
                    if(i < 3) {
                        sb.append((int) dayData[i]);
                    }
                    else {
                        sb.append(dayData[i]);
                    }
                    sb.append(',');
                }
                sb.append('\n');
            }
            writer.write(sb.toString());
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
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
    public void dayPassed(IWorldMap map) {
        this.calculateStats(map);
    }

    @Override
    public void animalDied(int age) {
        this.numberOfDeadAnimals++;
        this.sumOfAges += age;
        this.averageLifeLength = (float) this.sumOfAges / this.numberOfDeadAnimals;
    }

    @Override
    public void magicHappened() {

    }
}
