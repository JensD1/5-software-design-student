package be.uantwerpen.sd.labs.lab4b.tesla;

public abstract class Tesla
{
    protected String color;
    protected String name;
    protected String model;

    protected double speed;

    public Tesla(String name)
    {
        this.name = name;
        this.speed = 0.0;
    }

    public String getColor()
    {
        return this.color;
    }

    public String getName()
    {
        return this.name;
    }

    public String getModel()
    {
        return this.model;
    }

    protected abstract void increaseSpeed();
    protected abstract void decreaseSpeed();

    public void accelerate()
    {
        increaseSpeed();
        printStatus();
    }

    public void decelerate()
    {
        decreaseSpeed();
        printStatus();
    }

    private void printStatus()
    {
        System.out.println(String.format("%s (%s %s - %s) has speed %2.1f", name, "Tesla", model, color, speed));
    }
}
