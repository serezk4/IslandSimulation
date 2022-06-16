package com.serezka.entities.herbivores;

import com.serezka.entities.Entity;
import com.serezka.entities.Grass;

import java.util.Map;

public class Mouse extends Entity {
    public Mouse() {
        super("мышь", 0.05, 500, 1, 0.01,
                Map.of(new Caterpillar().getName(), 100,
                        new Grass().getName(), 100));
    }
}
