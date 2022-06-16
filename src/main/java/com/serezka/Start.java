package com.serezka;

import com.serezka.entities.herbivores.Horse;
import com.serezka.entities.herbivores.Rabbit;
import com.serezka.map.Terrain;
import org.w3c.dom.Entity;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Start {
    public static void main(String[] args) {
        new Start().run();
    }

    private void run() {
        Terrain terrain = new Terrain();

        terrain.init();
        terrain.process();
        System.out.println(terrain.getStats());
    }
}
