package island;

import animals.Animal;
import animals.herbivores.*;
import animals.predators.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.Comparator;


public class Island {
    private final int width;
    private final int height;
    private final IslandCell[][] cells;
    private final ScheduledExecutorService scheduler;
    private volatile boolean isRunning;

    public Island(int width, int height) {
        this.width = width;
        this.height = height;
        this.cells = new IslandCell[width][height];
        this.scheduler = Executors.newScheduledThreadPool(3);
        this.isRunning = true;

        initializeCells();
        populateIsland();
    }

    private void initializeCells() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                cells[x][y] = new IslandCell(x, y);
            }
        }
    }

    private void populateIsland() {
        for (int i = 0; i < 10; i++) {
            addRandomAnimal();
        }
    }

    private void addRandomAnimal() {
        int x = ThreadLocalRandom.current().nextInt(width);
        int y = ThreadLocalRandom.current().nextInt(height);

        Animal animal = createRandomAnimal();
        if (animal != null) {
            cells[x][y].addAnimal(animal);
        }
    }

    private Animal createRandomAnimal() {
        int type = ThreadLocalRandom.current().nextInt(15);
        return switch (type) {
            case 0 -> new Wolf();
            case 1 -> new Boa();
            case 2 -> new Fox();
            case 3 -> new Bear();
            case 4 -> new Eagle();
            case 5 -> new Horse();
            case 6 -> new Deer();
            case 7 -> new Rabbit();
            case 8 -> new Mouse();
            case 9 -> new Goat();
            case 10 -> new Sheep();
            case 11 -> new Boar();
            case 12 -> new Buffalo();
            case 13 -> new Duck();
            case 14 -> new Caterpillar();
            default -> new Rabbit();
        };
    }

    public void startSimulation() {
        scheduler.scheduleAtFixedRate(this::growPlants, 0, 2, TimeUnit.SECONDS);
        scheduler.scheduleAtFixedRate(this::animalLifeCycle, 1, 3, TimeUnit.SECONDS);
        scheduler.scheduleAtFixedRate(this::printStatistics, 2, 5, TimeUnit.SECONDS);
        scheduler.scheduleAtFixedRate(this::displayIsland, 0, 1, TimeUnit.SECONDS);
    }

    private void growPlants() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                cells[x][y].growPlants();
            }
        }
    }

    private void animalLifeCycle() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                IslandCell cell = cells[x][y];
                for (Animal animal : cell.getAnimals()) {
                    if (animal.isAlive()) {
                        animal.eat(cell);
                        animal.reproduce(cell);
                        animal.move(cell, this);
                    }
                }
            }
        }
        cleanupDeadAnimals();
    }

    private void cleanupDeadAnimals() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                cells[x][y].getAnimals().removeIf(animal -> !animal.isAlive());
            }
        }
    }

    private void printStatistics() {
        Map<String, Integer> animalCounts = new HashMap<>();
        double totalPlants = 0;
        int totalAnimals = 0;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                totalPlants += cells[x][y].getPlants();
                for (Animal animal : cells[x][y].getAnimals()) {
                    if (animal.isAlive()) {
                        animalCounts.merge(animal.getName(), 1, Integer::sum);
                        totalAnimals++;
                    }
                }
            }
        }

        System.out.println("=== СТАТИСТИКА ===");
        System.out.printf("Животных: %d, Растений: %.1f%n", totalAnimals, totalPlants);
        animalCounts.forEach((name, count) -> System.out.printf("%s: %d%n", name, count));
        System.out.println("=================");
    }

    public void stopSimulation() {
        isRunning = false;
        scheduler.shutdown();
    }
    private void displayIsland() {
        System.out.println("\n".repeat(50)); // очистка консоли

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                IslandCell cell = cells[x][y];
                String symbol = getCellSymbol(cell);
                System.out.print(symbol + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private String getCellSymbol(IslandCell cell) {
        List<Animal> animals = cell.getAnimals();

        if (animals.isEmpty()) {
            return cell.getPlants() > 50 ? "\uD83C\uDF3F" : "."; // 🌿 или .
        }

        Animal topAnimal = animals.stream()
                .filter(Animal::isAlive)
                .max(Comparator.comparingDouble(Animal::getWeight))
                .orElse(null);

        if (topAnimal == null) return ".";

        switch (topAnimal.getName()) {
            case "Волк": return "\uD83D\uDC3A"; // 🐺
            case "Медведь": return "\uD83D\uDC3B"; // 🐻
            case "Лиса": return "\uD83E\uDD8A"; // 🦊
            case "Удав": return "\uD83D\uDC0D"; // 🐍
            case "Орел": return "\uD83E\uDD85"; // 🦅
            case "Лошадь": return "\uD83D\uDC0E"; // 🐎
            case "Олень": return "\uD83E\uDD8C"; // 🦌
            case "Кролик": return "\uD83D\uDC07"; // 🐇
            case "Мышь": return "\uD83D\uDC01"; // 🐁
            case "Коза": return "\uD83D\uDC10"; // 🐐
            case "Овца": return "\uD83D\uDC11"; // 🐑
            case "Кабан": return "\uD83D\uDC17"; // 🐗
            case "Буйвол": return "\uD83D\uDC03"; // 🐃
            case "Утка": return "\uD83E\uDD86"; // 🦆
            case "Гусеница": return "\uD83D\uDC1B"; // 🐛
            default: return "?";
        }
    }
    public IslandCell getCell(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return cells[x][y];
        }
        return null;
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
}
