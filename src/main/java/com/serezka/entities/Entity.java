package com.serezka.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
public class Entity {
    private final String name;
    private final double weight; // kg
    private final int maxInPiece;
    private final int speed;
    private final double foodNeed; // kg
    private final Map<String, Integer> chanceToEat;

    @Setter private boolean moved = false;
    @Setter private double food;
    @Setter private int ticksWithoutFood = 0;

    public void addFood(double eaten) {
        food += eaten;
        if (food > foodNeed) food = foodNeed;
    }

    public Entity(String name, double weight, int maxInPiece, int speed, double foodNeed, Map<String, Integer> chanceToEat) {
        this.name = name;
        this.weight = weight;
        this.maxInPiece = maxInPiece;
        this.speed = speed;
        this.foodNeed = foodNeed;
        this.food = 0; //todo!
        this.chanceToEat = chanceToEat;
    }

    public Entity(Entity entity) {
        this.name = entity.getName();
        this.weight = entity.getWeight();
        this.maxInPiece = entity.getMaxInPiece();
        this.speed = entity.getSpeed();
        this.foodNeed = entity.getFoodNeed();
        this.chanceToEat = entity.getChanceToEat();
        this.food = 0;
    }

}
