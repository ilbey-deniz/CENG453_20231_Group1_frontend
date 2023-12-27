package com.group1.frontend.components;

import com.group1.frontend.enums.TileType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Tile {
    private int diceNumber;
    private TileType tileType;

    // center coordinates of the tile
    private double xCoordinate;
    private double yCoordinate;
}
