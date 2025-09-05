package be.uantwerpen.sd.labs.lab2.classdiagrams.abstractions;

public class Square extends Shape
{
    public Square(double size, String name)
    {
        super(size, name);
    }

    @Override
    public double calcCircumference()
    {
        return 4 * size;
    }

    @Override
    public double calcArea()
    {
        return size * size;
    }
}
