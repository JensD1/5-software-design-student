package be.uantwerpen.sd.labs.lab2.classdiagrams.relations;

public class Wheel
{
    private double speed;

    public Wheel()
    {
        this.speed = 0;
    }

    public void speedUp()
    {
        this.speed++;
    }

    public void speedDown()
    {
        this.speed--;
    }

    public double getSpeed()
    {
        return speed;
    }
}
