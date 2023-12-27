package com.group1.frontend.components;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.group1.frontend.enums.BuildingType;
import java.util.List;

public class Building {
    BuildingType buildingType;
    @JsonBackReference
    Player owner;
    List<Tile> tiles;
    Corner corner;

    public Building(BuildingType buildingType, Player owner, List<Tile> tiles, Corner corner) {
        this.buildingType = buildingType;
        this.owner = owner;
        this.tiles = tiles;
        this.corner = corner;
        corner.setIsOccupied(true);
        corner.setOwner(owner);
        corner.setBuildingType(buildingType);
    }

    public void setBuildingType(BuildingType buildingType) {
        this.buildingType = buildingType;
    }

    public BuildingType getBuildingType() {
        return buildingType;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public Player getOwner() {
        return owner;
    }


    public void setTiles(List<Tile> tiles) {
        this.tiles = tiles;
    }

    public List<Tile> getTiles() {
        return tiles;
    }

    public void setCorner(Corner corner) {
        this.corner = corner;
    }

    public Corner getCorner() {
        return corner;
    }
}