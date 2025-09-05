package be.uantwerpen.sd.labs.lab2.classdiagrams.inheritance;

public class DepartmentOfficer extends Employee {
    protected double companyBonus;

    public DepartmentOfficer(double hourlySalary, double hoursWorked, double companyBonus) {
        super(hourlySalary, hoursWorked);
        this.companyBonus = companyBonus;
    }

    @Override
    public double calculateDailySalary() {
        return hourlySalary * hoursWorked + companyBonus;
    }
}
