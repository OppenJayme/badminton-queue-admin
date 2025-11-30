package com.badmintonqueue.admin.ui;

import javax.swing.*;
import java.awt.*;

public class StatusBar extends JPanel {
    private final JLabel label = new JLabel("Ready");

    public StatusBar() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));
        add(label, BorderLayout.WEST);
    }

    public void info(String text) {
        label.setText(text);
    }

    public void error(String text) {
        label.setText("Error: " + text);
    }
}
