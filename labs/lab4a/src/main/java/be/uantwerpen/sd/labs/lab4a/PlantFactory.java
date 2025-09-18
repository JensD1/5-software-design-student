package be.uantwerpen.sd.labs.lab4a;

import be.uantwerpen.sd.labs.lab4a.plants.*;

import java.util.Locale;

public class PlantFactory {

    public Plant createPlant(String speciesName) {
        Plant plant = null;

        switch (speciesName.toLowerCase(Locale.ROOT)) {
            case "oak" -> plant = new Oak();
            case "pine" -> plant = new Pine();
            case "spruce" -> plant = new Spruce();
            case "birch" -> plant = new Birch();
            case "beech" -> plant = new Beech();
            case "maple" -> plant = new Maple();
            case "poplar" -> plant = new Poplar();
            case "willow" -> plant = new Willow();
            case "alder" -> plant = new Alder();
            case "ash" -> plant = new Ash();
            case "elm" -> plant = new Elm();
            case "cedar" -> plant = new Cedar();
            case "larch" -> plant = new Larch();
            default -> throw new IllegalArgumentException("Unknown species: " + speciesName);
        }
        return plant;
    }

}
