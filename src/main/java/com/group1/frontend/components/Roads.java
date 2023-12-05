package com.group1.frontend.components;

import java.util.List;

public class Roads {
    Player owner;
    List<Corner> Corners;
    Edge edge;

    public Roads(Player owner, List<Corner> Corners, Edge edge) {
        this.owner = owner;
        this.Corners = Corners;
        this.edge = edge;
        edge.setIsOccupied(true);
        edge.setOwner(owner);
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public List<Corner> getCorners() {
        return Corners;
    }

    public void setCorners(List<Corner> Corners) {
        this.Corners = Corners;
    }

    public Edge getEdge() {
        return edge;
    }

    public void setEdge(Edge edge) {
        this.edge = edge;
    }
}
