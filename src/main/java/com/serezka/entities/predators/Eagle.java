package com.serezka.entities.predators;

import com.serezka.entities.Entity;
import com.serezka.entities.herbivores.*;

import java.util.Map;

public class Eagle extends Entity {
    public Eagle() {
        super("орёл", 6, 20, 3, 1,
                Map.of(
                        new Fox().getName(), 10,
                        new Rabbit().getName(), 90,
                        new Mouse().getName(), 90,
                        new Duck().getName(), 80
                ));
    }
}
