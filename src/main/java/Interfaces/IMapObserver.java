package Interfaces;

import Classes.MapWithBorders;

public interface IMapObserver {
    void dayPassed(MapWithBorders map);
    void animalDied(int age);
}
