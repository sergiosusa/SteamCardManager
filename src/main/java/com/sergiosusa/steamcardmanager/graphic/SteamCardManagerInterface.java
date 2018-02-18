package com.sergiosusa.steamcardmanager.graphic;

import com.sergiosusa.steamcardmanager.world.Configuration;
import com.sergiosusa.steamcardmanager.world.SteamCardManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

public class SteamCardManagerInterface extends JFrame implements MouseListener, ActionListener {

    private final JLabel total;
    private final SteamCardManager steamCardManager;
    private final JMenu menuLoad;
    private ActionsMenu mainMenu;

    private JTable gamesTable;
    private Thread t;
    private JMenuItem submenuStopProcess;


    public SteamCardManagerInterface(SteamCardManager steamCardManager) {
        this.steamCardManager = steamCardManager;

        JMenuBar menuBar = new JMenuBar();

        this.menuLoad = new JMenu("Load");

        JMenuItem submenuLoad = new JMenuItem("New reading from Steam");
        submenuLoad.addActionListener(this);
        submenuLoad.setActionCommand("LOAD");
        menuLoad.add(submenuLoad);

        JMenuItem submenuContinueLoad = new JMenuItem("Continue reading from Steam");
        submenuContinueLoad.addActionListener(this);
        submenuContinueLoad.setActionCommand("CONTINUE_LOAD");
        menuLoad.add(submenuContinueLoad);

        JMenuItem submenuExit = new JMenuItem("Exit");
        submenuExit.addActionListener(this);
        submenuExit.setActionCommand("EXIT");
        menuLoad.add(submenuExit);

        JMenu menuOptions = new JMenu("Options");
        JMenuItem menuItemSettings = new JMenuItem("Settings");
        menuOptions.add(menuItemSettings);

        JMenu menuHelp = new JMenu("Help");

        JMenuItem menuInfo = new JMenuItem("About");
        menuInfo.addActionListener(this);
        menuInfo.setActionCommand("INFO");
        menuHelp.add(menuInfo);

        menuBar.add(menuLoad);
        menuBar.add(menuOptions);
        menuBar.add(menuInfo);

        this.setJMenuBar(menuBar);
        this.setTitle("Steam Card Manager");
        this.setSize(900, 700);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Object[][] gamesObj = this.steamCardManager.gamesWithCards();
        gamesTable = buildGamesTable(gamesObj);

        JScrollPane gamesPanel = new JScrollPane(gamesTable);

        this.setLayout(new BorderLayout());
        this.add(gamesPanel, BorderLayout.CENTER);

        this.mainMenu = new ActionsMenu(this);
        this.add(this.mainMenu, BorderLayout.NORTH);

        JPanel status = new JPanel();

        this.total = new JLabel();
        total.setText("Total: " + gamesObj.length);
        status.add(this.total);

        this.add(status, BorderLayout.SOUTH);

    }

    private JTable buildGamesTable(Object[][] gamesObj) {
        JTable gamesTable = new JTable(getGamesModel(gamesObj));

        for (int row = 0; row < gamesTable.getRowCount(); row++) {
            int rowHeight = gamesTable.getRowHeight();

            for (int column = 0; column < gamesTable.getColumnCount(); column++) {
                Component comp = gamesTable.prepareRenderer(gamesTable.getCellRenderer(row, column), row, column);
                rowHeight = Math.max(rowHeight, comp.getPreferredSize().height);
            }

            gamesTable.setRowHeight(row, rowHeight);
        }

        gamesTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        gamesTable.addMouseListener(this);

        return gamesTable;
    }


    public void executeCompleteBadges() {
        Object[][] gamesObj = this.steamCardManager.completeBadges();

        int totalCompleteBadges = this.steamCardManager.countCompleteBadges();

        this.gamesTable.setModel(getGamesModel(gamesObj));
        total.setText("Total: " + gamesObj.length + " Games - " + totalCompleteBadges + " Complete Badges");
        formatTable();

    }

    public void executeCompletableBadges() {

        Object[][] gamesObj = this.steamCardManager.completableBadges();
        this.gamesTable.setModel(getGamesModel(gamesObj));
        total.setText("Total: " + gamesObj.length + " Games");
        formatTable();

    }

