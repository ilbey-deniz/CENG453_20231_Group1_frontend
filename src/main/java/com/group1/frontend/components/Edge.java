package com.group1.frontend.components;

public class Edge {
    private double firstXCoordinate;
    private double firstYCoordinate;

    private double secondXCoordinate;
    private double secondYCoordinate;

    private boolean isOccupied;
    private Player owner;

    public Edge(double firstXCoordinate, double firstYCoordinate, double secondXCoordinate, double secondYCoordinate) {
        this.firstXCoordinate = firstXCoordinate;
        this.firstYCoordinate = firstYCoordinate;
        this.secondXCoordinate = secondXCoordinate;
        this.secondYCoordinate = secondYCoordinate;
        this.isOccupied = false;
        this.owner = null;
    }

    public Edge(double firstXCoordinate, double firstYCoordinate, double secondXCoordinate, double secondYCoordinate,
                boolean isOccupied, Player owner) {
        this.firstXCoordinate = firstXCoordinate;
        this.firstYCoordinate = firstYCoordinate;
        this.secondXCoordinate = secondXCoordinate;
        this.secondYCoordinate = secondYCoordinate;
        this.isOccupied = isOccupied;
        this.owner = owner;
    }

    public void setFirstXCoordinate(double firstXCoordinate) {
        this.firstXCoordinate = firstXCoordinate;
    }
    public double getFirstXCoordinate() {
        return firstXCoordinate;
    }

    public void setFirstYCoordinate(double firstYCoordinate) {
        this.firstYCoordinate = firstYCoordinate;
    }

    public double getFirstYCoordinate() {
        return firstYCoordinate;
    }

    public void setSecondXCoordinate(double secondXCoordinate) {
        this.secondXCoordinate = secondXCoordinate;
    }

    public double getSecondXCoordinate() {
        return secondXCoordinate;
    }

    public void setSecondYCoordinate(double secondYCoordinate) {
        this.secondYCoordinate = secondYCoordinate;
    }

    public double getSecondYCoordinate() {
        return secondYCoordinate;
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



}
