package Classes;

import Interfaces.IMapElement;

public class Plant implements IMapElement {
    private Vector2d position;

    public Plant(Vector2d plantPosition) {
        this.position = plantPosition;
    }

    public String toString() {
        return "*";
    }

    @Override
    public Vector2d getPosition() {
        return this.position;
    }

    @Override
    public String getColor() {return "rgb(144,238,144)";}
}
