package be.uantwerpen.sd.labs.lab4a;

import be.uantwerpen.sd.labs.lab4a.controller.Controller;
import be.uantwerpen.sd.labs.lab4a.controller.RegistrationController;
import be.uantwerpen.sd.labs.lab4a.database.Database;
import be.uantwerpen.sd.labs.lab4a.database.RegistrationDB;
import be.uantwerpen.sd.labs.lab4a.employee.CustomerService;
import be.uantwerpen.sd.labs.lab4a.employee.Employee;
import be.uantwerpen.sd.labs.lab4a.employee.Manager;
import be.uantwerpen.sd.labs.lab4a.employee.Programmer;
import be.uantwerpen.sd.labs.lab4a.factory.EmployeeFactory;
import be.uantwerpen.sd.labs.lab4a.observers.DetailedEntryObserver;
import be.uantwerpen.sd.labs.lab4a.observers.EntryAddedObserver;
import be.uantwerpen.sd.labs.lab4a.register_entry.RegisterEntry;

public class Main {
    public static void main(String[] args) {
        Main main = new Main();
        main.run();
    }

    public Main() {

    }

    public void run() {
        Database timedb = RegistrationDB.getInstance();
        timedb.addPropertyChangeListener(new EntryAddedObserver());
        timedb.addPropertyChangeListener(new DetailedEntryObserver());

        Controller register = new RegistrationController(timedb);

        EmployeeFactory employeeFactory = new EmployeeFactory();

        Employee e1 = employeeFactory.getEmployee("Alice", "Programmer");
        Employee e2 = employeeFactory.getEmployee("Bob", "Customer Service");
        Employee e3 = employeeFactory.getEmployee("Charlie", "Manager");

        register.checkIn(e1);
        register.checkIn(e2);

//        print(e1, timedb.getEntry(e1));
//        print(e2, timedb.getEntry(e2));

        // We missed the print section of this checkin
        register.checkIn(e3);

        register.checkOut(e1);
        register.checkOut(e2);
        register.checkOut(e3);

//        print(e1, timedb.getEntry(e1));
//        print(e2, timedb.getEntry(e2));
//        print(e3, timedb.getEntry(e3));
    }

    public void print(Employee e, RegisterEntry re) {
        System.out.println(e.getName() +
                " (" + e.getFunction() + ")" +
                " " + re);
    }
}
