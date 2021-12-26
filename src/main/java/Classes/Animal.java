package Classes;

import Enums.MapDirection;
import Enums.MoveDirection;
import Interfaces.IMapElement;
import Interfaces.IWorldMap;

import java.util.ArrayList;
import java.util.LinkedList;

public class Animal implements IMapElement, Comparable<Animal> {
    private MapDirection direction;
    private int energy;
    private Vector2d position;
    private Genotype genes;
    private IWorldMap map;
    public ArrayList<Animal> children = new ArrayList<>();

    public Animal() {
        this.direction = MapDirection.random();
        this.genes = new Genotype();
    }
    public Animal(IWorldMap map) {
        this();
        this.map = map;
    }

    public Animal(IWorldMap map, Vector2d initialPosition, int energy) {
        this(map);
        this.position = initialPosition;
        this.energy = energy;
    }

    public Animal(IWorldMap map, Vector2d initialPosition, int energy, Genotype genotype) {
        this(map, initialPosition, energy);
        this.genes = genotype;
    }

    @Override
    public Vector2d getPosition() {
        return this.position;
    }

    public int getEnergy() {
        return this.energy;
    }

    public Genotype getGenes() {
        return this.genes;
    }

    public MapDirection getDirection() {return this.direction; }

    public void move(MoveDirection moveDirection) {
        switch(moveDirection) {
            case ROTATE:
                int rotations = this.genes.chooseRandomGen();
                for(int i = 0; i < rotations; i++) {
                    this.direction = this.direction.next();
                }
                break;
            case FORWARD:
                if(map.canMoveTo(this.position.add(direction.toUnitVector()))) {
                    this.position = position.add(direction.toUnitVector());
                }
                break;
            case BACKWARD:
                if(map.canMoveTo(this.position.subtract(direction.toUnitVector()))) {
                    this.position = position.subtract(direction.toUnitVector());
                }
                break;
        }
    }

    public void changeEnergy(int value) {
        this.energy += value;
        if(this.energy < 0) {
            this.energy = 0;
        }
    }

    public boolean canReproduce(int value) {
        return this.getEnergy() >= 0.5 * value;
    }

    public Animal reproduce(Animal otherParent) {
        int childEnergy = (int) ((this.energy * 0.25) + (otherParent.energy * 0.25));
        this.changeEnergy((int) (-0.25 * this.energy));
        otherParent.changeEnergy((int) (-0.25 * otherParent.energy));

        Genotype genes = new Genotype(this, otherParent);
        Animal child = new Animal(map, this.getPosition(), childEnergy, genes);
        this.children.add(child);
        otherParent.children.add(child);

        return child;
    }

    @Override
    public int compareTo(Animal animal) {
        return animal.energy - this.energy;
    }

    @Override
    public String toString() {
        return "direction: " + this.direction.toString() + ", position: " + this.position.toString() + ", energy: " +
                this.energy;
    }
}
