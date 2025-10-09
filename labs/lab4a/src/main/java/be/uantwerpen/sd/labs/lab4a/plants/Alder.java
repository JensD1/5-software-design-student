package be.uantwerpen.sd.labs.lab4a.plants;

import be.uantwerpen.sd.labs.lab4a.Plant;

public class Alder implements Plant {
    public String commonName() {
        return "Alder";
    }

    public double spacingMeters() {
        return 2.0;
    }

    public String soilPreference() {
        return "wet";
    }
}

