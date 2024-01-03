package com.group1.frontend.components;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.group1.frontend.utils.JsonDeserializers;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonDeserialize(using = JsonDeserializers.RoadDeserializer.class)
public class Road {
    //TODO: make them private
    @JsonBackReference
    Player owner;
    Edge edge;
    public Road(Player owner, Edge edge) {
        this.owner = owner;
        this.edge = edge;
        edge.setOccupied(true);
        edge.setOwner(owner);
    }
}
