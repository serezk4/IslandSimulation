package com.serezka.entities.herbivores;

import com.serezka.entities.Entity;
import com.serezka.entities.Grass;

import java.util.Map;

public class Boar extends Entity {
    public Boar() {
        super("кабан", 400, 50, 2, 50,
                Map.of(
                        new Mouse().getName(), 50,
                        new Caterpillar().getName(), 90,
                        new Grass().getName(), 100));
    }
}
