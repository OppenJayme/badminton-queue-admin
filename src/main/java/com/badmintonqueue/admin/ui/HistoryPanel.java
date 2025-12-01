package com.badmintonqueue.admin.ui;

import com.badmintonqueue.admin.api.ApiClient;
import com.badmintonqueue.admin.api.AdminMatchDto;

import javax.swing.*;
import java.awt.*;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HistoryPanel extends JPanel {
    private final ApiClient api;
    private final StatusBar status;
    private final JTable table;
    private final HistoryTableModel tableModel = new HistoryTableModel();

    public HistoryPanel(ApiClient api, StatusBar status) {
        this.api = api;
        this.status = status;
        setLayout(new BorderLayout());
        var toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton refresh = new JButton("Refresh");
        toolbar.add(refresh);
        add(toolbar, BorderLayout.NORTH);

        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        add(new JScrollPane(table), BorderLayout.CENTER);

        refresh.addActionListener(e -> refreshHistory());
    }

    public void resetForLogout() {
        tableModel.setRows(java.util.Collections.emptyList());
    }

    public void refreshHistory() {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            List<AdminMatchDto> rows;

            @Override
            protected Void doInBackground() {
                api.getHistory(50).ifPresent(list -> rows = list);
                return null;
            }

            @Override
            protected void done() {
                if (rows != null) {
                    tableModel.setRows(rows);
                    status.info("History loaded");
                } else {
                    status.error("Failed to load history (requires admin)");
                }
            }
        };
        worker.execute();
    }

    private static class HistoryTableModel extends javax.swing.table.AbstractTableModel {
        private final String[] cols = new String[] { "Match", "Queue", "Session", "Mode", "Status", "Start", "Finish", "Duration", "Score" };
        private java.util.List<AdminMatchDto> rows = java.util.Collections.emptyList();
        private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        public void setRows(java.util.List<AdminMatchDto> rows) {
            this.rows = rows;
            fireTableDataChanged();
        }

        @Override
        public int getRowCount() {
            return rows.size();
        }

        @Override
        public int getColumnCount() {
            return cols.length;
        }

        @Override
        public String getColumnName(int column) {
            return cols[column];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            var m = rows.get(rowIndex);
            return switch (columnIndex) {
                case 0 -> m.id;
                case 1 -> m.queueName;
                case 2 -> m.sessionName == null ? "" : m.sessionName;
                case 3 -> m.mode;
                case 4 -> m.status;
                case 5 -> formatTime(m.startTime);
                case 6 -> formatTime(m.finishTime);
                case 7 -> formatDuration(m.durationSeconds);
                case 8 -> m.scoreText == null ? "" : m.scoreText;
                default -> "";
            };
        }

        private String formatTime(String t) {
            if (t == null || t.isBlank()) return "";
            try {
                var odt = OffsetDateTime.parse(t);
                return fmt.format(odt);
            } catch (Exception ex) {
                return t;
            }
        }

        private String formatDuration(Double seconds) {
            if (seconds == null) return "";
            var d = Duration.ofMillis((long)(seconds * 1000));
            long mins = d.toMinutes();
            long secs = d.minusMinutes(mins).getSeconds();
            return String.format("%dm %02ds", mins, secs);
        }
    }
}
