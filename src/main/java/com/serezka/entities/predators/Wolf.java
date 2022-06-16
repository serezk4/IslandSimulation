package com.serezka.entities.predators;

import com.serezka.entities.Entity;
import com.serezka.entities.herbivores.*;

import java.util.Map;

public class Wolf extends Entity {
    public Wolf() {
        super("волк", 50, 30, 3, 8,
                Map.of(
                        new Horse().getName(), 10,
                        new Deer().getName(), 15,
                        new Rabbit().getName(), 60,
                        new Mouse().getName(), 80,
                        new Goat().getName(), 60,
                        new Sheep().getName(), 70,
                        new Boar().getName(), 15,
                        new Buffalo().getName(), 10,
                        new Duck().getName(), 40));
    }
}
