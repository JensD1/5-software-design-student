package be.uantwerpen.sd.labs.lab4a.database;

import be.uantwerpen.sd.labs.lab4a.employee.Employee;
import be.uantwerpen.sd.labs.lab4a.register_entry.RegisterEntry;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public abstract class Database {
    private final PropertyChangeSupport support;

    protected Database() {
        this.support = new PropertyChangeSupport(this);
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }

    protected void notifyObservers(String propertyName, Object oldValue, Object newValue) {
        support.firePropertyChange(propertyName, oldValue, newValue);
    }

    public abstract void addEntry(Employee e, RegisterEntry re);

    public abstract RegisterEntry getEntry(Employee e);
}
