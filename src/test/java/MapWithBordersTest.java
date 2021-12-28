import Classes.Animal;
import Classes.MapWithBorders;
import Interfaces.IWorldMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MapWithBordersTest {
    IWorldMap map;

    @BeforeEach
    public void initialize() {
        map = new MapWithBorders(10, 10, 100, 10, 20, 0.5, false);
    }
}


