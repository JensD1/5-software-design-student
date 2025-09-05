package be.uantwerpen.sd.labs.lab2.classdiagrams.inheritance;

public class CustomerService extends Employee {
    protected double bonusPerCostumer;
    protected double numberOfCostumers;

    public CustomerService(double hourlySalary, double hoursWorked, double bonusPerCostumer, double numberOfCostumers) {
        super(hourlySalary, hoursWorked);
        this.bonusPerCostumer = bonusPerCostumer;
        this.numberOfCostumers = numberOfCostumers;
    }

    @Override
    public double calculateDailySalary() {
        return hourlySalary * hoursWorked + bonusPerCostumer * numberOfCostumers;
    }
}
