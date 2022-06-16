package com.serezka.entities.herbivores;

import com.serezka.entities.Entity;
import com.serezka.entities.Grass;

import java.util.Map;

public class Sheep extends Entity {
    public Sheep() {
        super("овца", 70, 140, 3, 15,
                Map.of(new Grass().getName(), 100));
    }
}
