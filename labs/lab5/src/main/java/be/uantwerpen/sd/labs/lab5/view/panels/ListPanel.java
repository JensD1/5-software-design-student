package be.uantwerpen.sd.labs.lab5.view.panels;

import be.uantwerpen.sd.labs.lab5.register_entry.RegisterEntry;

import javax.swing.*;

public class ListPanel extends JPanel {

    private JList<RegisterEntry> entryJList;
    private DefaultListModel<RegisterEntry> entryListModel;

    public ListPanel() {
        JLabel label = new JLabel("Registrations");
        entryListModel = new DefaultListModel<>();
        entryJList = new JList<>(entryListModel);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.add(label);
        this.add(entryJList);
    }

    public void addEntry(RegisterEntry entry) {
        this.entryListModel.addElement(entry);
    }
}
