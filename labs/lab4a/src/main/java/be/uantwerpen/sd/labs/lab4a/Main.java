package be.uantwerpen.sd.labs.lab4a;

public class Main {
    public static void main(String[] args) {
        PlantFactory plantFactory = new PlantFactory();

        System.out.println("Reforestation of Antwerp:");
        ReforestationPlanner reforestAntwerp = new ReforestationPlanner(plantFactory);
        PlantingPlan p1 = reforestAntwerp.plan(1.2, "loam", "oak");
        System.out.println(p1.getNote());
        PlantingPlan p2 = reforestAntwerp.plan(2, "loam", "maple");
        System.out.println(p2.getNote());

        System.out.println("\nReforestation of East-Flanders:");
        ReforestationPlanner reforestEastFlanders = new ReforestationPlanner(plantFactory);
        PlantingPlan p3 = reforestEastFlanders.plan(5, "clay", "alder");
        System.out.println(p3.getNote());

        System.out.println("\nReforestation of West-Flanders:");
        ReforestationPlanner reforestWestFlanders = new ReforestationPlanner(plantFactory);
        PlantingPlan p4 = reforestWestFlanders.plan(3.1, "sandy", "pine");
        System.out.println(p4.getNote());

    }
}
