package com.sergiosusa.steamcardmanager.graphic;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActionsMenu extends JPanel implements ActionListener {

    private static final String COMPLETE_BADGES = "COMPLETE_BADGES";
    private static final String SHOW_ALL = "SHOW_ALL";
    private static final String COMPLETABLE_BADGES = "COMPLETABLE_BADGES";
    private static final String MISSING_CARDS_BADGES = "MISSING_CARDS_BADGES" ;

    private final JTextField cardsMissing;

    private SteamCardManagerInterface window;

    public ActionsMenu(SteamCardManagerInterface window)
    {
        this.window = window;
        this.setLayout(new GridBagLayout());

        this.setBorder(new EmptyBorder(5,5,5,5));

        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 3;

        JButton showAll = new JButton("Show All Games");
        showAll.addActionListener(this);
        showAll.setActionCommand(SHOW_ALL);

        add(showAll, c);


        c.gridx = 4;
        c.gridy = 0;
        c.gridwidth = 3;

        JButton completeBadges = new JButton("Complete Badges");
        completeBadges.addActionListener(this);
        completeBadges.setActionCommand(COMPLETE_BADGES);

        add(completeBadges, c);

        c.gridx = 7;
        c.gridy = 0;
        c.gridwidth = 3;

        JButton completableBadges = new JButton("Completable Badges");
        completableBadges.addActionListener(this);
        completableBadges.setActionCommand(COMPLETABLE_BADGES);
        add(completableBadges, c);

        c.gridx = 10;
        c.gridy = 0;
        c.gridwidth = 2;
        c.weightx = 1;

        JLabel test = new JLabel();
        test.setPreferredSize(new Dimension(50,1));

        add(test, c);


        c.gridx = 13;
        c.gridy = 0;
        c.gridwidth = 1;
        c.weightx = 0.1;

        this.cardsMissing = new JTextField("1", 2);

        JLabel labelMiss = new JLabel("Missing cards to complete a badge: ");

        add(labelMiss, c);

        c.gridx = 14;
        c.gridy = 0;
        c.gridwidth = 1;
        c.weightx = 0.1;

        add(this.cardsMissing, c);

        c.gridx = 15;
        c.gridy = 0;
        c.gridwidth = 1;
        c.weightx = 0.1;

        JButton incompleteBadgesMissing = new JButton("Find");
        incompleteBadgesMissing.addActionListener(this);
        incompleteBadgesMissing.setActionCommand(MISSING_CARDS_BADGES);

        add(incompleteBadgesMissing, c);


    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getActionCommand().equals(COMPLETE_BADGES)) {
            window.executeCompleteBadges();
        }

        if (e.getActionCommand().equals(SHOW_ALL)) {
            window.executeShowAll();
        }

        if (e.getActionCommand().equals(COMPLETABLE_BADGES)) {
            window.executeCompletableBadges();
        }

        if (e.getActionCommand().equals(MISSING_CARDS_BADGES)) {

            window.executeMissCardBadges(Integer.valueOf(this.cardsMissing.getText()));
        }

    }
}
