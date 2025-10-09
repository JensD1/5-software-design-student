package be.uantwerpen.sd.labs.lab4a.planners;

import be.uantwerpen.sd.labs.lab4a.Plant;
import be.uantwerpen.sd.labs.lab4a.ReforestationPlanner;
import be.uantwerpen.sd.labs.lab4a.plants.Spruce;

public class SprucePlanner extends ReforestationPlanner {
    protected Plant createPlant() {
        return new Spruce();
    }
}

