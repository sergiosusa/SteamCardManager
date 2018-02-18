package com.sergiosusa.steamcardmanager.world;

import javax.swing.*;
import java.io.IOException;
import java.util.HashMap;

public class SteamCardManager implements Persistable {

    private Configuration configuration;
    private HashMap<Integer, Game> games;
    private SteamCardScraper steamCardScraper;
    private SteamGamesReader steamGamesReader;

    public SteamCardManager(Configuration configuration) {
        this.configuration = configuration;
        this.games = loadGamesFile(configuration);
        this.steamGamesReader = new SteamGamesReader(this.configuration);
        this.steamCardScraper = new SteamCardScraper(this.configuration, this.steamGamesReader.read());
    }

    private HashMap<Integer, Game> loadGamesFile(Configuration configuration)  {
        try {
            return (HashMap<Integer, Game>) this.load(configuration.getSteamUserId());
        } catch (IOException | ClassNotFoundException e) {
            return new HashMap<>();
        }
    }

    public void scrapeGames(boolean startAgain) {
        this.games = this.steamCardScraper.scrapeGames(startAgain);
        this.persist(this.games, this.configuration.getSteamUserId());
    }

    public Object[][] completeBadges() {

        HashMap<Integer, Game> completeBadgesGames = new HashMap<>();

        int badgesComplete = 0;

        for (Game game : this.games.values()) {

            int totalBadgesComplete = game.countBadgesAlreadyComplete();

            if (totalBadgesComplete > 0) {
                completeBadgesGames.put(game.getAppId(), game);
            }

            badgesComplete += totalBadgesComplete;
        }

        return transformGamesToObject(completeBadgesGames);
    }

    public int countCompleteBadges()
    {
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
                    game.getBadgeUrl()
            };
            i++;
        }

        return result;
    }

    public Object[][] gamesWithCards() {
        return transformGamesToObject(this.games);
    }

    public String steamUrlBadgeByAppId(int appId) {
        Game game = this.games.get(appId);
        return game.getBadgeUrl();
    }

    public void loadPartialScrape() {
        this.games = this.steamCardScraper.getGamesWithCards();
        this.persist(this.games, this.configuration.getSteamUserId());
    }
}
