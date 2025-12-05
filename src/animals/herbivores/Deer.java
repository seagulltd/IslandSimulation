package animals.herbivores;

import animals.Herbivore;
import java.util.Map;

public class Deer extends Herbivore {
    public Deer() {
        super("Олень", 300, 20, 4, 50, Map.of("Растения", 100));
    }
}
