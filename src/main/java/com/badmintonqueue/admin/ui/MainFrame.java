package com.badmintonqueue.admin.ui;

import com.badmintonqueue.admin.api.ApiClient;
import com.badmintonqueue.admin.config.Config;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel content = new JPanel(cardLayout);
    private final StatusBar statusBar = new StatusBar();
    private final ApiClient api;

    public MainFrame() {
        super("Badminton Queue Admin");
        this.api = new ApiClient(new Config());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(960, 640);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        Sidebar sidebar = new Sidebar(this::showCard);
        add(sidebar, BorderLayout.WEST);

        content.add(new DashboardPanel(api, statusBar), "Dashboard");
        content.add(new HistoryPanel(api, statusBar), "History");
        add(content, BorderLayout.CENTER);
        add(statusBar, BorderLayout.SOUTH);

        showCard("Dashboard");
    }

    private void showCard(String name) {
        cardLayout.show(content, name);
    }
}
