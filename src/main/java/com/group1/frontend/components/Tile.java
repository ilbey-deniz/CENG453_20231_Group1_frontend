package com.group1.frontend.components;

import com.group1.frontend.enums.TileType;

public class Tile {
    private int diceNumber;
    private TileType tileType;

    // center coordinates of the tile
    private double xCoordinate;
    private double yCoordinate;

    public Tile(int diceNumber, TileType tileType, double xCoordinate, double yCoordinate) {
        this.diceNumber = diceNumber;
        this.tileType = tileType;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
    }

    public void setDiceNumber(int diceNumber) {
        this.diceNumber = diceNumber;
    }

    public int getDiceNumber() {
        return diceNumber;
    }

    public void setResourceType(TileType tileType) {
        this.tileType = tileType;
    }

    public TileType getTileType() {
        return tileType;
    }

    public void setXCoordinate(double xCoordinate) {
        this.xCoordinate = xCoordinate;
    }
    public double getXCoordinate() {
        return xCoordinate;
    }

    public void setYCoordinate(double yCoordinate) {
        this.yCoordinate = yCoordinate;
    }
    public double getYCoordinate() {
        return yCoordinate;
    }
}
