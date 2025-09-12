package be.uantwerpen.sd.labs.lab4b.factory;

import be.uantwerpen.sd.labs.lab4b.tesla.Tesla;
import be.uantwerpen.sd.labs.lab4b.tesla.model_y.ModelY_Black;
import be.uantwerpen.sd.labs.lab4b.tesla.model_y.ModelY_Red;

public class ModelYFactory implements TeslaFactory {
    public ModelYFactory() {
    }

    @Override
    public Tesla getRedCar(String name) {
        return new ModelY_Red(name);
    }

    @Override
    public Tesla getBlackCar(String name) {
        return new ModelY_Black(name);
    }
}
