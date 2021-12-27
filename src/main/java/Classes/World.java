package Classes;

import Gui.App;
import Interfaces.IWorldMap;
import javafx.application.Application;

import java.util.LinkedList;

public class World {
    public static void main(String[] args) {
//        MapWithBorders map = new MapWithBorders(5, 5, 100, 10, 20,
//                (double) 9/25);
//
//        SimulationEngine engine = new SimulationEngine(map, 4);
//        for(int i = 0; i < 15; i++) {
//            map.createPlants();
//        }
//
//        System.out.println("Test");
        Application.launch(App.class, args);
    }
}
