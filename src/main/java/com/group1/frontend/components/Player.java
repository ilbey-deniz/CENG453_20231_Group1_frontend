package com.group1.frontend.components;

import com.group1.frontend.enums.BuildingType;
import com.group1.frontend.enums.ResourceType;

import java.util.List;
import java.util.Map;

public class Player {
    Map<ResourceType, Integer> resources;
    List<Building> buildings;
    List<Roads> roads;
    String color;
    String name;
    Integer victoryPoints;

    public Player(String name, String color) {
        this.name = name;
        this.color = color;
        this.victoryPoints = 0;
    }

    public Map<ResourceType, Integer> getResources() {
        return resources;
    }

    public void gainSingleResource(ResourceType resourceType, Integer amount) {
        this.resources.put(resourceType, this.resources.get(resourceType) + amount);
    }

    //checks before buying a building:
    // 1. if player has enough resources
    // 2. if player has road connected to the corner
    // 3. if corner is not occupied
    // 4. if the turn is valid

    public boolean buySettlement(Building building, BuildingType buildingType) {

//        TODO: add logic to check if player has enough resources to buy settlement. if not, return false.
        this.buildings.add(building);

        return false;
    }

    public boolean buyCity(Building building, BuildingType buildingType) {
//        TODO: add logic to check if player has enough resources to buy settlement.
//        make city from settlement
        return false;
    }

}
