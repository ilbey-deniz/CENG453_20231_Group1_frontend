package com.group1.frontend.components;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.group1.frontend.enums.BuildingType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Corner {

    private double xCoordinate;

    private  double yCoordinate;

    private boolean occupied;

    @JsonBackReference
    private Player owner;

    private BuildingType buildingType;

    public Corner(double xCoordinate, double yCoordinate) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.occupied = false;
        this.owner = null;
        this.buildingType = null;
    }
    public List<Double> getCoordinates() {
        return List.of(xCoordinate, yCoordinate);
    }
}
