package com.group1.frontend.components;

import com.group1.frontend.enums.BuildingType;

import java.util.List;

public class Corner {

    private double xCoordinate;
    private  double yCoordinate;

    private boolean isOccupied;
    private Player owner;
    private BuildingType buildingType;

    public Corner(double xCoordinate, double yCoordinate) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.isOccupied = false;
        this.owner = null;
        this.buildingType = null;
    }

    public Corner(double xCoordinate, double yCoordinate, boolean isOccupied, Player owner, BuildingType buildingType) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.isOccupied = isOccupied;
        this.owner = owner;
        this.buildingType = buildingType;
    }

    public void setCoordinates(double xCoordinate, double yCoordinate) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
    }

    public double getXCoordinate() {
        return xCoordinate;
    }

    public double getYCoordinate() {
        return yCoordinate;
    }

    public void setCoordinates(List<Double> coordinates) {
        this.xCoordinate = coordinates.get(0);
        this.yCoordinate = coordinates.get(1);
    }



    public void setIsOccupied(boolean isOccupied) {
        this.isOccupied = isOccupied;
    }

    public boolean getIsOccupied() {
        return isOccupied;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public Player getOwner() {
        return owner;
    }

    public void setBuildingType(BuildingType buildingType) {
        this.buildingType = buildingType;
    }

    public BuildingType getBuildingType() {
        return buildingType;
    }


}
