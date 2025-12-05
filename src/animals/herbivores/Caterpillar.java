package animals.herbivores;

import animals.Herbivore;
import island.Island;
import island.IslandCell;
import java.util.Map;

public class Caterpillar extends Herbivore {
    public Caterpillar() {
        super("Гусеница", 0.01, 1000, 0, 0, Map.of("Растения", 100));
    }

    @Override
    public void move(IslandCell currentCell, Island island) {
    }
}
