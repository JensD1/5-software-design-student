package be.uantwerpen.sd.labs.lab5;

import be.uantwerpen.sd.labs.lab5.controller.RegistrationController;
import be.uantwerpen.sd.labs.lab5.database.Database;
import be.uantwerpen.sd.labs.lab5.database.RegistrationDB;
import be.uantwerpen.sd.labs.lab5.employee.Employee;
import be.uantwerpen.sd.labs.lab5.factory.EmployeeFactory;
import be.uantwerpen.sd.labs.lab5.observers.DetailedEntryObserver;
import be.uantwerpen.sd.labs.lab5.observers.EntryAddedObserver;
import be.uantwerpen.sd.labs.lab5.view.ViewFrame;

public class Main {
    public Main() {

    }

    public static void main(String[] args) {
        Main main = new Main();
        main.run();
    }

    public void run() {
        // Replace with your own objects
        Database timedb = RegistrationDB.getInstance();
        RegistrationController register = new RegistrationController(timedb);
        EmployeeFactory factory = new EmployeeFactory();

        ViewFrame view = new ViewFrame(register);
        view.initialize();

        // Replace with your own observers
        EntryAddedObserver addedObserver = new EntryAddedObserver();
        DetailedEntryObserver detailedObserver = new DetailedEntryObserver();
        timedb.addPropertyChangeListener(addedObserver);
        timedb.addPropertyChangeListener(detailedObserver);

        // Replace with your own employee creation methods
        Employee e1 = factory.getEmployee("Alice", "Programmer");
        Employee e2 = factory.getEmployee("Bob", "CustomerService");
        Employee e3 = factory.getEmployee("Charlie", "Manager");

        sleep(3000);

        register.checkIn(e1);
        register.checkIn(e2);
        register.checkIn(e3);

        sleep(1000);
        register.checkOut(e1);
        sleep(1000);
        register.checkOut(e2);
        sleep(1000);
        register.checkOut(e3);
    }

    public void sleep(int millis) {
        try {
            System.out.print("Sleeping [    ]\r");
            Thread.sleep(millis);
            System.out.println("Sleeping [ OK ]");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
