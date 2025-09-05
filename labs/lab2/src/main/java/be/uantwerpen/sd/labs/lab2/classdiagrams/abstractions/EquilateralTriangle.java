package be.uantwerpen.sd.labs.lab2.classdiagrams.abstractions;

public class EquilateralTriangle extends Shape {

    public EquilateralTriangle(double size, String name) {
        super(size, name);
    }

    @Override
    public double calcCircumference() {
        return 3 * size;
    }

    @Override
    public double calcArea() {
        return Math.sqrt(3) / 4 * size * size;
    }
}
