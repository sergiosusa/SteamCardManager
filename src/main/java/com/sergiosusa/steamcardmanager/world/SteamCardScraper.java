package com.sergiosusa.steamcardmanager.world;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SteamCardScraper implements Persistable {

    private static final String STEAM_BADGE_URL = "http://steamcommunity.com/id/[steamUserId]/gamecards/[gameId]";

    private Configuration configuration;
    private ArrayList<Game> games;
    private HashMap<Integer, Game> gamesWithCards;
    private HashMap<Integer, Game> gamesWithoutCards;

    public SteamCardScraper(Configuration configuration, ArrayList<Game> games) {
        this.configuration = configuration;
        this.games = games;
        this.gamesWithCards = loadGamesAlreadyRead("cards");
        this.gamesWithoutCards = loadGamesAlreadyRead("noCards");
    }

    public HashMap<Integer, Game> scrapeGames(boolean startAgain) {

        if (startAgain) {
            this.gamesWithCards = new HashMap<>();
        }

        System.out.println("total: " + this.games.size());

        int batch = 1;

        try {
            for (Game game : this.games) {
                scrapCards(game);

                if (batch % 100 == 0) {
                    persistScraper();
                }
                System.out.print(batch);

                batch++;
            }

            return this.gamesWithCards;

        } catch (Exception e) {
            persistScraper();
        }
        return null;
    }

    private void persistScraper() {
        this.persist(this.gamesWithoutCards, "noCards");
        this.persist(this.gamesWithCards, "cards");
    }

    private String getSteamCardPageUrl(Game game) {
        return STEAM_BADGE_URL.replace("[gameId]", String.valueOf(game.getAppId())).replace("[steamUserId]", this.configuration.getSteamUserId());
    }

    private boolean hasAlreadyBeenScraped(Game game) {
        return this.gamesWithoutCards.containsKey(game.getAppId()) || this.gamesWithCards.containsKey(game.getAppId());
    }

    private void scrapCards(Game game) {

        String url = getSteamCardPageUrl(game);
        game.setBadgeUrl(url);

        if (hasAlreadyBeenScraped(game)) {
            return;
        }

        if (getStatusConnectionCode(url) != 200) {
            this.gamesWithoutCards.put(game.getAppId(), game);
            return;
        }

        System.out.println(game.getName() + "->" + url);

        Document document = getHtmlDocument(url);
        Elements cards = document.getElementsByClass("badge_card_set_card");

        if (cards.size() == 0) {
            this.gamesWithoutCards.put(game.getAppId(), game);
            return;
        }

        storeGameImages(game);

        for (Element card : cards) {
            Card objCard = buildCard(card);
            game.addCard(objCard);
        }

        this.gamesWithCards.put(game.getAppId(), game);
    }

    private void storeGameImages(Game game) {

        createGameFolder(game);

        String logoUrl = "http://media.steampowered.com/steamcommunity/public/images/apps/" + game.getAppId() + "/" + game.getLogo() + ".jpg";
        String filepathLogo = "images/" + game.getAppId() + "/" + game.getLogo() + ".jpg";
        game.setLogoPath(downloadImage(logoUrl, filepathLogo));

        String iconUrl = "http://media.steampowered.com/steamcommunity/public/images/apps/" + game.getAppId() + "/" + game.getIcon() + ".jpg";
        String filepathIcon = "images/" + game.getAppId() + "/" + game.getIcon() + ".jpg";
        game.setIconPath(downloadImage(iconUrl, filepathIcon));

    }

    private void createGameFolder(Game game) {
        File folder = new File("images/" + game.getAppId());

        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    private Card buildCard(Element card) {
        String quantity, cardName = "", cardNumber = "";

        quantity = card.getElementsByClass("badge_card_set_text_qty").text().replace("(", "").replace(")", "");
        String line = card.getElementsByClass("badge_card_set_text").text().replace("(" + quantity + ")", "");

        Pattern pattern = Pattern.compile("(\\D*)(\\d+)");
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            cardName = matcher.group(1);
            cardNumber = matcher.group(2);
        }

        if (quantity.equals("")) {
            quantity = "0";
        }

        int intQuantity = Integer.parseInt(quantity);
        int intCardNumber = Integer.parseInt(cardNumber);

        return new Card(intCardNumber, cardName, intQuantity);
    }

    private Document getHtmlDocument(String url) {

        Document doc = null;
        try {
            doc = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(100000).get();
        } catch (IOException ex) {
            System.out.println("Excepción al obtener el HTML de la página" + ex.getMessage());
        }
        return doc;
    }

    private int getStatusConnectionCode(String url) {

        Connection.Response response;

        try {
            response = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(100000).ignoreHttpErrors(true).execute();

            if (response.statusCode() == 200) {
                if (!response.url().toString().equals(url)) {
                    return 302;
                }
            }
            return response.statusCode();
        } catch (IOException ex) {
            return 404;
        }
    }

    private HashMap<Integer, Game> loadGamesAlreadyRead(String filename) {
        try {
            HashMap<Integer, Game> loadedGames = (HashMap<Integer, Game>) this.load(filename);
            return loadedGames;
        } catch (IOException | ClassNotFoundException e) {
            return new HashMap<>();
        }
    }

    public HashMap<Integer, Game> getGamesWithCards() {
        return this.gamesWithCards;
    }

}
