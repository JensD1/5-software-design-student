package be.uantwerpen.sd.labs.lab3;

import be.uantwerpen.sd.labs.lab3.controller.Controller;
import be.uantwerpen.sd.labs.lab3.controller.RegistrationController;
import be.uantwerpen.sd.labs.lab3.database.Database;
import be.uantwerpen.sd.labs.lab3.database.RegistrationDB;
import be.uantwerpen.sd.labs.lab3.employee.CustomerService;
import be.uantwerpen.sd.labs.lab3.employee.Employee;
import be.uantwerpen.sd.labs.lab3.employee.Manager;
import be.uantwerpen.sd.labs.lab3.employee.Programmer;
import be.uantwerpen.sd.labs.lab3.observers.DetailedEntryObserver;
import be.uantwerpen.sd.labs.lab3.observers.EntryAddedObserver;
import be.uantwerpen.sd.labs.lab3.register_entry.RegisterEntry;

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
        Database timedb = RegistrationDB.getInstance();
        timedb.addPropertyChangeListener(new EntryAddedObserver());
        timedb.addPropertyChangeListener(new DetailedEntryObserver());

        Controller register= new RegistrationController(timedb);

        Employee e1 = new Programmer("Alice");
        Employee e2 = new CustomerService("Bob");
        Employee e3 = new Manager("Charlie");

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

    public void print(Employee e, RegisterEntry re)
    {
        System.out.println(e.getName() +
                " (" + e.getFunction() + ")" +
                " " + re);
    }
}
