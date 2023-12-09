package com.group1.frontend.components;

import com.group1.frontend.enums.ResourceType;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class Player {
    Map<ResourceType, Integer> resources;
    List<Building> buildings;
    HashSet<Road> roads;
    String color;
    String name;
    Boolean cpu;

    public Player(String name, String color, Boolean cpu) {
        this.name = name;
        this.color = color;
        this.cpu = cpu;
    }

    public Map<ResourceType, Integer> getResources() {
        return resources;
    }

    public void setResources(Map<ResourceType, Integer> resources) {
        this.resources = resources;
    }

    public void setBuildings(List<Building> buildings) {
        this.buildings = buildings;
    }

    public List<Building> getBuildings() {
        return buildings;
    }

    public void setRoads(HashSet<Road> roads) {
        this.roads = roads;
    }

    public HashSet<Road> getRoads() {
        return roads;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setCpu(Boolean cpu) {
        this.cpu = cpu;
    }
    public Boolean isCpu() {
        return cpu;
    }

    //checks before buying a building:
    // 1. if player has enough resources
    // 2. if player has road connected to the corner
    // 3. if corner is not occupied
    // 4. if the turn is valid
    // TODO: move this to game class
//    public boolean buySettlement(Building building, BuildingType buildingType) {
//
////        TODO: add logic to check if player has enough resources to buy settlement. if not, return false.
//        this.buildings.add(building);
//
//        return false;
//    }
//
//    public boolean buyCity(Building building, BuildingType buildingType) {
////        TODO: add logic to check if player has enough resources to buy settlement.
////        make city from settlement
//        return false;
//    }

}
