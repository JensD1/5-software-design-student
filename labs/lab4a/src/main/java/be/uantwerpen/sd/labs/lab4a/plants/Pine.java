package be.uantwerpen.sd.labs.lab4a.plants;

import be.uantwerpen.sd.labs.lab4a.Plant;

public class Pine implements Plant {
    public String commonName() {
        return "Pine";
    }

    public double spacingMeters() {
        return 2.5;
    }

    public String soilPreference() {
        return "sandy";
    }
}

