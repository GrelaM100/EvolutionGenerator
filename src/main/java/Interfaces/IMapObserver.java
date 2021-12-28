package Interfaces;

import Classes.MapWithBorders;

public interface IMapObserver {
    void dayPassed(IWorldMap map);
    void animalDied(int age);
    void magicHappened();
}
