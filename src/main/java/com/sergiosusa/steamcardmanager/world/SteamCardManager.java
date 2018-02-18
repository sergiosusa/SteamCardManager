package com.sergiosusa.steamcardmanager.world;

import com.sergiosusa.steamcardmanager.graphic.StatusPanel;
import com.sergiosusa.steamcardmanager.world.utils.HttpClient;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SteamCardManager implements Persistable {

    private Configuration configuration;

    private HashMap<Integer, Game> games;
    private HashMap<Integer, Game> baseGames;
    private HashMap<Integer, Game> gamesWithoutCards;


    private SteamBadgePageScraper steamBadgePageScraper;
    private SteamApiReader steamApiReader;

    public SteamCardManager(Configuration configuration) {
        this.configuration = configuration;
        this.baseGames = loadBaseGameFile();
        this.games = loadGamesFile(configuration);
        this.gamesWithoutCards = loadGamesAlreadyRead("noCards");
        HttpClient client = new HttpClient();
        this.steamApiReader = new SteamApiReader(client, this.configuration);
        this.steamBadgePageScraper = new SteamBadgePageScraper(client, this.configuration.getSteamUserId());
    }

    private HashMap<Integer, Game> loadGamesFile(Configuration configuration) {
        try {
            return (HashMap<Integer, Game>) this.load(configuration.getSteamUserId());
        } catch (IOException | ClassNotFoundException e) {
            return new HashMap<>();
        }
    }

    private HashMap<Integer, Game> loadBaseGameFile() {
        try {
            return (HashMap<Integer, Game>) this.load("baseGames");
        } catch (IOException | ClassNotFoundException e) {
            return new HashMap<>();
        }
    }


    public void scrapeGames(StatusPanel statusPanel) {

        this.games = new HashMap<>();

        ArrayList<Game> steamGames = this.steamApiReader.getGamesFromSteamApi(
                configuration.getSteamUserId64(),
                configuration.getSteamApiId()
        );

        int gamesProcessed = 1;

        for (Game game : steamGames) {

            try {

                statusPanel.setProgressBarValue(gamesProcessed, steamGames.size());
                gamesProcessed++;

                if (baseGames.containsKey(game.getAppId())) {
                    Game foundGame = new Game(baseGames.get(game.getAppId()));
                    games.put(foundGame.getAppId(), foundGame);
                    continue;
                }

                if (gamesWithoutCards.containsKey(game.getAppId())) {
                    continue;
                }

                if (!steamBadgePageScraper.hasBadgePage(game)) {
                    gamesWithoutCards.put(game.getAppId(), game);
                    continue;
                }

                steamBadgePageScraper.scrapeBadgePage(game);

                if (0 == game.getTotalCards()) {
                    gamesWithoutCards.put(game.getAppId(), game);
                    continue;
                }

                baseGames.put(game.getAppId(), game);
                games.put(game.getAppId(), game);

            } catch (Exception e) {
                persistFiles();
            }
        }
        persistFiles();

        HashMap<String, String[]> rest = steamApiReader.readInventory();

        Iterator it = rest.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();

            String[] data = (String[])pair.getValue();

            Game baseGame = baseGames.get(Integer.parseInt(data[0]));
            if (null != baseGame) {
                baseGame.setClassIdUsingName(data[2], data[1]);
            } else {

                Game newGame =  new Game("", Integer.parseInt(data[0]), "","");

                if(!steamBadgePageScraper.hasBadgePage(newGame)){
                    gamesWithoutCards.put(newGame.getAppId(), newGame);
                } else {
                    steamBadgePageScraper.scrapeBadgePage(newGame);
                    newGame.setClassIdUsingName(data[2], data[1]);
                    baseGames.put(newGame.getAppId(), newGame);
                    baseGame=newGame;
                }
            }

            Game game = games.get(Integer.parseInt(data[0]));
            if (null != game) {
                game.setClassIdUsingName(data[2], data[1]);
                game.setOwnQuantityUsingName(data[2], Integer.parseInt(data[3]));
            } else {

                if (null != baseGame){
                    baseGame.setClassIdUsingName(data[2], data[1]);
                    baseGame.setOwnQuantityUsingName(data[2], Integer.parseInt(data[3]));
                    games.put(baseGame.getAppId(), baseGame);
                }

            }
            it.remove(); // avoids a ConcurrentModificationException
        }
        persistFiles();
    }

    private void persistFiles() {
        this.persist(this.baseGames, "baseGames");
        this.persist(this.games, this.configuration.getSteamUserId());
        this.persist(this.gamesWithoutCards, "noCards");
    }

    public Object[][] completeBadges() {

        HashMap<Integer, Game> completeBadgesGames = new HashMap<>();

        for (Game game : this.games.values()) {

            int totalBadgesComplete = game.countBadgesAlreadyComplete();

            if (totalBadgesComplete > 0) {
                completeBadgesGames.put(game.getAppId(), game);
            }
        }

        return transformGamesToObject(completeBadgesGames);
    }

    public int countCompleteBadges() {
        int badgesComplete = 0;

        for (Game game : this.games.values()) {
            badgesComplete += game.countBadgesAlreadyComplete();
        }
        return badgesComplete;
    }


    public Object[][] completableBadges() {

        HashMap<Integer, Game> completeBadgesGames = new HashMap<>();

        for (Game game : this.games.values()) {
            if (game.hasCompletableBadges()) {
                completeBadgesGames.put(game.getAppId(), game);
            }
        }

        return transformGamesToObject(completeBadgesGames);
    }

    public Object[][] missingCards(int howManyMissCards) {

        HashMap<Integer, Game> completeBadgesGames = new HashMap<>();

        for (Game game : this.games.values()) {
            if (game.canCompleteBadgeWith(howManyMissCards)) {
                completeBadgesGames.put(game.getAppId(), game);
            }

        }
        return transformGamesToObject(completeBadgesGames);
    }

    public Object[][] transformGamesToObject(HashMap<Integer, Game> games) {

        Object[][] result = new Object[games.size()][2];

        int i = 0;
        for (Game game : games.values()) {

            ImageIcon icon = new ImageIcon(game.getLogoPath());

            result[i] = new Object[]{
                    icon,
                    game.getAppId(),
                    game.getName(),
                    game.getBadgeLevel(),
                    game.countBadgesAlreadyComplete(),
                    game.getBadgeUrl(configuration.getSteamUserId()),
            };
            i++;
        }

        return result;
    }

    public Object[][] gamesWithCards() {
        return transformGamesToObject(this.games);
    }

    public Object[][] gamesWithExtraBadges() {

        HashMap<Integer, Game> extraBadgesGames = new HashMap<>();

        for (Game game : this.games.values()) {
            if (game.hasExtraBadges()) {
                extraBadgesGames.put(game.getAppId(), game);
            }

        }
        return transformGamesToObject(extraBadgesGames);
    }

    public String steamUrlBadgeByAppId(int appId) {
        Game game = this.games.get(appId);
        return game.getBadgeUrl(configuration.getSteamUserId());
    }

    public void loadPartialScrape() {

    }

    private HashMap<Integer, Game> loadGamesAlreadyRead(String filename) {
        try {
            HashMap<Integer, Game> loadedGames = (HashMap<Integer, Game>) this.load(filename);
            return loadedGames;
        } catch (IOException | ClassNotFoundException e) {
            return new HashMap<>();
        }
    }

    public int countTotalExtraBadges() {
         int totalExtraBadges = 0;

        for (Game game : this.games.values()) {
            if (game.hasExtraBadges()) {
                totalExtraBadges += game.countExtraBadges();
            }
        }
        return totalExtraBadges;
    }
}
