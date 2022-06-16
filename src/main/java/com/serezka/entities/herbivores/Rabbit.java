package com.serezka.entities.herbivores;

import com.serezka.entities.Entity;
import com.serezka.entities.Grass;

import java.util.Map;

public class Rabbit extends Entity {
    public Rabbit() {
        super("кролик", 2, 150, 2, 0.45,
                Map.of(new Grass().getName(), 100));
    }
}
