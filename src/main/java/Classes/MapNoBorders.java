package Classes;

import Abstract.AbstractWorldMap;

public class MapNoBorders extends AbstractWorldMap {
    

    public MapNoBorders(int width, int height, int startEnergy, int moveEnergy, int plantEnergy, double jungleRatio,
                        boolean isMagic) {
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

}