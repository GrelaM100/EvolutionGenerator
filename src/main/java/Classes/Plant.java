package Classes;

import Interfaces.IMapElement;

public class Plant implements IMapElement {
    private Vector2d position;

    public Plant(Vector2d grassPosition) {
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
