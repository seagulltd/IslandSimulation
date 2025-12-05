package animals;

import java.util.Map;

public abstract class Predator extends Animal {
    public Predator(String name, double weight, int maxPerCell, int maxSpeed,
                    double foodRequired, Map<String, Integer> foodProbabilities) {
        super(name, weight, maxPerCell, maxSpeed, foodRequired, foodProbabilities);
    }
}
