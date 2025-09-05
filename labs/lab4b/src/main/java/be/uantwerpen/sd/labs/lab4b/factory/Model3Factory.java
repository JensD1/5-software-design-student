package be.uantwerpen.sd.labs.lab4b.factory;

import be.uantwerpen.sd.labs.lab4b.tesla.Tesla;
import be.uantwerpen.sd.labs.lab4b.tesla.model_3.Model3_Black;
import be.uantwerpen.sd.labs.lab4b.tesla.model_3.Model3_Red;

public class Model3Factory implements TeslaFactory {

    public Model3Factory() {
    }

    @Override
    public Tesla getRedCar(String name) {
        return new Model3_Red(name);
    }

    @Override
    public Tesla getBlackCar(String name) {
        return new Model3_Black(name);
    }
}
