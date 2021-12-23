package Classes;

import Enums.MapDirection;
import Enums.MoveDirection;
import Interfaces.IMapElement;
import Interfaces.IWorldMap;

import java.util.ArrayList;
import java.util.Comparator;

public class Animal implements IMapElement, Comparable<Animal> {
    public MapDirection direction;
    public int energy;
    public Vector2d position;
    public Genotype genes;
    public IWorldMap map;
    public ArrayList<Animal> children = new ArrayList<>();

    public Animal(IWorldMap map) {
        this.map = map;
    }

    public Animal(IWorldMap map, Vector2d initialPosition) {
        this(map);
        this.position = initialPosition;
        this.direction = this.direction.random();
    }

    public Animal(IWorldMap map, Vector2d position, int energy) {
        this(map, position);
        this.energy = energy;
    }

    @Override
    public Vector2d getPosition() {
        return this.position;
    }

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
            case BACKWARD:
                if(map.canMoveTo(this.position.subtract(direction.toUnitVector()))) {
                    this.position = position.subtract(direction.toUnitVector());
                }
        }
    }

    public void changeEnergy(int value) {
        this.energy += value;
        if(this.energy < 0) {
            this.energy = 0;
        }
    }

    public Animal copulate(Animal otherParent) {
        int childEnergy = (int) ((this.energy * 0.25) + (otherParent.energy * 0.25));
        this.changeEnergy((int) (-0.25 * this.energy));
        otherParent.changeEnergy((int) (-0.25 * this.energy));

        Animal child = new Animal(map, this.getPosition(), childEnergy);
        child.genes = new Genotype(this, otherParent);
        this.children.add(child);
        otherParent.children.add(child);

        return child;
    }

    @Override
    public int compareTo(Animal animal) {
        return animal.energy - this.energy;
    }
}
