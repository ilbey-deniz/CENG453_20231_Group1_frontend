package com.group1.frontend.components;

import com.group1.frontend.enums.ResourceType;

public class Tile {
    private int diceNumber;
    private ResourceType resourceType;

    public Tile(int diceNumber, ResourceType resourceType) {
        this.diceNumber = diceNumber;
        this.resourceType = resourceType;
    }

    public void setDiceNumber(int diceNumber) {
        this.diceNumber = diceNumber;
    }

    public int getDiceNumber() {
        return diceNumber;
    }

    public void setResourceType(ResourceType resourceType) {
        this.resourceType = resourceType;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }
}
