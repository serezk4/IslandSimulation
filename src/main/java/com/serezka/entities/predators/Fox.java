package com.serezka.entities.predators;

import com.serezka.entities.Entity;
import com.serezka.entities.herbivores.*;

import java.util.Map;

public class Fox extends Entity {
    public Fox() {
        super("лиса", 8, 30, 2, 2,
                Map.of(
                        new Rabbit().getName(), 70,
                        new Mouse().getName(), 90,
                        new Duck().getName(), 60,
                        new Caterpillar().getName(), 40
                ));
    }
}
