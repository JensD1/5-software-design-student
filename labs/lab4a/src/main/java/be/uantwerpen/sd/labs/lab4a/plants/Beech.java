package be.uantwerpen.sd.labs.lab4a.plants;

import be.uantwerpen.sd.labs.lab4a.Plant;

public class Beech implements Plant {
    public String commonName() {
        return "Beech";
    }

    public double spacingMeters() {
        return 3.0;
    }

    public String soilPreference() {
        return "well-drained";
    }
}

