package com.badmintonqueue.admin.ui;

import com.badmintonqueue.admin.api.ApiClient;

import javax.swing.*;
import java.awt.*;

public class HistoryPanel extends JPanel {
    public HistoryPanel(ApiClient api, StatusBar status) {
        setLayout(new BorderLayout());
        add(new JLabel("History (read-only placeholder)", SwingConstants.CENTER), BorderLayout.CENTER);
    }
}
