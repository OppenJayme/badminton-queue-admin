package com.badmintonqueue.admin.ui;

import com.badmintonqueue.admin.api.ApiClient;
import com.badmintonqueue.admin.api.TotalsDto;

import javax.swing.*;
import java.awt.*;

public class DashboardPanel extends JPanel {
    private final ApiClient api;
    private final StatusBar status;
    private final JLabel totalsLabel = new JLabel("Loading...");

    public DashboardPanel(ApiClient api, StatusBar status) {
        this.api = api;
        this.status = status;
        setLayout(new BorderLayout());
        add(new JLabel("Dashboard", SwingConstants.CENTER), BorderLayout.NORTH);
        add(totalsLabel, BorderLayout.CENTER);
        refreshTotals();
    }

    private void refreshTotals() {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            TotalsDto totals;

            @Override
            protected Void doInBackground() {
                api.getTotals().ifPresent(t -> totals = t);
                return null;
            }

            @Override
            protected void done() {
                if (totals != null) {
                    totalsLabel.setText(String.format(
                            "<html>Total queues: %d<br/>Total sessions: %d<br/>Users: %d<br/>Players: %d<br/>Matches: %d</html>",
                            totals.totalQueues, totals.totalSessions, totals.totalUsers, totals.totalPlayers, totals.totalMatches));
                    status.info("Totals loaded");
                } else {
                    totalsLabel.setText("Totals unavailable");
                    status.error("Failed to load totals (requires admin endpoint /api/admin/totals)");
                }
            }
        };
        worker.execute();
    }
}
