package com.sergiosusa.steamcardmanager.graphic;

import javax.swing.*;
import java.awt.*;

public class StatusPanel extends JPanel {

    private final JLabel informationLabel;
    private final JProgressBar processBar;

    public StatusPanel() {
        setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;

        processBar = new JProgressBar();
        add(processBar, c);

        c.gridx = 3;
        c.gridy = 0;
        c.gridwidth = 1;
        c.weightx = 2;

        this.informationLabel = new JLabel();
        add(informationLabel, c);
    }

    public void changeInformation(String information) {
        this.informationLabel.setText(information);
    }

    public void setCountGamesInformation(int totalGames) {
        this.changeInformation("Total: " + totalGames + " Games");
    }

    public void setCountGamesAndBadges(int totalGames, int totalBadges) {
        this.changeInformation("Total: " + totalGames + " Games - " + totalBadges + " Complete Badges");
    }

    public void setProgressBarValue(int progress, int total) {
        processBar.setValue(progress*100/total);
        processBar.setStringPainted(true);
    }
}
