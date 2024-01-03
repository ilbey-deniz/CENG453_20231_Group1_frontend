package com.group1.frontend.components;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.group1.frontend.enums.BuildingType;
import com.group1.frontend.utils.JsonDeserializers;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@JsonDeserialize(using = JsonDeserializers.BuildingDeserializer.class)
public class Building {
    //TODO: make them private
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