package com.group1.frontend.components;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.group1.frontend.constants.BoardConstants;
import com.group1.frontend.enums.BuildingType;
import com.group1.frontend.enums.PlayerColor;
import com.group1.frontend.enums.ResourceType;
import com.group1.frontend.utils.JsonDeserializers;
import com.group1.frontend.utils.JsonSerializers;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@JsonDeserialize(using = JsonDeserializers.PlayerDeserializer.class)
@JsonSerialize(using = JsonSerializers.PlayerSerializer.class)
public class Player {
    //TODO: make them private
    HashMap<ResourceType, Integer> resources;
    List<Building> buildings;
    HashSet<Road> roads;
    PlayerColor color;
    String name;
    Boolean cpu;
    Integer victoryPoint;
    Integer longestRoad;

    public Player(PlayerColor color, String name, Boolean cpu) {
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

    public boolean hasEnoughResourcesToTrade(HashMap<ResourceType, Integer> requestedResources) {
        for (ResourceType resourceType : requestedResources.keySet()) {
            if (resources.get(resourceType) < requestedResources.get(resourceType)) {
                return false;
            }
        }
        return true;
    }

    public void addVictoryPoint(int i) {
        victoryPoint += i;
    }

    public Boolean isCpu() {
          return cpu;
    }

}
