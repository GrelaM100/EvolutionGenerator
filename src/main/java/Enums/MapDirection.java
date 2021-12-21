package Enums;

import Classes.Vector2d;

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
            case SOUTH -> "South";
            case EAST -> "East";
            case WEST -> "West";
            case NORTHEAST -> "North-East";
            case NORTHWEST -> "North-West";
            case SOUTHEAST -> "South-East";
            case SOUTHWEST -> "South-West";
        };
    }

    public MapDirection next() {
        return switch(this) {
            case NORTH -> NORTHEAST;
            case SOUTH -> SOUTHWEST;
            case EAST -> SOUTHEAST;
            case WEST -> NORTHWEST;
            case NORTHEAST -> EAST;
            case NORTHWEST -> NORTH;
            case SOUTHEAST -> SOUTH;
            case SOUTHWEST -> WEST;
        };
    }

    public MapDirection previous() {
        return switch(this) {
            case NORTH -> NORTHWEST;
            case SOUTH -> SOUTHEAST;
            case EAST -> NORTHEAST;
            case WEST -> SOUTHWEST;
            case NORTHEAST -> NORTH;
            case NORTHWEST -> WEST;
            case SOUTHEAST -> EAST;
            case SOUTHWEST -> SOUTH;
        };
    }

    public Vector2d toUnitVector() {
        return switch(this) {
            case NORTH -> new Vector2d(0, 1);
            case SOUTH -> new Vector2d(0, -1);
            case EAST -> new Vector2d(1, 0);
            case WEST -> new Vector2d(-1, 0);
            case NORTHEAST -> new Vector2d(1, 1);
            case NORTHWEST -> new Vector2d(-1, 1);
            case SOUTHEAST -> new Vector2d(1, -1);
            case SOUTHWEST -> new Vector2d(-1, -1);
        };
    }
}
