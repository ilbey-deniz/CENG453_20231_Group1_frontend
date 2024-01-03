package com.group1.frontend.components;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.security.PublicKey;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
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
    public List<Double> getCoordinates() {
        return List.of(firstXCoordinate, firstYCoordinate, secondXCoordinate, secondYCoordinate);
    }
}
