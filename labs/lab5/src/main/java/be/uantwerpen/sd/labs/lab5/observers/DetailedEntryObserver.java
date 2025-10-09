package be.uantwerpen.sd.labs.lab5.observers;

import be.uantwerpen.sd.labs.lab5.register_entry.RegisterEntry;

import java.beans.PropertyChangeListener;

public class DetailedEntryObserver implements PropertyChangeListener {
    @Override
    public void propertyChange(java.beans.PropertyChangeEvent evt) {
        if ("entryAdded".equals(evt.getPropertyName())) {
            RegisterEntry newEntry = (RegisterEntry) evt.getNewValue();
            System.out.println("Employee entry added: " + newEntry);
        }
    }
}
