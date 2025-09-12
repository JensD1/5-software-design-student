package be.uantwerpen.sd.labs.lab4b.tesla.model_3;

import be.uantwerpen.sd.labs.lab4b.tesla.Tesla;

public abstract class Model3 extends Tesla
{
    public Model3(String name)
    {
        super(name);
        this.model = "Model3";
    }

    @Override
    protected void increaseSpeed()
    {
        this.speed += 5;
    }

    @Override
    protected void decreaseSpeed()
    {
        this.speed -= 5;
    }
}
