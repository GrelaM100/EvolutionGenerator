import Classes.Vector2d;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class Vector2dTest {
    @Test
    void equalsSameObjectReturnTrue() {
        Vector2d vector2d = new Vector2d(1, 1);
        assertEquals(vector2d, vector2d);
    }

    @Test
    void equalsOtherTypeObjectReturnFalse() {
        Vector2d vector2d = new Vector2d(1, 1);
        Object other = new Object();
        assertNotEquals(vector2d, other);
    }

    @Test
    void equalsOtherVectorSamePositionReturnTrue() {
        Vector2d vector2d = new Vector2d(1, 1);
        Vector2d other = new Vector2d(1, 1);
        assertEquals(vector2d, other);
    }

    @Test
    void toStringReturnValidString() {
        Vector2d vector2d = new Vector2d(1, 1);
        assertEquals(vector2d.toString(), "(1,1)");
    }

    @Test
    void precedesGivenBothCoordinatesSmallerReturnTrue() {
        Vector2d vector2d = new Vector2d(1, 1);
        Vector2d other = new Vector2d(2, 2);
        assertTrue(vector2d.precedes(other));
    }

    @Test
    void precedesGivenOnlyXCoordinateSmallerReturnFalse() {
        Vector2d vector2d = new Vector2d(1, 3);
        Vector2d other = new Vector2d(2, 2);
        assertFalse(vector2d.precedes(other));
    }

    @Test
    void precedesGivenOnlyYCoordinateSmallerReturnFalse() {
        Vector2d vector2d = new Vector2d(3, 1);
        Vector2d other = new Vector2d(2, 2);
        assertFalse(vector2d.precedes(other));
    }

    @Test
    void precedesGiveBothCoordinatesGreaterReturnFalse() {
        Vector2d vector2d = new Vector2d(2, 2);
        Vector2d other = new Vector2d(1, 1);
        assertFalse(vector2d.precedes(other));
    }

    @Test
    void followsGivenBothCoordinatesGreaterReturnTrue() {
        Vector2d vector2d = new Vector2d(2, 2);
        Vector2d other = new Vector2d(1, 1);
        assertTrue(vector2d.follows(other));
    }

    @Test
    void followsGivenOnlyXCoordinateGreaterReturnFalse() {
        Vector2d vector2d = new Vector2d(3, 1);
        Vector2d other = new Vector2d(2, 2);
        assertFalse(vector2d.follows(other));
    }

    @Test
    void followsGivenOnlyYCoordinateGraterReturnFalse() {
        Vector2d vector2d = new Vector2d(1, 3);
        Vector2d other = new Vector2d(2, 2);
        assertFalse(vector2d.follows(other));
    }

    @Test
    void followsGiveBothCoordinatesSmallerReturnFalse() {
        Vector2d vector2d = new Vector2d(2, 2);
        Vector2d other = new Vector2d(3, 3);
        assertFalse(vector2d.follows(other));
    }

    @Test
    void upperRightReturnUpperRightPoint() {
        Vector2d vector2d = new Vector2d(1, 2);
        Vector2d other = new Vector2d(2, 1);
        Vector2d upperRight = new Vector2d(2, 2);
        assertEquals(vector2d.upperRight(other), upperRight);
    }

    @Test
    void lowerLeftReturnLowerLeftPoint() {
        Vector2d vector2d = new Vector2d(1, 2);
        Vector2d other = new Vector2d(2, 1);
        Vector2d lowerLeft = new Vector2d(1, 1);
        assertEquals(vector2d.lowerLeft(other), lowerLeft);
    }

    @Test
    void addReturnValidPoint() {
        Vector2d vector2d = new Vector2d(1, 2);
        Vector2d other = new Vector2d(2, 1);
        Vector2d add = new Vector2d(3, 3);
        assertEquals(vector2d.add(other), add);
    }

    @Test
    void subtractReturnValidPoint() {
        Vector2d vector2d = new Vector2d(1, 2);
        Vector2d other = new Vector2d(2, 1);
        Vector2d subtract = new Vector2d(-1, 1);
        assertEquals(vector2d.subtract(other), subtract);
    }

    @Test
    void oppositeReturnOppositePoint() {
        Vector2d vector2d = new Vector2d(1, 1);
        Vector2d opposite = new Vector2d(-1, -1);
        assertEquals(vector2d.opposite(), opposite);
    }
}
