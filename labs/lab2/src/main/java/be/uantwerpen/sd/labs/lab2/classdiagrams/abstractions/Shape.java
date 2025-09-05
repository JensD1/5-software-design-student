package be.uantwerpen.sd.labs.lab2.classdiagrams.abstractions;

public abstract class Shape {
    protected double size;
    protected String name;

    public Shape(double size, String name) {
        if (size <= 0) {
            throw new IllegalArgumentException("Size must be greater than zero");
        }
        this.size = size;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract double calcCircumference();

    public abstract double calcArea();

}
