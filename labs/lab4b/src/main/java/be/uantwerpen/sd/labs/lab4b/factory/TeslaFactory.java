package be.uantwerpen.sd.labs.lab4b.factory;

import be.uantwerpen.sd.labs.lab4b.tesla.Tesla;

public interface TeslaFactory
{
    Tesla getRedCar(String name);
    Tesla getBlackCar(String name);
}
