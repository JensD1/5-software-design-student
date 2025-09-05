package be.uantwerpen.sd.labs.lab4b.tesla.model_y;

import be.uantwerpen.sd.labs.lab4b.tesla.Tesla;

public abstract class ModelY extends Tesla
{
    public ModelY(String name)
    {
        super(name);
        this.model = "ModelY";
    }

    @Override
    protected void increaseSpeed()
    {
        this.speed += 6;
    }

    @Override
    protected void decreaseSpeed()
    {
        this.speed -= 6;
    }
}
