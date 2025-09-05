package be.uantwerpen.sd.labs.lab5.factory;

import be.uantwerpen.sd.labs.lab5.employee.CustomerService;
import be.uantwerpen.sd.labs.lab5.employee.Employee;
import be.uantwerpen.sd.labs.lab5.employee.Manager;
import be.uantwerpen.sd.labs.lab5.employee.Programmer;

public class EmployeeFactory {
    public Employee getEmployee(String name, String function) {
        if (function == null || function.isEmpty()) {
            throw new IllegalArgumentException("Function cannot be null or empty");
        }

        function = function.toLowerCase().trim().replace(" ", "");

        switch (function) {
            case "programmer":
                return new Programmer(name);
            case "manager":
                return new Manager(name);
            case "customerservice":
                return new CustomerService(name);
            case "employee":
                return new Employee(name, function);
            default:
                throw new IllegalArgumentException("Function not recognized");
        }
    }
}
