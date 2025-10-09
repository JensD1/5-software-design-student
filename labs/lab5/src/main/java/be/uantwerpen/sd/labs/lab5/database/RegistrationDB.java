package be.uantwerpen.sd.labs.lab5.database;

import be.uantwerpen.sd.labs.lab5.employee.Employee;
import be.uantwerpen.sd.labs.lab5.register_entry.RegisterEntry;
import be.uantwerpen.sd.labs.lab5.register_entry.RegisterEntryNull;

import java.util.HashMap;

public class RegistrationDB extends Database {
    private static volatile RegistrationDB uniqueInstance;
    private final HashMap<Employee, RegisterEntry> db;

    private RegistrationDB() {
        this.db = new HashMap<>();
    }

    public static RegistrationDB getInstance() {
        if (uniqueInstance == null) {
            synchronized (RegistrationDB.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new RegistrationDB();
                }
            }
        }
        return uniqueInstance;
    }

    @Override
    public void addEntry(Employee e, RegisterEntry re) {
        RegisterEntry oldEntry = this.db.put(e, re);
        notifyObservers("entryAdded", oldEntry, re);
    }

    @Override
    public RegisterEntry getEntry(Employee e) {
        return this.db.getOrDefault(e, new RegisterEntryNull());
    }
}
