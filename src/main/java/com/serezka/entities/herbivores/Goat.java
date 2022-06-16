package com.serezka.entities.herbivores;

import com.serezka.entities.Entity;
import com.serezka.entities.Grass;

import java.util.Map;

public class Goat extends Entity {
    public Goat() {
        super("коза", 60, 140, 3, 10,
                Map.of(new Grass().getName(), 100));
    }
}
