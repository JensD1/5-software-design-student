package be.uantwerpen.sd.labs.lab4a.employee;

public class Employee
{
    private String name;
    private String function;

    public Employee(String name, String function)
    {
        this.name = name;
        this.function = function;
    }

    public String getName()
    {
        return name;
    }

    public String getFunction()
    {
        return function;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setFunction(String function)
    {
        this.function = function;
    }
}
