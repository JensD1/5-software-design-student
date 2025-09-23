package be.uantwerpen.sd.labs.lab4a.controller;

import be.uantwerpen.sd.labs.lab4a.employee.Employee;

public interface Controller
{
    void checkIn(Employee e);
    void checkOut(Employee e);
}
