package com.serezka.map;

import com.serezka.entities.Entity;
import com.serezka.entities.Grass;
import com.serezka.entities.herbivores.*;
import com.serezka.entities.predators.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Terrain {
    public static final int HEIGHT = 10;
    public static final int WIDTH = 10;

    public static final int TICK_TIME = 3000; // ms
    public static final int TICKS_WITHOUT_FOOD_TO_DIE = 7;

    private int currentTick;
    private TerrainField[][] map;

    private final List<Object> allEntities = List.of(
            new Bear().getName(), new Eagle().getName(), new Fox().getName(), new Snake().getName(), new Wolf().getName(),
            new Boar().getName(), new Buffalo().getName(), new Caterpillar().getName(), new Deer().getName(), new Duck().getName(), new Goat().getName(), new Horse().getName(),
            new Mouse().getName(), new Rabbit().getName(), new Sheep().getName(), new Grass().getName()
    );

    public void init() {
        // clear array
        currentTick = 0;
        map = new TerrainField[WIDTH][HEIGHT];

        // init array
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                map[i][j] = new TerrainField();
            }
        }

        // spawn entities

        // grass
        for (int i = 0; i < 600; i++) getRandomField().addEntity(new Grass());

        // predators
        for (int i = 0; i < (WIDTH * HEIGHT / 3) + ThreadLocalRandom.current().nextInt((WIDTH * HEIGHT) / 10); i++) getRandomField().addEntity(new Bear());
        for (int i = 0; i < (WIDTH * HEIGHT / 3) +ThreadLocalRandom.current().nextInt((WIDTH * HEIGHT) / 3); i++) getRandomField().addEntity(new Eagle());
        for (int i = 0; i < (WIDTH * HEIGHT / 3) +ThreadLocalRandom.current().nextInt((WIDTH * HEIGHT) / 3); i++) getRandomField().addEntity(new Fox());
        for (int i = 0; i < (WIDTH * HEIGHT / 3) +ThreadLocalRandom.current().nextInt((WIDTH * HEIGHT) / 2); i++) getRandomField().addEntity(new Snake());
        for (int i = 0; i < (WIDTH * HEIGHT / 3) +ThreadLocalRandom.current().nextInt((WIDTH * HEIGHT) / 3); i++) getRandomField().addEntity(new Wolf());

        // herbivores
        for (int i = 0; i < (WIDTH * HEIGHT / 3) +ThreadLocalRandom.current().nextInt((WIDTH * HEIGHT) / 3); i++) getRandomField().addEntity(new Boar());
        for (int i = 0; i < (WIDTH * HEIGHT / 3) +ThreadLocalRandom.current().nextInt((WIDTH * HEIGHT) / 3); i++) getRandomField().addEntity(new Buffalo());
        for (int i = 0; i < (WIDTH * HEIGHT / 3) +ThreadLocalRandom.current().nextInt(WIDTH * HEIGHT); i++) getRandomField().addEntity(new Caterpillar());
        for (int i = 0; i < (WIDTH * HEIGHT / 3) +ThreadLocalRandom.current().nextInt((WIDTH * HEIGHT) / 3); i++) getRandomField().addEntity(new Deer());
        for (int i = 0; i < (WIDTH * HEIGHT / 3) +ThreadLocalRandom.current().nextInt((WIDTH * HEIGHT) / 2); i++) getRandomField().addEntity(new Duck());
        for (int i = 0; i < (WIDTH * HEIGHT / 3) +ThreadLocalRandom.current().nextInt((WIDTH * HEIGHT) / 3); i++) getRandomField().addEntity(new Goat());
        for (int i = 0; i < (WIDTH * HEIGHT / 3) +ThreadLocalRandom.current().nextInt((WIDTH * HEIGHT) / 4); i++) getRandomField().addEntity(new Horse());
        for (int i = 0; i < (WIDTH * HEIGHT / 3) +ThreadLocalRandom.current().nextInt((int) ((WIDTH * HEIGHT) / 1.5)); i++) getRandomField().addEntity(new Mouse());
        for (int i = 0; i < (WIDTH * HEIGHT / 3) +ThreadLocalRandom.current().nextInt((int) ((WIDTH * HEIGHT) / 1.5)); i++) getRandomField().addEntity(new Rabbit());
        for (int i = 0; i < (WIDTH * HEIGHT / 3) +ThreadLocalRandom.current().nextInt((WIDTH * HEIGHT) / 2); i++) getRandomField().addEntity(new Sheep());
    }

    public String getStats() {
        Map<String, Integer> stats = new HashMap<>();

        for (int i = 0; i < WIDTH; i++)
            for (int j = 0; j < HEIGHT; j++)
                map[i][j].getEntities().forEach(entity ->
                        stats.put(entity.getName(), stats.containsKey(entity.getName()) ? stats.get(entity.getName()) + 1 : 1));

        StringBuilder sb = new StringBuilder();
        sb.append("----------------------------------------------------------------------------------->>>>>\n");

        sb.append("tick: ").append(currentTick).append("\n");
        stats.forEach((entityString, count) -> sb.append(String.format(" > %s - %d%n", entityString, count)));

        int allEntities = 0;
        for (int i = 0; i < WIDTH; i++)
            for (int j = 0; j < HEIGHT; j++) allEntities += map[i][j].getEntities().size();

        sb.append("всего существ: ").append(allEntities).append("\n");
        sb.append("----------------------------------------------------------------------------------->>>>>\n");
        sb.append("\n\n\n");
        return sb.toString();
    }

    private TerrainField getRandomField() {
        return map[ThreadLocalRandom.current().nextInt(WIDTH)][ThreadLocalRandom.current().nextInt(HEIGHT)];
    }

    public void process() {
        new Thread(new TerrainRunnable()).start();
    }

    private class TerrainRunnable implements Runnable {
        @Override
        public void run() {
            while (true) { // program runs util termination
                eat();
                multiply();
                move();

                System.out.println(getStats());
                currentTick++;

                try {
                    Thread.sleep(TICK_TIME);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void multiply() {
        ExecutorService executor = Executors.newFixedThreadPool(16);
        List<FutureTask<Map.Entry<Coordinates, List<Entity>>>> futureTasks = new ArrayList<>();
        for (int i = 0; i < WIDTH; i++)
            for (int j = 0; j < HEIGHT; j++) futureTasks.add(new FutureTask<>(new MultiplyCallable(i, j)));

        futureTasks.forEach(executor::submit);

        // sync
        while (futureTasks.stream().anyMatch(futureTask -> !futureTask.isDone())) {}

        AtomicInteger multiplied = new AtomicInteger();

        // update map
        futureTasks.forEach(task -> {
            try {
                Map.Entry<Coordinates, List<Entity>> result = task.get();

                int prevSize = map[result.getKey().x()][result.getKey().y()].getEntities().size();
                map[result.getKey().x()][result.getKey().y()].setEntities(result.getValue());
                multiplied.addAndGet(map[result.getKey().x()][result.getKey().y()].getEntities().size() - prevSize);

            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });

        System.out.println("размножилсь: " + multiplied.get());
    }

    @AllArgsConstructor
    @Getter
    private class MultiplyCallable implements Callable<Map.Entry<Coordinates, List<Entity>>> {
        private final int i;
        private final int j;

        @Override
        public Map.Entry<Coordinates, List<Entity>> call() {
            List<Entity> result = new ArrayList<>();
            int oldSize = map[i][j].getEntities().size();


            allEntities.forEach(entityName -> {
                List<Entity> individuals = map[i][j].getEntities()
                        .stream()
                        .filter(entity -> entity.getName().equals(entityName))
                        .toList();

                List<Entity> canMultiply = individuals
                        .stream()
                        .filter(individual -> individual.getFood() >= individual.getFoodNeed() && individual.getFoodNeed() != -1)
                        .toList();

                canMultiply.forEach(entityFiltered -> entityFiltered.setFood(0));

                result.addAll(individuals);

                if (!canMultiply.isEmpty()) {
                    Entity example = canMultiply.get(0);
                    for (int k = 0; k < canMultiply.size() / 2 && example.getMaxInPiece() > individuals.size(); k++)
                        result.add(new Entity(
                                example.getName(),
                                example.getWeight(),
                                example.getMaxInPiece(),
                                example.getSpeed(),
                                example.getFoodNeed(),
                                example.getChanceToEat())
                        );
                }
            });


            return new AbstractMap.SimpleEntry<>(new Coordinates(i, j), result);
        }
    }

    private void move() {
        ExecutorService executor = Executors.newFixedThreadPool(16);
        List<FutureTask<Void>> futureTasks = new ArrayList<>();
        for (int i = 0; i < WIDTH; i++)
            for (int j = 0; j < HEIGHT; j++) futureTasks.add(new FutureTask<>(new MoveCallable(new Coordinates(i, j))));

        futureTasks.forEach(executor::submit);

        // sync
        while (futureTasks.stream().anyMatch(futureTask -> !futureTask.isDone())) {}

        // validate
        validate();

        Arrays.stream(map).forEach(row -> Arrays.stream(row).forEach(entities -> entities.getEntities().forEach(entity -> entity.setMoved(false))));
    }

    @AllArgsConstructor
    private class MoveCallable implements Callable<Void> {
        private final Coordinates cords;

        @Override
        public Void call() {
            List<Entity> localEntities = map[cords.getX()][cords.getY()].getEntities();

            for (Entity entity : localEntities) {
                if (entity.getSpeed() <= 0) continue;
                if (entity.getName().equals(new Grass().getName())) continue;

                int range = ThreadLocalRandom.current().nextInt(entity.getSpeed());
                int direction = ThreadLocalRandom.current().nextInt(4);

                // 0 - right     | j
                // 1 - left      | j
                // 2 - up        | i
                // 3 - down      | i

                int x = direction == 2 ? Math.max(cords.getX() - range, 0) : direction == 3 ? (Math.min(cords.getX() + range, WIDTH - 1)) : cords.getX();
                int y = direction == 1 ? Math.max(cords.getY() - range, 0) : direction == 0 ? (Math.min(cords.getY() + range, HEIGHT - 1)) : cords.getY();

                if (!(cords.check(x, y)) && !entity.isMoved()) {
                    entity.setMoved(true);
                    map[x][y].addEntity(new Entity(entity));
                    map[cords.getX()][cords.getY()].removeEntity(entity);
                }
            }

            return null; // void
        }
    }

    private void eat() {
        ExecutorService executor = Executors.newFixedThreadPool(16);
        List<FutureTask<Map.Entry<Coordinates, List<Entity>>>> futureTasks = new ArrayList<>();
        for (int i = 0; i < WIDTH; i++)
            for (int j = 0; j < HEIGHT; j++) futureTasks.add(new FutureTask<>(new EatCallable(new Coordinates(i, j))));

        futureTasks.forEach(executor::submit);

        // sync
        while (futureTasks.stream().anyMatch(futureTask -> !futureTask.isDone())) {}

        AtomicInteger diedOrEaten = new AtomicInteger();

        // update map
        futureTasks.forEach(task -> {
            try {
                Map.Entry<Coordinates, List<Entity>> result = task.get();

                int prevSize = map[result.getKey().getX()][result.getKey().getY()].getEntities().size();
                map[result.getKey().getX()][result.getKey().getY()].setEntities(result.getValue().stream().filter(Objects::nonNull).toList());
                diedOrEaten.addAndGet(prevSize - map[result.getKey().getX()][result.getKey().getY()].getEntities().size());

            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });

        // validate
        validate();

        // print
        System.out.println("умерли или были съедены: " + diedOrEaten.get());
    }

    @AllArgsConstructor
    private class EatCallable implements Callable<Map.Entry<Coordinates, List<Entity>>> {
        private final Coordinates cords;

        @Override
        public Map.Entry<Coordinates, List<Entity>> call() {
            List<Entity> entitiesInField = new ArrayList<>(map[cords.getX()][cords.getY()].getEntities());
            entitiesInField.sort(Comparator.comparingDouble(Entity::getWeight));

            List<Entity> deleted = new ArrayList<>();

            entity:
            for (Entity entity : entitiesInField) {
                if (entity.getName().equals(new Grass().getName())) continue;
                if (deleted.stream().anyMatch(deletedEntity -> deletedEntity == entity)) continue;

                for (Map.Entry<String, Integer> entityEatChance :
                        entity.getChanceToEat().entrySet().stream().sorted(Comparator.comparingInt(Map.Entry::getValue)).toList()) {
                    for (Entity suitable : entitiesInField.stream().filter(toFilter -> toFilter.getName().equals(entityEatChance.getKey())).toList()) {
                        if (ThreadLocalRandom.current().nextInt(100) <= entityEatChance.getValue()) {
                            entity.setFood(entity.getFood() + suitable.getWeight());

                            deleted.add(suitable);
                            continue entity;
                        }
                    }
                }
            }

            entitiesInField.removeAll(deleted);

            entitiesInField.removeAll(entitiesInField.stream().filter(entity -> entity.getFoodNeed() != -1 && entity.getTicksWithoutFood() >= TICKS_WITHOUT_FOOD_TO_DIE && entity.getFood() == 0).toList());
            entitiesInField.stream().filter(entity -> entity.getFoodNeed() != -1 && entity.getFood() > 0 && entity.getTicksWithoutFood() > 0).forEach(entity -> entity.setTicksWithoutFood(0));
            entitiesInField.stream().filter(entity -> entity.getFoodNeed() != -1 && entity.getFood() == 0).forEach(entity -> entity.setTicksWithoutFood(entity.getTicksWithoutFood() + 1));

            return new AbstractMap.SimpleEntry<>(cords, entitiesInField);
        }
    }

    private record Coordinates(int x, int y) {
        public boolean check(int x, int y) {
            return this.x == x && this.y == y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }

    private void validate() {
        for (int i = 0; i < WIDTH; i++)
            for (int j = 0; j < HEIGHT; j++)
                map[i][j].setEntities(map[i][j].getEntities().stream().filter(Objects::nonNull).toList());
    }
}
