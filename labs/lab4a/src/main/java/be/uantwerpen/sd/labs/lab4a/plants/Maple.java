package be.uantwerpen.sd.labs.lab4a.plants;

import be.uantwerpen.sd.labs.lab4a.Plant;

public class Maple implements Plant {
    public String commonName() {
        return "Maple";
    }

    public double spacingMeters() {
        return 3.0;
    }

    public String soilPreference() {
        return "loam";
    }
}

