import Classes.Vector2d;
import Enums.MapDirection;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MapDirectionTest {
    @Test
    void nextNorthReturnNorthEast() {
        assertEquals(MapDirection.NORTH.next(),MapDirection.NORTHEAST);
    }
    @Test
    void nextNorthEastReturnEast() {
        assertEquals(MapDirection.NORTHEAST.next(), MapDirection.EAST);
    }
    @Test
    void nextEastReturnSouthEast() {
        assertEquals(MapDirection.EAST.next(), MapDirection.SOUTHEAST);
    }
    @Test
    void nextSouthEastReturnSouth() {
        assertEquals(MapDirection.SOUTHEAST.next(), MapDirection.SOUTH);
    }
    @Test
    void nextSouthReturnSouthWest() {
        assertEquals(MapDirection.SOUTH.next(), MapDirection.SOUTHWEST);
    }
    @Test
    void nextSouthWestReturnWest() {
        assertEquals(MapDirection.SOUTHWEST.next(), MapDirection.WEST);
    }
    @Test
    void nextWestReturnNorthWest() {
        assertEquals(MapDirection.WEST.next(), MapDirection.NORTHWEST);
    }
    @Test
    void nextNorthWestReturnNorth() {
        assertEquals(MapDirection.NORTHWEST.next(), MapDirection.NORTH);
    }
    @Test
    void previousNorthReturnNorthWest() {
        assertEquals(MapDirection.NORTH.previous(), MapDirection.NORTHWEST);
    }
    @Test
    void previousNorthWestReturnWest() {
        assertEquals(MapDirection.NORTHWEST.previous(), MapDirection.WEST);
    }
    @Test
    void previousWestReturnSouthWest() {
        assertEquals(MapDirection.WEST.previous(), MapDirection.SOUTHWEST);
    }
    @Test
    void previousSouthWestReturnSouth() {
        assertEquals(MapDirection.SOUTHWEST.previous(), MapDirection.SOUTH);
    }
    @Test
    void previousSouthReturnSouthEast() {
        assertEquals(MapDirection.SOUTH.previous(), MapDirection.SOUTHEAST);
    }
    @Test
    void previousSouthEastReturnEast() {
        assertEquals(MapDirection.SOUTHEAST.previous(), MapDirection.EAST);
    }
    @Test
    void previousEastReturnNorthEast() {
        assertEquals(MapDirection.EAST.previous(), MapDirection.NORTHEAST);
    }
    @Test
    void previousNorthEastReturnNorth() {
        assertEquals(MapDirection.NORTHEAST.previous(), MapDirection.NORTH);
    }

    @Test
    void toUnitVectorNorth() {
        assertEquals(MapDirection.NORTH.toUnitVector(), new Vector2d(0, 1));
    }
    @Test
    void toUnitVectorNorthEast() {
        assertEquals(MapDirection.NORTHEAST.toUnitVector(), new Vector2d(1, 1));
    }
    @Test
    void toUnitVectorEast() {
        assertEquals(MapDirection.EAST.toUnitVector(), new Vector2d(1, 0));
    }
    @Test
    void toUnitVectorSouthEast() {
        assertEquals(MapDirection.SOUTHEAST.toUnitVector(), new Vector2d(1, -1));
    }
    @Test
    void toUnitVectorSouth() {
        assertEquals(MapDirection.SOUTH.toUnitVector(), new Vector2d(0, -1));
    }
    @Test
    void toUnitVectorSouthWest() {
        assertEquals(MapDirection.SOUTHWEST.toUnitVector(), new Vector2d(-1, -1));
    }
    @Test
    void toUnitVectorWest() {
        assertEquals(MapDirection.WEST.toUnitVector(), new Vector2d(-1, 0));
    }
    @Test
    void toUnitVectorNorthWest() {
        assertEquals(MapDirection.NORTHWEST.toUnitVector(), new Vector2d(-1, 1));
    }
    @Test
    void toStringNorth() {
        assertEquals(MapDirection.NORTH.toString(), "North");
    }
    @Test
    void toStringNorthEast() {
        assertEquals(MapDirection.NORTHEAST.toString(), "North-East");
    }
    @Test
    void toStringEast() {

        assertEquals(MapDirection.EAST.toString(), "East");
    }
    @Test
    void toStringSouthEast() {
        assertEquals(MapDirection.SOUTHEAST.toString(), "South-East");
    }
    @Test
    void toStringSouth() {
        assertEquals(MapDirection.SOUTH.toString(), "South");
    }
    @Test
    void toStringSouthWest() {
        assertEquals(MapDirection.SOUTHWEST.toString(), "South-West");
    }
    @Test
    void toStringWest() {
        assertEquals(MapDirection.WEST.toString(), "West");
    }
    @Test
    void toStringNorthWest() {
        assertEquals(MapDirection.NORTHWEST.toString(), "North-West");
    }
}