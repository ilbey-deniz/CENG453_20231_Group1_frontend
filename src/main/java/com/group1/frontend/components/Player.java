package com.group1.frontend.components;

import com.group1.frontend.constants.BoardConstants;
import com.group1.frontend.enums.BuildingType;
import com.group1.frontend.enums.ResourceType;

import java.util.*;

public class Player {
    HashMap<ResourceType, Integer> resources;
    List<Building> buildings;
    HashSet<Road> roads;
    String color;
    String name;
    Boolean cpu;
    Integer victoryPoints;
    Integer longestRoad;

    public Player(String color, String name, Boolean cpu) {
        this.name = name;
        this.color = color;
        this.cpu = cpu;
        this.buildings = new ArrayList<>();
        this.roads = new HashSet<>();
        this.resources = new HashMap<>();
        this.victoryPoints = 1;
        this.longestRoad = 0;
        for (ResourceType resourceType : ResourceType.values()) {
            resources.put(resourceType, 0);
        }
    }

    public Map<ResourceType, Integer> getResources() {
        return resources;
    }

    public void setResources(HashMap<ResourceType, Integer> resources) {
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

    public void setVictoryPoints(Integer victoryPoints) {
        this.victoryPoints = victoryPoints;
    }
    public Integer getVictoryPoints() {
        return victoryPoints;
    }
    public void setLongestRoad(Integer longestRoad) {
        this.longestRoad = longestRoad;
    }
    public Integer getLongestRoad() {
        return longestRoad;
    }

    public void addResource(ResourceType resourceType, int i) {
        resources.put(resourceType, resources.get(resourceType) + i);
    }

    public void removeResource(ResourceType resourceType, int i) {
        resources.put(resourceType, resources.get(resourceType) - i);
    }

    public boolean hasEnoughResources(BuildingType buildingType) {
        HashMap<ResourceType, Integer> requiredResources = BoardConstants.REQUIRED_RESOURCES.get(buildingType);
        for (ResourceType resourceType : requiredResources.keySet()) {
            if (resources.get(resourceType) < requiredResources.get(resourceType)) {
                return false;
            }
        }
        return true;
    }

    public void addVictoryPoints(int i) {
        victoryPoints += i;
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
