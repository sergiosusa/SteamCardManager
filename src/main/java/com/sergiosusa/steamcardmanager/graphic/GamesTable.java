package com.sergiosusa.steamcardmanager.graphic;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class GamesTable extends JTable implements MouseListener {

    private String[] columnNames = {
            "",
            "AppId",
            "Name",
            "Badge Level (Max. 5)",
            "Badges Ready"
    };

    private SteamCardManagerInterface window;

    public GamesTable(SteamCardManagerInterface window, Object[][] gamesObj) {
        this.window = window;

        setData(gamesObj);

        this.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        this.addMouseListener(this);

    }

    private void adjustRows() {

        for (int row = 0; row < this.getRowCount(); row++) {
            int rowHeight = this.getRowHeight();

            for (int column = 0; column < this.getColumnCount(); column++) {
                Component comp = this.prepareRenderer(this.getCellRenderer(row, column), row, column);
                rowHeight = Math.max(rowHeight, comp.getPreferredSize().height);
            }
            this.setRowHeight(row, rowHeight);
        }
    }

    private DefaultTableModel getGamesModel(Object[][] gamesObj) {

        return new DefaultTableModel(gamesObj, columnNames) {
            public Class getColumnClass(int column) {

                switch (column) {
                    case 0:
                        return ImageIcon.class;
                    case 1:
                        return String.class;
                    case 2:
                        return Integer.class;
                    default:
                        return Integer.class;
                }
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        };
    }

    public void setData(Object[][] data) {

        DefaultTableModel gamesModel = getGamesModel(data);
        this.setModel(gamesModel);
        setAutoCreateRowSorter(true);
        adjustRows();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {

        Point point = e.getPoint();
        int row = rowAtPoint(point);
        int modelRow = convertRowIndexToModel(row);

        if (e.getClickCount() == 2) {
            String url = window.steamUrlBadgeByAppId((Integer) this.getModel().getValueAt(modelRow, 1));
            try {
                Desktop.getDesktop().browse(new URI(url));
            } catch (IOException | URISyntaxException e1) {
                e1.printStackTrace();
            }

        }

    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
