package be.uantwerpen.sd.labs.lab4b.factory;

import be.uantwerpen.sd.labs.lab4b.tesla.Tesla;
import be.uantwerpen.sd.labs.lab4b.tesla.model_x.ModelX_Black;
import be.uantwerpen.sd.labs.lab4b.tesla.model_x.ModelX_Red;

public class ModelXFactory implements TeslaFactory {
    public ModelXFactory() {
    }

    @Override
    public Tesla getRedCar(String name) {
        return new ModelX_Red(name);
    }

    @Override
    public Tesla getBlackCar(String name) {
        return new ModelX_Black(name);
    }
}
