package be.uantwerpen.sd.labs.lab4a.planners;

import be.uantwerpen.sd.labs.lab4a.Plant;
import be.uantwerpen.sd.labs.lab4a.ReforestationPlanner;
import be.uantwerpen.sd.labs.lab4a.plants.Willow;

public class WillowPlanner extends ReforestationPlanner {
    protected Plant createPlant() {
        return new Willow();
    }
}
