package com.serezka.entities.herbivores;

import com.serezka.entities.Entity;
import com.serezka.entities.Grass;

import java.util.Map;

public class Caterpillar extends Entity {
    public Caterpillar() {
        super("гусеница", 0.01, 1000, 0, 0.01,
                Map.of(new Grass().getName(), 100));
    }
}
