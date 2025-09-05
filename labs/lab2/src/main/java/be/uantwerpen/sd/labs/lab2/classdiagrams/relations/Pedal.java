package be.uantwerpen.sd.labs.lab2.classdiagrams.relations;

public abstract class Pedal
{
    Car car;

    public Pedal(Car car)
    {
        this.car = car;
    }

    public abstract void press();
}
