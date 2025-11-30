package com.badmintonqueue.admin;

import javax.swing.SwingUtilities;
import com.badmintonqueue.admin.ui.MainFrame;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
