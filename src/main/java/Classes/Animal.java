package Classes;

import Enums.MapDirection;
import Enums.MoveDirection;
import Interfaces.IMapElement;
import Interfaces.IWorldMap;

import java.util.ArrayList;

public class Animal implements IMapElement {
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
    }

    public Animal(IWorldMap map, Vector2d position, int energy) {
        this(map, position);
        this.energy = energy;
    }

    public void move(MoveDirection moveDirection) {
        switch(moveDirection) {
            case LEFT:
                this.direction = this.direction.previous();
                break;
            case RIGHT:
                this.direction = this.direction.next();
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
    @Override
    public Vector2d getPosition() {
        return this.position;
    }

    public void rotate() {
        int rotations = this.genes.chooseRandomGen();
        for(int i = 0; i < rotations; i++) {
            this.move(MoveDirection.RIGHT);
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

        return child;
    }
}
