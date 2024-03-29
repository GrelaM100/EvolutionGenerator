package Classes;

import Enums.MapDirection;
import Enums.MoveDirection;
import Interfaces.IMapElement;
import Interfaces.IWorldMap;

import java.util.ArrayList;

public class Animal implements IMapElement, Comparable<Animal> {
    private MapDirection direction;
    private int energy;
    private Vector2d position;
    private Genotype genotype;
    private IWorldMap map;
    private int age = 0;
    private int dayOfDeath = 0;
    public ArrayList<Animal> children = new ArrayList<>();
    public ArrayList<Animal> trackedChildren;

    public Animal() {
        this.direction = MapDirection.random();
        this.genotype = new Genotype();
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
        this.genotype = genotype;
    }

    public int getAge() {return this.age;}

    @Override
    public Vector2d getPosition() {
        return this.position;
    }

    @Override
    public String getColor() {
        int startEnergy = this.map.getStartEnergy();
        if(this.energy >= startEnergy) {
            return "rgb(255,32,0)";
        }
        else if(this.energy >= 0.75 * startEnergy) {
            return "rgb(255,103,0)";
        }
        else if(this.energy >= 0.5 * startEnergy) {
            return "rgb(255,146,72)";
        }
        else if(this.energy >= 0.25 * startEnergy) {
            return "rgb(255,179,138)";
        }
        else if(this.energy > 0) {
            return "rgb(255,215,181)";
        }
        else {
            return "rgb(255,255,255)";
        }
    }

    public int getEnergy() {
        return this.energy;
    }

    public Genotype getGenotype() {
        return this.genotype;
    }

    public MapDirection getDirection() {return this.direction; }

    public void setDayOfDeath(int dayOfDeath) {
        this.dayOfDeath = dayOfDeath;
    }

    public int getDayOfDeath() {
        return this.dayOfDeath;
    }

    public void setPosition(Vector2d position) {
        this.position = position;
    }

    public void move(MoveDirection moveDirection) {
        Vector2d newPosition;
        switch (moveDirection) {
            case ROTATE -> {
                int rotations = this.genotype.chooseRandomGen();
                for (int i = 0; i < rotations; i++) {
                    this.direction = this.direction.next();
                }
            }
            case FORWARD -> {
                newPosition = this.position.add(direction.toUnitVector());
                if (map.canMoveTo(newPosition)) {
                    if (this.map instanceof MapNoBorders) {
                        newPosition = teleportWithNoBorder(newPosition);
                    }
                    this.position = newPosition;
                }
            }
            case BACKWARD -> {
                newPosition = this.position.subtract(direction.toUnitVector());
                if (map.canMoveTo(newPosition)) {
                    if (this.map instanceof MapNoBorders) {
                        newPosition = teleportWithNoBorder(newPosition);
                    }
                    this.position = newPosition;
                }
            }
        }
    }

    private Vector2d teleportWithNoBorder(Vector2d position) {
        int x = position.x;
        int y = position.y;

        if(position.x < 0) {
            x = this.map.getWidth() - 1;
        }
        if(position.x >= this.map.getWidth()) {
            x = 0;
        }
        if(position.y < 0) {
            y = this.map.getHeight() - 1;
        }
        if(position.y >= this.map.getHeight()) {
            y = 0;
        }

        return new Vector2d(x, y);
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
        if(this.trackedChildren != null) {
            this.trackedChildren.add(child);
        }
        otherParent.children.add(child);

        return child;
    }

    public void track() {
        this.trackedChildren = new ArrayList<>();
    }

    public int calculateDescendant(Animal currentAnimal) {
        if(currentAnimal.children.size() == 0) return 0;
        else {
            int sum;
            if(currentAnimal == this) {
                sum = currentAnimal.trackedChildren.size();
            }
            else {
                sum = currentAnimal.children.size();
            }
            for(Animal child : currentAnimal.children) {
                sum += calculateDescendant(child);
            }
            return sum;
        }
    }

    public void increaseAge() {
        this.age++;
    }

    @Override
    public int compareTo(Animal animal) {
        return animal.energy - this.energy;
    }

    @Override
    public String toString() {
        return "A";
    }
}
