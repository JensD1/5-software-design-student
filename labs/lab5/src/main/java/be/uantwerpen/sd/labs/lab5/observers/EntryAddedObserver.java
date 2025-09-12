package be.uantwerpen.sd.labs.lab5.observers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class EntryAddedObserver implements PropertyChangeListener {

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("entryAdded".equals(evt.getPropertyName())) {
            System.out.println("Database got updated");
        }
    }
}
