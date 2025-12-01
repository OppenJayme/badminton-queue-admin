package com.badmintonqueue.admin.ui;

import javax.swing.*;
import java.awt.*;

public class StatusBar extends JPanel {
    private final JLabel label = new JLabel("Ready");
    private final JButton logoutButton = new JButton("Logout");
    private Runnable onLogout;

    public StatusBar() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));
        add(label, BorderLayout.WEST);
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
        logoutButton.addActionListener(e -> {
            if (onLogout != null) onLogout.run();
        });
        right.add(logoutButton);
        add(right, BorderLayout.EAST);
    }

    public void info(String text) {
        label.setText(text);
    }

    public void error(String text) {
        label.setText("Error: " + text);
    }

    public void setOnLogout(Runnable onLogout) {
        this.onLogout = onLogout;
    }

    public void setLogoutEnabled(boolean enabled) {
        logoutButton.setEnabled(enabled);
    }
}
