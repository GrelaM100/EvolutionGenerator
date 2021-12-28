import Classes.Animal;
import Classes.MapWithBorders;
import Classes.Vector2d;
import Enums.MoveDirection;
import Interfaces.IWorldMap;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AnimalTest {
    private final IWorldMap map = new MapWithBorders(5, 5, 100, 10, 20, 0.3);


    @Test
    void createAnimalPositionEnergy() {
        Vector2d position = new Vector2d(2, 2);
        int energy = 100;
        Animal animal = new Animal(map, position, 100);
        assertEquals(animal.getPosition(), position);
        assertEquals(animal.getEnergy(), energy);
        assertNotNull(animal.getGenotype());
    }

    @Test
    void moveAnimalRotate() {
        Animal animal = new Animal(map, new Vector2d(2, 2), 100);
        Vector2d oldPosition = animal.getPosition();
        animal.move(MoveDirection.ROTATE);
        assertEquals(animal.getPosition(), oldPosition);
    }

    @Test
    void moveAnimalForward() {
        Animal animal = new Animal(map, new Vector2d(2, 2), 100);
        Vector2d oldPosition = animal.getPosition();
        animal.move(MoveDirection.FORWARD);
        assertEquals(animal.getPosition(), oldPosition.add(animal.getDirection().toUnitVector()));
    }

    @Test
    void moveAnimalBackward() {
        Animal animal = new Animal(map, new Vector2d(2, 2), 100);
        Vector2d oldPosition = animal.getPosition();
        animal.move(MoveDirection.BACKWARD);
        assertEquals(animal.getPosition(), oldPosition.subtract(animal.getDirection().toUnitVector()));
    }

    @Test
    void changeAnimalEnergy() {
        Animal animal = new Animal(map, new Vector2d(2, 2), 100);
        int energy = animal.getEnergy();
        int value = 50;
        animal.changeEnergy(value);
        assertEquals(animal.getEnergy(), energy + value);
    }

    @Test
    void changeAnimalEnergyBelowZero() {
        Animal animal = new Animal(map, new Vector2d(2, 2), 100);
        int energy = animal.getEnergy();
        int value = -energy - 1;
        animal.changeEnergy(value);
        assertEquals(animal.getEnergy(), 0);
    }

    @Test
    void canReproduceAnimal() {
        Animal animal = new Animal(map, new Vector2d(2, 2), 100);
        int value = 200;
        assertTrue(animal.canReproduce(value));
    }

    @Test
    void reproduceAnimal() {
        Animal firstAnimal = new Animal(map, new Vector2d(2, 2), 120);
        Animal secondAnimal = new Animal(map, new Vector2d(2, 2), 100);
        Animal child = firstAnimal.reproduce(secondAnimal);
        assertEquals(firstAnimal.getEnergy(), 90);
        assertEquals(secondAnimal.getEnergy(), 75);
        assertEquals(child.getEnergy(), 55);
        assertEquals(firstAnimal.children.size(), 1);
        assertEquals(secondAnimal.children.size(), 1);
    }
}
