package be.uantwerpen.sd.labs.lab4b.tesla.model_x;

import be.uantwerpen.sd.labs.lab4b.tesla.Tesla;

public abstract class ModelX extends Tesla
{
    public ModelX(String name)
    {
        super(name);
        this.model = "ModelX";
    }

    @Override
    protected void increaseSpeed()
    {
        this.speed += 11;
    }

    @Override
    protected void decreaseSpeed()
    {
        this.speed -= 11;
    }
}
