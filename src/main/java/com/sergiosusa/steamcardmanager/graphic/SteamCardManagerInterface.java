package com.sergiosusa.steamcardmanager.graphic;

import com.sergiosusa.steamcardmanager.world.Configuration;
import com.sergiosusa.steamcardmanager.world.SteamCardManager;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class SteamCardManagerInterface extends JFrame {

    private static final String CONFIG_PROPERTIES_PATH = "config.properties";
    private final ActionsMenu buttonActionMenu;
    private final Menu menuBar;
    private GamesTable gamesTable;

    private final StatusPanel statusPanel;

    private Thread thread;

    private final SteamCardManager steamCardManager;

    private SteamCardManagerInterface(SteamCardManager steamCardManager) {

        this.steamCardManager = steamCardManager;

        menuBar = new Menu(this);
        this.setJMenuBar(menuBar);

        this.setTitle("Steam Card Manager");
        this.setSize(900, 700);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Object[][] gamesObj = this.steamCardManager.gamesWithCards();

        gamesTable = new GamesTable(this, gamesObj);

        JScrollPane gamesPanel = new JScrollPane(gamesTable);


        statusPanel = new StatusPanel();
        statusPanel.setCountGamesInformation(gamesObj.length);

        buttonActionMenu = new ActionsMenu(this);

        setLayout(new BorderLayout());
        add(gamesPanel, BorderLayout.CENTER);
        add(buttonActionMenu, BorderLayout.NORTH);
        add(statusPanel, BorderLayout.SOUTH);
    }

    public void showGamesWithCompleteBadges() {
        Object[][] gamesObj = this.steamCardManager.completeBadges();
        this.gamesTable.setData(gamesObj);
        int totalCompleteBadges = this.steamCardManager.countCompleteBadges();
        statusPanel.setCountGamesAndBadges(gamesObj.length, totalCompleteBadges);
    }

    public void showGamesWithCompletableBadges() {
        Object[][] gamesObj = this.steamCardManager.completableBadges();
        this.gamesTable.setData(gamesObj);
        statusPanel.setCountGamesInformation(gamesObj.length);
    }

    public void showGamesWithMissingCardsToCompleteBadge(int cardsMissing) {
        Object[][] gamesObj = this.steamCardManager.missingCards(cardsMissing);
        this.gamesTable.setData(gamesObj);
        statusPanel.setCountGamesInformation(gamesObj.length);
    }

    public void showGamesWithBadges() {
        Object[][] gamesObj = this.steamCardManager.gamesWithCards();
        this.gamesTable.setData(gamesObj);
        statusPanel.setCountGamesInformation(gamesObj.length);
    }

    public void showGamesWithExtraBadges() {
        Object[][] gamesObj = this.steamCardManager.gamesWithExtraBadges();
        this.gamesTable.setData(gamesObj);
        int totalBadges = this.steamCardManager.countTotalExtraBadges();
        statusPanel.setCountGamesAndBadges(gamesObj.length, totalBadges);
    }

    public void showInfoWindow() {

        JDialog frejm = new JDialog(this, "Information");

        ClassLoader classLoader = getClass().getClassLoader();
        JLabel info = new JLabel(new ImageIcon(classLoader.getResource("about.jpg").getFile()));
        info.setBounds(1, 1, 1, 1);

        frejm.getContentPane().add(info);

        frejm.setSize(500, 400);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frejm.setLocation(dim.width / 2 - frejm.getSize().width / 2, dim.height / 2 - frejm.getSize().height / 2);
        frejm.add(info);
        frejm.setModal(true);
        frejm.setVisible(true);
    }

    public void startScrapeProcess() {
        if (thread == null) {
            thread = new Thread(() -> {
                steamCardManager.scrapeGames(this.statusPanel);
                showGamesWithBadges();
                buttonActionMenu.changeProcessButtons(true);
                JOptionPane.showMessageDialog( null , "Process complete");
                thread = null;
            });
            thread.start();
        } else {
            JOptionPane.showMessageDialog(null, "We are loading your badges statusPanel, be patience.");
        }
    }

    public void selectAllGames() {
        this.gamesTable.selectAll();
    }

    private static void showToConfigureMessage() {
        try {
            JOptionPane.showMessageDialog(
                    null,
                    "Please fill the configuration file: config.properties.\n\n" +
                            "steam_api_id: Steam Web API Key.\n" +
                            "steam_user_id: account name.\n" +
                            "steam_user_id_64: steamID64.\n"
            );

            Desktop.getDesktop().open(new File(CONFIG_PROPERTIES_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void run() {
        Configuration configuration = new Configuration(CONFIG_PROPERTIES_PATH);

        if (configuration.isNotConfigured()) {
            showToConfigureMessage();
            Runtime.getRuntime().exit(1);
        }

        SteamCardManager steamCardManager = new SteamCardManager(configuration);

        SteamCardManagerInterface main = new SteamCardManagerInterface(steamCardManager);
        main.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SteamCardManagerInterface::run);
    }

    public String steamUrlBadgeByAppId(Integer appId) {
        return this.steamCardManager.steamUrlBadgeByAppId(appId);
    }
}
