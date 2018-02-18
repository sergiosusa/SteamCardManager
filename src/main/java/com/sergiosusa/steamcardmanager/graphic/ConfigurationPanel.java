package com.sergiosusa.steamcardmanager.graphic;

import com.sergiosusa.steamcardmanager.world.Configuration;

import javax.swing.*;
import java.awt.*;

public class ConfigurationPanel extends JPanel{


    public ConfigurationPanel(Configuration configuration) {

        this.setSize(500, 300);
        this.setVisible(true);
        this.setLayout(new BorderLayout());

        String[] columnNames = {"Name",
                "Value"
        };

        Object[][] data = configuration.getObjects();

        JTable table = new JTable(data, columnNames);
        table.getColumn(0).setCellEditor(null);
        this.add(table, BorderLayout.CENTER);

        JPanel buttons = new JPanel();
        buttons.add(new JButton("Save"));

        this.add(buttons, BorderLayout.SOUTH);

    }
}
