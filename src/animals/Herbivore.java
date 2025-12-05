package animals;

import island.Island;
import island.IslandCell;

import java.util.Map;

public abstract class Herbivore extends Animal {
    public Herbivore(String name, double weight, int maxPerCell, int maxSpeed,
                     double foodRequired, Map<String, Integer> foodProbabilities) {
        super(name, weight, maxPerCell, maxSpeed, foodRequired, foodProbabilities);
    }
}


