package com.badmintonqueue.admin.ui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public class Sidebar extends JPanel {
    private final java.util.List<JButton> buttons = new ArrayList<>();

    public Sidebar(Consumer<String> onSelect) {
        setLayout(new GridLayout(0, 1, 4, 4));
        setPreferredSize(new Dimension(180, 0));
        Map<String, String> items = new LinkedHashMap<>();
        items.put("Dashboard", "Dashboard");
        items.put("History", "History");
        items.forEach((card, label) -> {
            JButton btn = new JButton(label);
            btn.addActionListener(e -> onSelect.accept(card));
            add(btn);
            buttons.add(btn);
        });
    }

    public void setButtonsEnabled(boolean enabled) {
        buttons.forEach(b -> b.setEnabled(enabled));
    }
}
