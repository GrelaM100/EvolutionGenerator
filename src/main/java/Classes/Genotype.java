package Classes;

import java.util.Arrays;
import java.util.Random;

public class Genotype {
    private int numberOfGens = 32;
    private int availableGens = 8;
    public int[] genes = new int[numberOfGens];

    public Genotype() {
        generateRandomGens();
    }

    public Genotype(Animal firstParent, Animal secondParent) {
        Animal strongerParent;
        Animal weakerParent;
        if(firstParent.energy >= secondParent.energy) {
            strongerParent = firstParent;
            weakerParent = secondParent;
        }
        else {
            strongerParent = secondParent;
            weakerParent = firstParent;
        }

        int dividePoint = (weakerParent.energy / (weakerParent.energy + strongerParent.energy)) - 1;
        if(dividePoint < 0) {
            dividePoint = 0;
        }

        Random rand = new Random();
        int sideOfStronger = rand.nextInt(2);
        if(sideOfStronger == 0) {
            this.genes = combineGens(strongerParent.genes, weakerParent.genes, dividePoint);
        }
        else {
            this.genes = combineGens(weakerParent.genes, strongerParent.genes, dividePoint);
        }
    }

    private int[] combineGens(Genotype firstGenotype, Genotype secondGenotype, int dividePoint) {
        int[] newGenotype = new int[numberOfGens];
        for(int i = 0; i < numberOfGens; i++) {
            if(i < dividePoint) {
                newGenotype[i] = firstGenotype.genes[i];
            }
            else {
                newGenotype[i] = secondGenotype.genes[i];
            }
        }

        return newGenotype;
    }

    private void generateRandomGens() {
        Random rand = new Random();
        for(int i = 0; i < numberOfGens; i++) {
            genes[i] = rand.nextInt(availableGens);
        }
    }

    public int chooseRandomGen() {
        Random rand = new Random();
        int randomGen = rand.nextInt(numberOfGens);
        return this.genes[randomGen];
    }
}
