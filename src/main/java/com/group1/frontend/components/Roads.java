package com.group1.frontend.components;

import java.util.List;


public class Roads {
    Player owner;
//    List<Corner> corners;
    Edge edge;

    public Roads(Player owner, Edge edge) {
        this.owner = owner;
//        this.corners = corners;
        this.edge = edge;
        edge.setOccupied(true);
        edge.setOwner(owner);
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public Player getOwner() {
        return owner;
    }

//    public void setCorners(List<Corner> corners) {
//        this.corners = corners;
//    }
//
//    public List<Corner> getCorners() {
//        return corners;
//    }

    public void setEdge(Edge edge) {
        this.edge = edge;
    }

    public Edge getEdge() {
        return edge;
    }
}
