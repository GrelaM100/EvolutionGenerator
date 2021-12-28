package Classes;

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

public class Genotype {
    private final int numberOfGens = 32;
    private final int availableGens = 8;
    public int[] genes = new int[numberOfGens];

    public Genotype() {
        generateRandomGens();
    }

    public Genotype(Animal firstParent, Animal secondParent) {
        Animal strongerParent;
        Animal weakerParent;
        if(firstParent.getEnergy() >= secondParent.getEnergy()) {
            strongerParent = firstParent;
            weakerParent = secondParent;
        }
        else {
            strongerParent = secondParent;
            weakerParent = firstParent;
        }

        int dividePoint = (weakerParent.getEnergy() / (weakerParent.getEnergy() + strongerParent.getEnergy())) - 1;
        if(dividePoint < 0) {
            dividePoint = 0;
        }

        Random rand = new Random();
        int sideOfStronger = rand.nextInt(2);
        if(sideOfStronger == 0) {
            this.genes = combineGens(strongerParent.getGenotype(), weakerParent.getGenotype(), dividePoint);
        }
        else {
            this.genes = combineGens(weakerParent.getGenotype(), strongerParent.getGenotype(), dividePoint);
        }
    }

    private int[] combineGens(Genotype firstGenotype, Genotype secondGenotype, int dividePoint) {
        int[] newGenotype = new int[this.numberOfGens];
        for(int i = 0; i < this.numberOfGens; i++) {
            if(i < dividePoint) {
                newGenotype[i] = firstGenotype.genes[i];
            }
            else {
                newGenotype[i] = secondGenotype.genes[i];
            }
        }

        Arrays.sort(newGenotype);

        return newGenotype;
    }

    private void generateRandomGens() {
        Random rand = new Random();
        for(int i = 0; i < this.numberOfGens; i++) {
            this.genes[i] = rand.nextInt(this.availableGens);
        }
        Arrays.sort(this.genes);
    }

    public int chooseRandomGen() {
        Random rand = new Random();
        int randomGen = rand.nextInt(this.numberOfGens);
        return this.genes[randomGen];
    }

    public boolean equals(Object other) {
        if(this == other)
            return true;
        if(!(other instanceof Genotype that))
            return false;
        for(int i = 0; i < this.numberOfGens; i++) {
            if(this.genes[i] != that.genes[i]) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.genes);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int gene : this.genes) {
            sb.append(gene);
            sb.append(" ");
        }

        return sb.toString();
    }
}
