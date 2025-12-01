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
    private final DashboardPanel dashboardPanel;
    private final HistoryPanel historyPanel;
    private final Sidebar sidebar;
    private final JPanel lockedPanel = new JPanel(new BorderLayout());

    public MainFrame() {
        super("Badminton Queue Admin");
        this.api = new ApiClient(new Config());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(960, 640);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        sidebar = new Sidebar(this::showCard);
        add(sidebar, BorderLayout.WEST);

        lockedPanel.add(new JLabel("Please log in to continue.", SwingConstants.CENTER), BorderLayout.CENTER);

        dashboardPanel = new DashboardPanel(api, statusBar);
        historyPanel = new HistoryPanel(api, statusBar);

        content.add(lockedPanel, "Locked");
        content.add(dashboardPanel, "Dashboard");
        content.add(historyPanel, "History");
        add(content, BorderLayout.CENTER);
        add(statusBar, BorderLayout.SOUTH);

        statusBar.setOnLogout(() -> {
            api.clearAuth();
            statusBar.info("Logged out");
            dashboardPanel.resetForLogout();
            historyPanel.resetForLogout();
            sidebar.setButtonsEnabled(false);
            statusBar.setLogoutEnabled(false);
            showCard("Locked");
            setVisible(false);
            promptLogin(true);
        });

        sidebar.setButtonsEnabled(false);
        statusBar.setLogoutEnabled(false);
        showCard("Locked");
        setVisible(false);
        promptLogin(false);
    }

    private void showCard(String name) {
        cardLayout.show(content, name);
    }

    private void promptLogin(boolean exitOnCancel) {
        LoginDialog dlg = new LoginDialog(this);
        while (true) {
            dlg.setLocationRelativeTo(null);
            dlg.setVisible(true);
            if (!dlg.isSuccess()) {
                if (exitOnCancel) {
                    dispose();
                    System.exit(0);
                }
                return;
            }
            try {
                boolean ok = api.login(dlg.getEmail(), dlg.getPassword());
                if (ok) {
                    statusBar.info("Logged in as " + api.getDisplayName() + " (" + api.getRole() + ")");
                    dashboardPanel.refreshTotals();
                    sidebar.setButtonsEnabled(true);
                    statusBar.setLogoutEnabled(true);
                    showCard("Dashboard");
                    setVisible(true);
                    break;
                } else {
                    JOptionPane.showMessageDialog(this, "Login failed", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Login error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
