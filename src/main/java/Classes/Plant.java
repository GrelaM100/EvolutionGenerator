package Classes;

import Interfaces.IMapElement;

public class Plant implements IMapElement {
    private Vector2d position;

    public Plant(Vector2d plantPosition) {
        this.position = plantPosition;
    }

    public String toString() {
        return this.position.toString();
    }

    @Override
    public Vector2d getPosition() {
        return this.position;
    }
}
