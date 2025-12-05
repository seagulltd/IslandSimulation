import island.Island;
public class Main {
    public static void main(String[] args) {
        System.out.println("Симуляция запущена!");

        Island island = new Island(5, 5);
        island.startSimulation();

        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {}

        island.stopSimulation();
        System.out.println("Симуляция завершена.");
    }
}
