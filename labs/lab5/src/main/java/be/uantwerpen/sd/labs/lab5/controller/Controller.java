package be.uantwerpen.sd.labs.lab5.controller;

import be.uantwerpen.sd.labs.lab5.employee.Employee;

public interface Controller {
    void checkIn(Employee e);

    void checkOut(Employee e);
}
