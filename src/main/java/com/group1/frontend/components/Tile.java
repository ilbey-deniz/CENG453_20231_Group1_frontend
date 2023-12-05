package com.group1.frontend.components;

import com.group1.frontend.enums.TileType;

public class Tile {
    private int diceNumber;
    private TileType resourceType;

    // center coordinates of the tile
    private double xCoordinate;
    private double yCoordinate;

    public Tile(int diceNumber, TileType resourceType, double xCoordinate, double yCoordinate) {
        this.diceNumber = diceNumber;
        this.resourceType = resourceType;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
    }

    public void setDiceNumber(int diceNumber) {
        this.diceNumber = diceNumber;
    }

    public int getDiceNumber() {
        return diceNumber;
    }

    public void setResourceType(TileType resourceType) {
        this.resourceType = resourceType;
    }

    public TileType getResourceType() {
        return resourceType;
    }

    public void setXCoordinate(double xCoordinate) {
        this.xCoordinate = xCoordinate;
    }
    public double getxCoordinate() {
        return xCoordinate;
    }

    public void setYCoordinate(double yCoordinate) {
        this.yCoordinate = yCoordinate;
    }
    public double getyCoordinate() {
        return yCoordinate;
    }
}
