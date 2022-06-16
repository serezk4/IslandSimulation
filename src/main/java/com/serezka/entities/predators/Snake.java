package com.serezka.entities.predators;

import com.serezka.entities.Entity;
import com.serezka.entities.herbivores.*;

import java.util.Map;

public class Snake extends Entity {
    public Snake() {
        super("удав", 15, 30, 1, 3,
                Map.of(
                        new Fox().getName(), 15,
                        new Rabbit().getName(), 20,
                        new Mouse().getName(), 40,
                        new Duck().getName(), 10
                ));
    }
}
