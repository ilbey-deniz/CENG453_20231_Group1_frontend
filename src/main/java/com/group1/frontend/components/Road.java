package com.group1.frontend.components;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Road {

    @JsonBackReference
    Player owner;
//    List<Corner> corners;
    Edge edge;

    public Road(Player owner, Edge edge) {
        this.owner = owner;
//        this.corners = corners;
        this.edge = edge;
        edge.setOccupied(true);
        edge.setOwner(owner);
    }
}
