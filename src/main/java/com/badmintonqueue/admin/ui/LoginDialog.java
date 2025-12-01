package com.badmintonqueue.admin.ui;

import javax.swing.*;
import java.awt.*;

public class LoginDialog extends JDialog {
    private boolean success = false;
    private final JTextField emailField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();

    public LoginDialog(Frame owner) {
        super(owner, "Admin Login", true);
        setLayout(new BorderLayout(8, 8));
        JPanel form = new JPanel(new GridLayout(0, 1, 4, 4));
        form.add(new JLabel("Email:"));
        form.add(emailField);
        form.add(new JLabel("Password:"));
        form.add(passwordField);
        add(form, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton ok = new JButton("Login");
        JButton cancel = new JButton("Cancel");
        ok.addActionListener(e -> {
            success = true;
            setVisible(false);
        });
        cancel.addActionListener(e -> {
            success = false;
            setVisible(false);
        });
        buttons.add(ok);
        buttons.add(cancel);
        add(buttons, BorderLayout.SOUTH);

        setSize(320, 180);
        setLocationRelativeTo(owner);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getEmail() {
        return emailField.getText().trim();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }
}
