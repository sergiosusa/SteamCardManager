package com.sergiosusa.steamcardmanager.graphic;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu extends JMenuBar implements ActionListener {

    private static final String EXIT = "EXIT";
    private static final String INFO = "INFO";

    private SteamCardManagerInterface window;

    public Menu(SteamCardManagerInterface steamCardManagerInterface) {

        window = steamCardManagerInterface;

        JMenu menuOptions = new JMenu("Options");

        JMenuItem submenuExit = new JMenuItem("Exit");
        submenuExit.addActionListener(this);
        submenuExit.setActionCommand(EXIT);
        menuOptions.add(submenuExit);

        JMenu menuHelp = new JMenu("Help");

        JMenuItem menuInfo = new JMenuItem("About");
        menuInfo.addActionListener(this);
        menuInfo.setActionCommand(INFO);
        menuHelp.add(menuInfo);

        add(menuOptions);
        add(menuHelp);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String actionCommand = e.getActionCommand();

        switch (actionCommand) {
            case EXIT:
                System.exit(0);
                break;
            case INFO:
                window.showInfoWindow();
                break;
        }
    }
}
