package be.uantwerpen.sd.labs.lab2.classdiagrams.inheritance;

public class Programmer extends Employee {
    protected double bonusPerBug;
    protected double numberOfBugs;

    public Programmer(double hourlySalary, double hoursWorked, double bonusPerBug, double numberOfBugs) {
        super(hourlySalary, hoursWorked);
        this.bonusPerBug = bonusPerBug;
        this.numberOfBugs = numberOfBugs;
    }

    @Override
    public double calculateDailySalary() {
        return hourlySalary * hoursWorked + bonusPerBug * numberOfBugs;
    }
}
