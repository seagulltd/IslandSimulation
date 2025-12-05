package animals;

import island.Island;
import island.IslandCell;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public abstract class Animal {
    protected String name;
    protected double weight;
    protected int maxPerCell;
    protected int maxSpeed;
    protected double foodRequired;
    protected boolean isAlive;
    protected Map<String, Integer> foodProbabilities;

    public Animal(String name, double weight, int maxPerCell, int maxSpeed,
                  double foodRequired, Map<String, Integer> foodProbabilities) {
        this.name = name;
        this.weight = weight;
        this.maxPerCell = maxPerCell;
        this.maxSpeed = maxSpeed;
        this.foodRequired = foodRequired;
        this.foodProbabilities = foodProbabilities;
        this.isAlive = true;
    }

    public void eat(IslandCell cell) {
        if (!isAlive) return;

        // Ест растения
        if (foodProbabilities.containsKey("Растения") && cell.getPlants() > 0) {
            double plantsToEat = Math.min(foodRequired, cell.getPlants());
            cell.removePlants(plantsToEat);
            weight += plantsToEat;
        }

        // Ест животных
        for (Animal animal : cell.getAnimals()) {
            if (animal == this || !animal.isAlive()) continue;

            Integer probability = foodProbabilities.get(animal.getName());
            if (probability != null && ThreadLocalRandom.current().nextInt(100) < probability) {
                weight += animal.getWeight();
                animal.die();
                break;
            }
        }
    }

    public void move(IslandCell currentCell, Island island) {
        if (!isAlive || maxSpeed == 0) return;

        int newX = currentCell.getX();
        int newY = currentCell.getY();

        int direction = ThreadLocalRandom.current().nextInt(4);
        switch (direction) {
            case 0 -> newX++;
            case 1 -> newX--;
            case 2 -> newY++;
            case 3 -> newY--;
        }

        newX = Math.max(0, Math.min(newX, island.getWidth() - 1));
        newY = Math.max(0, Math.min(newY, island.getHeight() - 1));

        IslandCell newCell = island.getCell(newX, newY);
        if (newCell != null && newCell != currentCell) {
            currentCell.removeAnimal(this);
            newCell.addAnimal(this);
        }
    }

    public void reproduce(IslandCell cell) {
        if (!isAlive) return;

        boolean hasPartner = false;
        for (Animal animal : cell.getAnimals()) {
            if (animal != this && animal.isAlive() && animal.getClass().equals(this.getClass())) {
                hasPartner = true;
                break;
            }
        }

        if (hasPartner && ThreadLocalRandom.current().nextInt(100) < 30) {
            try {
                Animal baby = this.getClass().getDeclaredConstructor().newInstance();
                cell.addAnimal(baby);
            } catch (Exception e) {
                // ignore
            }
        }
    }

    public void die() {
        isAlive = false;
    }

    public String getName() { return name; }
    public double getWeight() { return weight; }
    public boolean isAlive() { return isAlive; }
}
