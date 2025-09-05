package be.uantwerpen.sd.labs.lab2.classdiagrams.inheritance;

public class Main
{
    public static void main(String[] args)
    {
        Main main = new Main();
        main.run();
    }

    public Main()
    {

    }

    public void run()
    {
        Employee programmer1 = new Programmer(10, 8, 0.5, 12);
        Employee customerService1 = new CustomerService(9, 8, 1, 10);
        Employee departmentOfficer1 = new DepartmentOfficer(11, 9, 20);

        printWage("Programmer1", programmer1);
        printWage("CustomerService1", customerService1);
        printWage("DepartmentOfficer1", departmentOfficer1);
    }

    public void printWage(String name, Employee e)
    {
        System.out.println("Employee " + name + ": salary = " + e.calculateDailySalary());
    }
}
