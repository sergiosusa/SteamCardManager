package com.sergiosusa.steamcardmanager.world;

import com.sergiosusa.steamcardmanager.world.utils.Client;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SteamBadgePageScraper implements Persistable {

    private final String steamUserId;
    private Client client;

    public SteamBadgePageScraper(Client client, String steamUserId) {
        this.client = client;
        this.steamUserId = steamUserId;
    }

    public boolean hasBadgePage(Game game) {

        String url = game.getBadgeUrl(steamUserId);
        return client.getStatusConnectionCode(url) == 200;
    }

    public Game scrapeBadgePage(Game game) {

        String url = game.getBadgeUrl(steamUserId);
        Document badgePage = client.getHtmlDocument(url);

        game.setBadgeLevel(
                extractBadgeLevel(badgePage)
        );

        if (game.getName().isEmpty()) {
           game.setName(extractGameTitle(badgePage));
        }

        Elements cards = extractGameCards(badgePage);

        if (!game.getLogo().isEmpty()){
            storeGameImages(game);
        }

        game.clearCards();

        for (Element card : cards) {
            Card objCard = extractGameCard(card);
            game.addCard(objCard);
        }

        return game;
    }

    private String extractGameTitle(Document badgePage) {
        return badgePage.getElementsByClass("badge_title").text().replace("Badge", "").trim();
    }

    private int extractBadgeLevel(Document badgePage) {
        String badgeInfoDescription = badgePage.select(".badge_info_description div:nth-child(2)").text();

        int level = 0;

        if (!badgeInfoDescription.isEmpty()) {
            level = Integer.valueOf(badgeInfoDescription.substring(badgeInfoDescription.indexOf(",") - 1, badgeInfoDescription.indexOf(",")));
        }
        return level;
    }

    private Elements extractGameCards(Document badgePage) {
        return badgePage.getElementsByClass("badge_card_set_card");
    }

    private Card extractGameCard(Element card) {
        String cardTitle = "", cardNumber = "";

        String quantity = card.getElementsByClass("badge_card_set_text_qty").text().replace("(", "").replace(")", "");
        String line = card.getElementsByClass("badge_card_set_text").text().replace("(" + quantity + ")", "");

        line = line.substring(0, line.lastIndexOf("of")).trim();
        cardTitle = line.substring(0, line.lastIndexOf(" ")).trim();
        cardNumber = line.substring(line.lastIndexOf(" "), line.length()).trim();

        return new Card(
                Integer.parseInt(cardNumber),
                cardTitle,
                0
        );
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
}
