package be.uantwerpen.sd.labs.lab3.controller;

import be.uantwerpen.sd.labs.lab3.database.Database;
import be.uantwerpen.sd.labs.lab3.employee.Employee;
import be.uantwerpen.sd.labs.lab3.register_entry.RegisterEntry;

public class RegistrationController implements Controller
{
    private Database db;

    public RegistrationController(Database db)
    {
        this.db = db;
    }

    @Override
    public void checkIn(Employee e)
    {
        RegisterEntry entry = new RegisterEntry(true);
        db.addEntry(e, entry);
    }

    @Override
    public void checkOut(Employee e)
    {
        RegisterEntry entry = new RegisterEntry(false);
        db.addEntry(e, entry);
    }
}
