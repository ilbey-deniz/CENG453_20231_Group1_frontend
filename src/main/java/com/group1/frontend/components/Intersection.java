package com.group1.frontend.components;

import com.group1.frontend.enums.BuildingType;

public class Intersection {
    private boolean isOccupied;
    private Player owner;

    private BuildingType buildingType;
    private int xCoordinate;
    private int yCoordinate;

    public Intersection(int xCoordinate, int yCoordinate) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.isOccupied = false;
        this.owner = null;
    }

    public Intersection(int xCoordinate, int yCoordinate, boolean isOccupied, Player owner) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.isOccupied = isOccupied;
        this.owner = owner;
    }
}
