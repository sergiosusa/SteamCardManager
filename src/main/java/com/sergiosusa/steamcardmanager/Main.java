package com.sergiosusa.steamcardmanager;

import com.sergiosusa.steamcardmanager.graphic.SteamCardManagerInterface;
import com.sergiosusa.steamcardmanager.world.*;

import javax.swing.*;
import java.util.Scanner;

public class Main {

    private static final int LOAD_STEAM_INFO = 0;
    private static final int CONTINUE_LOAD_STEAM_INFO = 1;
    private static final int COMPLETABLE_BADGES = 2;
    private static final int MISSING_CARDS = 3;
    private static final int COMPLETE_BADGES = 4;
    private static final int EXIT = 10;

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {

                System.out.flush();

                Configuration configuration = new Configuration();

                if (configuration.isUnconfigured()) {
            /*System.out.println("Welcome to Steam Badges Tool");
            System.out.println("-------------------------\n");
            System.out.println("Please complete the config.properties file to start.\n");*/
                    Runtime.getRuntime().exit(1);
                    return;
                }

                SteamCardManager steamCardManager = new SteamCardManager(configuration);

                SteamCardManagerInterface main = new SteamCardManagerInterface(steamCardManager);
                main.setVisible(true);


            }
        });



        /*int process = -1;

        while (process != EXIT) {

            process = showMenu(configuration);

            if (process == LOAD_STEAM_INFO) {
                steamCardManager.scrapeGames(true);
            }  else if (process == CONTINUE_LOAD_STEAM_INFO) {
                steamCardManager.scrapeGames(false);
            }else if (process == COMPLETABLE_BADGES) {
                steamCardManager.completableBadges();
            } else if (process == MISSING_CARDS) {

                Scanner scanner = new Scanner(System.in);
                System.out.println("how many cards to complete a badges are you interested?");
                System.out.println("-------------------------\n");
                int howManyMissCards = scanner.nextInt();
                steamCardManager.missingCards(howManyMissCards);

            } else if (process == COMPLETE_BADGES) {
                steamCardManager.completeBadges();
            } else if (process == EXIT) {
                Runtime.getRuntime().exit(1);
            }

        }*/
    }

    private static int showMenu(Configuration configuration) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to Steam Badges Tool");
        System.out.println("-------------------------\n");
        System.out.println("What do you like to do: ");
        System.out.println("-------------------------\n");
        System.out.println("0 - Read Badges Information From Steam");
        System.out.println("1 - Continue Read Badges Information From Steam");
        System.out.println("2 - List Completable Badges");
        System.out.println("3 - List Incomplete Badges missing X cards");
        System.out.println("4 - Complete Badges");
        System.out.println("10 - Exit");

        return scanner.nextInt();
    }
}
