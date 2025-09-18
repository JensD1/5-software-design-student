package be.uantwerpen.sd.labs.lab4a;

import be.uantwerpen.sd.labs.lab4a.plants.*;

import java.util.*;

class ReforestationPlanner {

    PlantFactory plantFactory;

    public ReforestationPlanner(PlantFactory plantFactory) {
        this.plantFactory = plantFactory;
    }

    public PlantingPlan plan(double hectares, String soil, String speciesName) {
        Plant plant;

        plant = plantFactory.createPlant(speciesName);

        // naive density: 1 tree per (spacing^2) m²; 1 ha = 10_000 m²
        double spacing = Math.max(plant.spacingMeters(), soilAdjustment(soil));
        int count = (int) Math.round((hectares * 10_000.0) / (spacing * spacing));
        String note = "Plant " + count + " × " + plant.info() + " on " + hectares + " ha (soil=" + soil + ")";
        return new PlantingPlan(plant, count, spacing, note);
    }

    private double soilAdjustment(String soil) {
        String s = soil.toLowerCase(Locale.ROOT);
        if (s.contains("sandy")) return 2.5; // wider spacing on poor soils
        if (s.contains("clay"))  return 3.0;
        if (s.contains("wet") || s.contains("saline")) return 2.0;
        return 2.0; // default baseline
    }
}
