package com.group1.frontend.components;

public class Edge {
    private int xCoordinate;
    private int yCoordinate;

    private boolean isOccupied;
    private Player owner;

    public Edge(int xCoordinate, int yCoordinate) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.isOccupied = false;
        this.owner = null;
    }

    public Edge(int xCoordinate, int yCoordinate, boolean isOccupied, Player owner) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.isOccupied = isOccupied;
        this.owner = owner;
    }

    public void setXCoordinate(int xCoordinate) {
        this.xCoordinate = xCoordinate;
    }
    public int getXCoordinate() {
        return xCoordinate;
    }

    public void setYCoordinate(int yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    public int getYCoordinate() {
        return yCoordinate;
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
