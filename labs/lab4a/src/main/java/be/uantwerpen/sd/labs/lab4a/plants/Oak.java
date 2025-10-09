package be.uantwerpen.sd.labs.lab4a.plants;

import be.uantwerpen.sd.labs.lab4a.Plant;

public class Oak implements Plant {
    public String commonName() {
        return "Oak";
    }

    public double spacingMeters() {
        return 3.0;
    }

    public String soilPreference() {
        return "loam";
    }
}

