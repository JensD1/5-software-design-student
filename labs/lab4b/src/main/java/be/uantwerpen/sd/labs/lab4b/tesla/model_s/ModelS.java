package be.uantwerpen.sd.labs.lab4b.tesla.model_s;

import be.uantwerpen.sd.labs.lab4b.tesla.Tesla;

public abstract class ModelS extends Tesla
{
    public ModelS(String name)
    {
        super(name);
        this.model = "ModelS";
    }

    @Override
    protected void increaseSpeed()
    {
        this.speed += 10;
    }

    @Override
    protected void decreaseSpeed()
    {
        this.speed -= 10;
    }
}
