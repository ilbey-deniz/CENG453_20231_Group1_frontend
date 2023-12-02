package com.group1.frontend.components;

import com.group1.frontend.enums.ResourceType;

public class Tile {
    private int diceNumber;
    private ResourceType resourceType;

    // center coordinates of the tile
    private double xCoordinate;
    private double yCoordinate;

    public Tile(int diceNumber, ResourceType resourceType, double xCoordinate, double yCoordinate) {
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

    public void setResourceType(ResourceType resourceType) {
        this.resourceType = resourceType;
    }

    public ResourceType getResourceType() {
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
