package com.sergiosusa.steamcardmanager.graphic;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class ActionsMenu extends JPanel implements ActionListener {

    private static final String ACTION_START_SCRAPE_PROCESS = "ACTION_START_SCRAPE_PROCESS";
    private static final String ACTION_SHOW_GAMES_WITH_BADGES = "ACTION_SHOW_GAMES_WITH_BADGES";
    private static final String ACTION_SHOW_GAMES_WITH_COMPLETABLE_BADGES = "ACTION_SHOW_GAMES_WITH_COMPLETABLE_BADGES";
    private static final String ACTION_SHOW_GAMES_WITH_COMPLETE_BADGES = "ACTION_SHOW_GAMES_WITH_COMPLETE_BADGES";
    private static final String ACTION_SHOW_GAMES_WITH_MISSING_CARDS_TO_COMPLETE_BADGE = "ACTION_SHOW_GAMES_WITH_MISSING_CARDS_TO_COMPLETE_BADGE";
    private static final String ACTION_SELECT_ALL_GAMES = "ACTION_SELECT_ALL_GAMES";
    private static final String ACTION_SHOW_GAMES_WITH_EXTRA_BADGES = "ACTION_SHOW_GAMES_WITH_EXTRA_BADGES";

    private String actualFilter = ACTION_SHOW_GAMES_WITH_BADGES;

    private final JTextField cardsMissing;

    private final JButton startButton;

    private SteamCardManagerInterface window;

    public ActionsMenu(SteamCardManagerInterface window) {
        this.window = window;

        this.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;

        startButton = createImageButton("load.png", ACTION_START_SCRAPE_PROCESS, "Start a new import");
        add(startButton, c);

        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 1;

        JButton selectAll = createImageButton("selectAll.png", ACTION_SELECT_ALL_GAMES, "Select all results");
        add(selectAll, c);

        c.gridx = 2;
        c.gridy = 0;
        c.gridwidth = 1;

        JLabel emptyLabel = new JLabel();
        emptyLabel.setPreferredSize(new Dimension(45, 45));
        emptyLabel.setLayout(new BorderLayout());
        add(emptyLabel, c);

        c.gridx = 4;
        c.gridy = 0;
        c.gridwidth = 1;



        c.gridx = 5;
        c.gridy = 0;
        c.gridwidth = 1;


        c.gridx = 6;
        c.gridy = 0;
        c.gridwidth = 1;


        c.gridx = 7;
        c.gridy = 0;
        c.gridwidth = 1;

        JButton showAll = createImageButton("show_all.png", ACTION_SHOW_GAMES_WITH_BADGES, "Show all games with cards");
        add(showAll, c);

        c.gridx = 8;
        c.gridy = 0;
        c.gridwidth = 1;

        JButton completeBadges = createImageButton("complete.png", ACTION_SHOW_GAMES_WITH_COMPLETE_BADGES, "Show all complete games with badges");
        add(completeBadges, c);

        c.gridx = 9;
        c.gridy = 0;
        c.gridwidth = 1;

        JButton completableBadges = createImageButton("completable.png", ACTION_SHOW_GAMES_WITH_COMPLETABLE_BADGES, "Show all completable games badges");
        add(completableBadges, c);

        c.gridx = 10;
        c.gridy = 0;
        c.gridwidth = 1;

        JButton extraBadges = createImageButton("more.png", ACTION_SHOW_GAMES_WITH_EXTRA_BADGES, "Show Badges with extra badges");
        add(extraBadges, c);


        c.gridx = 13;
        c.gridy = 0;
        c.gridwidth = 2;
        c.weightx = 1;

        JLabel test = new JLabel();
        test.setPreferredSize(new Dimension(70, 1));
        add(test, c);


        c.gridx = 16;
        c.gridy = 0;
        c.gridwidth = 1;
        c.weightx = 0.1;

        JLabel labelMiss = new JLabel("Filter by missing cards to complete badges: ");
        add(labelMiss, c);

        c.gridx = 17;
        c.gridy = 0;
        c.gridwidth = 1;

        this.cardsMissing = new JTextField("1", 2);
        this.cardsMissing.setPreferredSize(new Dimension(10, 35));
        this.cardsMissing.setHorizontalAlignment(JTextField.CENTER);

        add(this.cardsMissing, c);

        c.gridx = 18;
        c.gridy = 0;
        c.gridwidth = 1;
        c.weightx = 0.1;

        JButton incompleteBadgesMissing = createImageButton("find.png", ACTION_SHOW_GAMES_WITH_MISSING_CARDS_TO_COMPLETE_BADGE, "Show games that missing a number of card to be completable");
        add(incompleteBadgesMissing, c);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        executeAction(actionCommand);
    }

    private void executeAction(String actionCommand) {
        switch (actionCommand) {
            case ACTION_START_SCRAPE_PROCESS:
                changeProcessButtons(false);
                window.startScrapeProcess();
                break;
            case ACTION_SHOW_GAMES_WITH_BADGES:
                this.actualFilter = ACTION_SHOW_GAMES_WITH_BADGES;
                window.showGamesWithBadges();
                break;
            case ACTION_SHOW_GAMES_WITH_COMPLETE_BADGES:
                this.actualFilter = ACTION_SHOW_GAMES_WITH_COMPLETE_BADGES;
                window.showGamesWithCompleteBadges();
                break;
            case ACTION_SHOW_GAMES_WITH_COMPLETABLE_BADGES:
                this.actualFilter = ACTION_SHOW_GAMES_WITH_COMPLETABLE_BADGES;
                window.showGamesWithCompletableBadges();
                break;
            case ACTION_SHOW_GAMES_WITH_MISSING_CARDS_TO_COMPLETE_BADGE:
                this.actualFilter = ACTION_SHOW_GAMES_WITH_MISSING_CARDS_TO_COMPLETE_BADGE;
                window.showGamesWithMissingCardsToCompleteBadge(Integer.valueOf(this.cardsMissing.getText()));
                break;
            case ACTION_SHOW_GAMES_WITH_EXTRA_BADGES:
                window.showGamesWithExtraBadges();
                break;
            case ACTION_SELECT_ALL_GAMES:
                window.selectAllGames();
                break;
        }
    }

    public void changeProcessButtons(boolean enabledStartButton) {
        this.startButton.setEnabled(enabledStartButton);
    }

    private JButton createImageButton(String icon, String action, String toolTipText) {
        JButton button = new JButton();
        try {
            ClassLoader classLoader = getClass().getClassLoader();

            URL url = classLoader.getResource("buttons/" + icon);
            Image image = new ImageIcon(url).getImage();
            button.setIcon(new ImageIcon(image.getScaledInstance(45, 45, Image.SCALE_SMOOTH)));
            setBorder(LineBorder.createGrayLineBorder());
            button.setPreferredSize(new Dimension(45, 45));
            button.setPreferredSize(new Dimension(45, 45));
            button.addActionListener(this);
            button.setActionCommand(action);
            button.setToolTipText(toolTipText);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return button;
    }

    public void reExecuteFilter() {
        executeAction(actualFilter);
    }
}
