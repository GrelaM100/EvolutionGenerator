package Enums;

import Classes.Vector2d;

import java.util.Map;
import java.util.Random;

public enum MapDirection {
    NORTH,
    SOUTH,
    EAST,
    WEST,
    NORTHEAST,
    NORTHWEST,
    SOUTHEAST,
    SOUTHWEST;

    public String toString(){
        return switch(this) {
            case NORTH -> "North";
            case NORTHEAST -> "North-East";
            case EAST -> "East";
            case SOUTHEAST -> "South-East";
            case SOUTH -> "South";
            case SOUTHWEST -> "South-West";
            case WEST -> "West";
            case NORTHWEST -> "North-West";
        };
    }

    public MapDirection next() {
        return switch(this) {
            case NORTH -> NORTHEAST;
            case NORTHEAST -> EAST;
            case EAST -> SOUTHEAST;
            case SOUTHEAST -> SOUTH;
            case SOUTH -> SOUTHWEST;
            case SOUTHWEST -> WEST;
            case WEST -> NORTHWEST;
            case NORTHWEST -> NORTH;
        };
    }

    public MapDirection previous() {
        return switch(this) {
            case NORTH -> NORTHWEST;
            case NORTHEAST -> NORTH;
            case EAST -> NORTHEAST;
            case SOUTHEAST -> EAST;
            case SOUTH -> SOUTHEAST;
            case SOUTHWEST -> SOUTH;
            case WEST -> SOUTHWEST;
            case NORTHWEST -> WEST;
        };
    }

    public Vector2d toUnitVector() {
        return switch(this) {
            case NORTH -> new Vector2d(0, 1);
            case NORTHEAST -> new Vector2d(1, 1);
            case EAST -> new Vector2d(1, 0);
            case SOUTHEAST -> new Vector2d(1, -1);
            case SOUTH -> new Vector2d(0, -1);
            case SOUTHWEST -> new Vector2d(-1, -1);
            case WEST -> new Vector2d(-1, 0);
            case NORTHWEST -> new Vector2d(-1, 1);
        };
    }

    public MapDirection random() {
        Random rand = new Random();
        int index = rand.nextInt(MapDirection.values().length);
        return MapDirection.values()[index];
    }
}
