package be.uantwerpen.sd.labs.lab5.view;

import be.uantwerpen.sd.labs.lab5.controller.RegistrationController;
import be.uantwerpen.sd.labs.lab5.view.panels.ListPanel;
import be.uantwerpen.sd.labs.lab5.view.panels.RegistrationButtonPanel;

import javax.swing.*;
import java.awt.*;

public class ViewFrame extends JFrame {
    // Get your controller in this private field
    RegistrationController registrationController;
    ListPanel panel;
    RegistrationButtonPanel buttons;

    public ViewFrame(RegistrationController register) {
        super("Registration");
        this.registrationController = register;
    }

    public void initialize() {
        this.setSize(500, 300);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GridBagLayout layout = new GridBagLayout();
        this.setLayout(layout);

        // Pass the controller to the ButtonPanel
        buttons = new RegistrationButtonPanel(this.registrationController);
        panel = new ListPanel();

        this.add(panel);
        this.add(buttons);
        this.setVisible(true);
    }
}
