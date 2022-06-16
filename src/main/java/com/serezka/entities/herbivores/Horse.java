package com.serezka.entities.herbivores;

import com.serezka.entities.Entity;
import com.serezka.entities.Grass;

import java.util.Map;

public class Horse extends Entity {
    public Horse() {
        super("лошадь", 400, 20, 4, 60,
                Map.of(new Grass().getName(), 100));
    }
}
