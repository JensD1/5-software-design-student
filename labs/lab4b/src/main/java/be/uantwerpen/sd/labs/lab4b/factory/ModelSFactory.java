package be.uantwerpen.sd.labs.lab4b.factory;

import be.uantwerpen.sd.labs.lab4b.tesla.Tesla;
import be.uantwerpen.sd.labs.lab4b.tesla.model_s.ModelS_Black;
import be.uantwerpen.sd.labs.lab4b.tesla.model_s.ModelS_Red;

public class ModelSFactory implements TeslaFactory {
    public ModelSFactory() {
    }

    @Override
    public Tesla getRedCar(String name) {
        return new ModelS_Red(name);
    }

    @Override
    public Tesla getBlackCar(String name) {
        return new ModelS_Black(name);
    }
}
