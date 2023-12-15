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
    Integer victoryPoint;
    Integer longestRoad;

    public Player(String color, String name, Boolean cpu) {
        this.name = name;
        this.color = color;
        this.cpu = cpu;
        this.buildings = new ArrayList<>();
        this.roads = new HashSet<>();
        this.resources = new HashMap<>();
        this.victoryPoint = 1;
        this.longestRoad = 1;
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

    public void setVictoryPoint(Integer victoryPoint) {
        this.victoryPoint = victoryPoint;
    }
    public Integer getVictoryPoint() {
        return victoryPoint;
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

    public void addVictoryPoint(int i) {
        victoryPoint += i;
    }

}
