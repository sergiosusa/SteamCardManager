package com.sergiosusa.steamcardmanager.world;

import java.io.Serializable;
import java.util.ArrayList;

public class Game implements Serializable {

    private static final long serialVersionUID = 7104142720453297105L;

    private int appId;
    private String name;
    private String icon;
    private String iconPath;
    private String logo;
    private String logoPath;
    private String badgeUrl;

    private ArrayList<Card> cards;

    public Game(String name, int appId, String icon, String logo) {
        this.name = name;
        this.appId = appId;
        this.icon = icon;
        this.logo = logo;
        this.cards = new ArrayList<>();
    }

    public void addCard(Card card) {
        if (!this.cards.contains(card)) {
            this.cards.add(card);
        }
    }

    private int getTotalCards() {
        return this.cards.size();
    }

    public int getTotalOwnCards() {
        int totalOwned = 0;

        for (Card card : this.cards) {
            totalOwned += card.getOwn();
        }
        return totalOwned;
    }

    public boolean hasCompletableBadges() {
        int completeBadges = this.countBadgesAlreadyComplete();
        int totalOwnedCards = this.getTotalOwnCards();
        int totalCards = this.getTotalCards();

        if (totalOwnedCards - (completeBadges * totalCards) >= totalCards) {
            return true;
        }
        return false;
    }

    public boolean canCompleteBadgeWith(int howManyMissCards) {

        int totalOwnCards = this.getTotalOwnCards();
        int totalCards = this.getTotalCards();
        int completeBadges = this.countBadgesAlreadyComplete();
        int cardsThatCompleteBadges = completeBadges * totalCards;

        return (totalOwnCards - cardsThatCompleteBadges + howManyMissCards) == totalCards && this.hasMinimumOneCardOn(howManyMissCards, completeBadges);
    }

    private boolean hasMinimumOneCardOn(int missCards, int completeBadges) {

        int totalCards = this.getTotalCards();
        int totalCardWithOneCardOn = 0;

        for (Card card : this.cards) {
            if (card.getOwn() - completeBadges > 0) {
                totalCardWithOneCardOn++;
            }
        }

        return totalCards - missCards == totalCardWithOneCardOn;
    }

    public int countBadgesAlreadyComplete() {

        int minimum = 10000;

        for (Card card : this.cards) {
            if (minimum > card.getOwn()) {
                minimum = card.getOwn();
            }
        }
        return minimum;
    }

    public int getAppId() {
        return appId;
    }

    public String getName() {
        return name;
    }

    public String getIcon() {
        return icon;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public String getLogo() {
        return logo;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public String getBadgeUrl() {
        return badgeUrl;
    }

    public void setBadgeUrl(String badgeUrl) {
        this.badgeUrl = badgeUrl;
    }

}
