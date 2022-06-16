package com.serezka.entities.herbivores;

import com.serezka.entities.Entity;
import com.serezka.entities.Grass;

import java.util.Map;

public class Buffalo extends Entity {
    public Buffalo() {
        super("буйвол", 700, 10, 3, 100,
                Map.of(new Grass().getName(), 100));
    }
}