    public void executeMissCardBadges(int cardsMissing) {

        Object[][] gamesObj = this.steamCardManager.missingCards(cardsMissing);
        this.gamesTable.setModel(getGamesModel(gamesObj));
        total.setText("Total: " + gamesObj.length + " Games");
        formatTable();

    }

    private DefaultTableModel getGamesModel(Object[][] gamesObj) {

        String[] columnNames = {"", "AppId",
                "Name",
        };

        return new DefaultTableModel(gamesObj, columnNames) {
            //  Returning the Class of each column will allow different
            //  renderers to be used based on Class
            public Class getColumnClass(int column) {
                return getValueAt(0, column).getClass();
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }

        };
    }

    public void executeShowAll() {

        Object[][] gamesObj = this.steamCardManager.gamesWithCards();
        this.gamesTable.setModel(getGamesModel(gamesObj));
        formatTable();
        total.setText("Total: " + gamesObj.length + " Games");
        gamesTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    }

    private void formatTable() {
        for (int row = 0; row < this.gamesTable.getRowCount(); row++) {
            int rowHeight = this.gamesTable.getRowHeight();

            for (int column = 0; column < this.gamesTable.getColumnCount(); column++) {
                Component comp = this.gamesTable.prepareRenderer(this.gamesTable.getCellRenderer(row, column), row, column);
                rowHeight = Math.max(rowHeight, comp.getPreferredSize().height);
            }

            gamesTable.setRowHeight(row, rowHeight);
        }

        gamesTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

        JTable table = (JTable) e.getSource();
        Point point = e.getPoint();
        int row = table.rowAtPoint(point);

        if (e.getClickCount() == 2) {

            String url = this.steamCardManager.steamUrlBadgeByAppId((Integer) table.getModel().getValueAt(row, 1));

            try {
                Desktop.getDesktop().browse(new URI(url));
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (URISyntaxException e1) {
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


    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getActionCommand().equals("INFO")) {
            this.showInfoWindow();
        }

        if (e.getActionCommand().equals("LOAD")) {

            if (t == null) {

                submenuStopProcess = new JMenuItem("Stop Loading Process");
                submenuStopProcess.addActionListener(this);
                submenuStopProcess.setActionCommand("STOP_PROCESS");
                this.menuLoad.add(submenuStopProcess, 2);

                t = new Thread(new Runnable() {
                    public void run() {
                        steamCardManager.scrapeGames(true);
                    }
                });
                t.start();
            } else {
                JOptionPane.showMessageDialog(null, "We are loading your badges information, be patience.");
            }

        }

        if (e.getActionCommand().equals("CONTINUE_LOAD")) {
            if (t == null) {

                submenuStopProcess = new JMenuItem("Stop Loading Process");
                submenuStopProcess.addActionListener(this);
                submenuStopProcess.setActionCommand("STOP_PROCESS");
                this.menuLoad.add(submenuStopProcess, 2);

                t = new Thread(new Runnable() {
                    public void run() {
                        steamCardManager.scrapeGames(false);
                    }
                });

                t.start();
            } else {
                JOptionPane.showMessageDialog(null, "We are loading your badges information, be patience.");
            }

        }

        if (e.getActionCommand().equals("EXIT")) {
            System.exit(0);
        }

        if (e.getActionCommand().equals("STOP_PROCESS")) {
            t.interrupt();
            t.stop();
            t = null;
            this.menuLoad.remove(this.submenuStopProcess);

            this.steamCardManager.loadPartialScrape();
            this.executeShowAll();

        }

    }

    private void showInfoWindow() {

        JDialog frejm = new JDialog(this, "Information");

        ClassLoader classLoader = getClass().getClassLoader();
        JLabel info = new JLabel(new ImageIcon(classLoader.getResource("about.jpg").getFile()));
        info.setBounds(1, 1, 1, 1);

        frejm.getContentPane().add(info);

        /* info.setBorder(new EmptyBorder(5,5,5,5));*/


        frejm.setSize(500, 400);
        //info.setPreferredSize(frejm.getPreferredSize());
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frejm.setLocation(dim.width / 2 - frejm.getSize().width / 2, dim.height / 2 - frejm.getSize().height / 2);
        frejm.add(info);
        frejm.setModal(true);
        frejm.setVisible(true);


    }
}
