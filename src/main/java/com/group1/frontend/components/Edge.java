package com.group1.frontend.components;

import com.fasterxml.jackson.annotation.JsonBackReference;

public class Edge {
    private double firstXCoordinate;
    private double firstYCoordinate;

    private double secondXCoordinate;
    private double secondYCoordinate;

    private boolean occupied;
    @JsonBackReference
    private Player owner;

    public Edge(double firstXCoordinate, double firstYCoordinate, double secondXCoordinate, double secondYCoordinate) {
        this.firstXCoordinate = firstXCoordinate;
        this.firstYCoordinate = firstYCoordinate;
        this.secondXCoordinate = secondXCoordinate;
        this.secondYCoordinate = secondYCoordinate;
        this.occupied = false;
        this.owner = null;
    }

    public Edge(double firstXCoordinate, double firstYCoordinate, double secondXCoordinate, double secondYCoordinate,
                boolean isOccupied, Player owner) {
        this.firstXCoordinate = firstXCoordinate;
        this.firstYCoordinate = firstYCoordinate;
        this.secondXCoordinate = secondXCoordinate;
        this.secondYCoordinate = secondYCoordinate;
        this.occupied = isOccupied;
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

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public boolean isOccupied() {
        return occupied;
    }



}
