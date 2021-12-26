package Interfaces;

import Classes.Vector2d;

public interface IWorldMap {
    boolean canMoveTo(Vector2d position);
    boolean place(IMapElement mapElement);
    boolean isOccupied(Vector2d position);
    Object objectAt(Vector2d position);
}
