package be.uantwerpen.sd.labs.lab4a.planners;

import be.uantwerpen.sd.labs.lab4a.Plant;
import be.uantwerpen.sd.labs.lab4a.ReforestationPlanner;
import be.uantwerpen.sd.labs.lab4a.plants.Pine;

public class PinePlanner extends ReforestationPlanner {
    protected Plant createPlant() {
        return new Pine();
    }
}