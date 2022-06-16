package com.serezka.entities.herbivores;

import com.serezka.entities.Entity;
import com.serezka.entities.Grass;

import java.util.Map;

public class Deer extends Entity {
    public Deer() {
        super("олень", 300, 20, 4, 50,
                Map.of(new Grass().getName(), 100));
    }
}
