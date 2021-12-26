package Classes;

import Interfaces.IWorldMap;

import java.util.LinkedList;

public class World {
    public static void main(String[] args) {
        MapWithBorders map = new MapWithBorders(5, 5, 100, 10, 20,
                (double) 9/25);

        SimulationEngine engine = new SimulationEngine(map, 4);
        System.out.println("Test");
    }
}
