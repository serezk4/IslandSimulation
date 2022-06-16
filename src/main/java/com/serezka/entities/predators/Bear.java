package com.serezka.entities.predators;

import com.serezka.entities.Entity;
import com.serezka.entities.herbivores.*;

import java.util.Map;

public class Bear extends Entity {
    public Bear() {
        super("медведь", 500, 5, 2, 80,
                Map.of(
                        new Snake().getName(), 80,
                        new Horse().getName(), 40,
                        new Deer().getName(), 80,
                        new Rabbit().getName(), 80,
                        new Mouse().getName(), 90,
                        new Goat().getName(), 70,
                        new Sheep().getName(), 70,
                        new Boar().getName(), 50,
                        new Buffalo().getName(), 20,
                        new Duck().getName(), 10
                ));
    }
}
