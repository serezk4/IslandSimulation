package com.serezka.entities.herbivores;

import com.serezka.entities.Entity;
import com.serezka.entities.Grass;

import java.util.Map;

public class Duck extends Entity {
    public Duck() {
        super("утка", 1, 200, 4, 0.15,
                Map.of(
                        new Caterpillar().getName(), 100,
                        new Grass().getName(), 100));
    }
}
