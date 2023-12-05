package com.group1.frontend.components;

import com.group1.frontend.enums.BuildingType;
import com.group1.frontend.enums.ResourceType;

import java.util.List;
import java.util.Map;

public class Building {
    BuildingType buildingType;
    Player owner;
    List<Tile> tiles;
    Corner corner;
    Map<Integer, ResourceType> diceToResourceMap;

    public Building(BuildingType buildingType, Player owner, List<Tile> tiles, Corner corner, Map<Integer, ResourceType> diceToResourceMap) {
        this.buildingType = buildingType;
        this.owner = owner;
        this.tiles = tiles;
        this.corner = corner;
        corner.setIsOccupied(true);
        corner.setOwner(owner);
        this.diceToResourceMap = diceToResourceMap;
    }

    public BuildingType getBuildingType() {
        return buildingType;
    }

    public void buildCityFromSettlement() {
        // resource logic implemented in Player or Game class
        if(buildingType == BuildingType.SETTLEMENT) {
            buildingType = BuildingType.CITY;
        }
    }
}
