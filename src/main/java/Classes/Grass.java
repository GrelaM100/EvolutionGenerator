package Classes;

import Interfaces.IMapElement;

public class Grass implements IMapElement {
    private Vector2d position;

    public Grass(Vector2d grassPosition) {
        this.position = grassPosition;
    }

    public String toString() {
        return "*";
    }


    @Override
    public Vector2d getPosition() {
        return this.position;
    }
}
