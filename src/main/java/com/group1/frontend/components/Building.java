package com.group1.frontend.components;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.group1.frontend.enums.BuildingType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
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
        corner.setOccupied(true);
        corner.setOwner(owner);
        corner.setBuildingType(buildingType);
    }
}