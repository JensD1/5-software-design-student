package be.uantwerpen.sd.labs.lab3.controller;

import be.uantwerpen.sd.labs.lab3.employee.Employee;

public interface Controller
{
    void checkIn(Employee e);
    void checkOut(Employee e);
}
