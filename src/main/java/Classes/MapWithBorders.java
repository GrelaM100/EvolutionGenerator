package Classes;

import Abstract.AbstractWorldMap;


public class MapWithBorders extends AbstractWorldMap {


    public MapWithBorders(int width, int height, int startEnergy, int moveEnergy, int plantEnergy,
                          double jungleRatio, boolean isMagic) {
        this.width = width;
        this.height = height;
        this.startEnergy = startEnergy;
        this.moveEnergy = moveEnergy;
        this.plantEnergy = plantEnergy;
        this.jungleRatio = jungleRatio;
        this.isMagic = isMagic;
        this.calculateJungleCorners();
        this.createPlants();
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return position.x < width && position.x >= 0 && position.y < height && position.y >= 0;
    }

}
