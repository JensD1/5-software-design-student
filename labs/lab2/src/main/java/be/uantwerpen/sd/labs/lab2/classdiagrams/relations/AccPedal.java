package be.uantwerpen.sd.labs.lab2.classdiagrams.relations;

public class AccPedal extends Pedal
{
    public AccPedal(Car car)
    {
        super(car);
    }

    @Override
    public void press()
    {
        car.accelerate();
    }
}
