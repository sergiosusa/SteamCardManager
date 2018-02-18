package com.sergiosusa.steamcardmanager.world;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameTest {

    private static final String DEFAULT_NAME = "My Game";
    private static final int DEFAULT_APP_ID = 12345;
    private static final String DEFAULT_ICON = "icon";
    private static final String DEFAULT_LOGO = "logo";

    private Game newGame;

    @BeforeEach
    void init() {
        newGame = new Game(
                DEFAULT_NAME,
                DEFAULT_APP_ID,
                DEFAULT_ICON,
                DEFAULT_LOGO
        );
    }

    @Test
    void createUsingParams() {

        assertEquals(DEFAULT_APP_ID, newGame.getAppId());
        assertEquals(DEFAULT_NAME, newGame.getName());
        assertEquals(DEFAULT_ICON, newGame.getIcon());
        assertEquals(DEFAULT_LOGO, newGame.getLogo());
        assertEquals(0, newGame.getTotalOwnCards());
    }

    @Test
    void createABaseGameFromGame() {
        Card[] newCards = {
                new Card(1, "My Card 1", 1),
                new Card(2, "My Card 2", 2),
                new Card(3, "My Card 3", 3),
                new Card(4, "My Card 4", 2),
                new Card(5, "My Card 5", 5),
        };

        for (Card card : newCards) {
            newGame.addCard(card);
        }

        Game newCloneGame = new Game(newGame);

        assertEquals(newGame.getAppId(), newCloneGame.getAppId());
        assertEquals(newGame.getName(), newCloneGame.getName());
        assertEquals(newGame.getIcon(), newCloneGame.getIcon());
        assertEquals(newGame.getLogo(), newCloneGame.getLogo());
        assertEquals(newGame.getIconPath(), newCloneGame.getIconPath());
        assertEquals(newGame.getLogoPath(), newCloneGame.getLogoPath());
        assertEquals(newGame.getBadgeLevel(), newCloneGame.getBadgeLevel());
        assertEquals(0, newCloneGame.getTotalOwnCards());
        assertEquals(5, newCloneGame.getTotalCards());
    }

    @Test
    void addingCardsToGame() {

        Card newCard = new Card(1, "My Card 1", 0);

        newGame.addCard(newCard);
        assertEquals(1, newGame.getTotalCards());

        newGame.addCard(newCard);
        assertEquals(1, newGame.getTotalCards());
    }


    @Test
    void countHowManyCardsOwn() {
        Card newCard = new Card(1, "My Card 1", 2);
        newGame.addCard(newCard);
        assertEquals(2, newGame.getTotalOwnCards());

        Card newCard2 = new Card(2, "My Card 2", 1);
        newGame.addCard(newCard2);
        assertEquals(3, newGame.getTotalOwnCards());
        assertEquals(2, newGame.getTotalCards());
    }

    @Test
    void checkAGameThatCanBeCompletedExchangingCards() {
        Card[] newCards = {
                new Card(1, "My Card 1", 0),
                new Card(2, "My Card 2", 0),
                new Card(3, "My Card 3", 0),
                new Card(4, "My Card 4", 3),
                new Card(5, "My Card 5", 2),
        };

        for (Card card : newCards) {
            newGame.addCard(card);
        }
        assertEquals(true, newGame.hasCompletableBadges());
    }

    @Test
    void checkAGameThatCantBeCompletedExchangingCards() {

        Card[] newCards = new Card[]{
                new Card(1, "My Card 1", 0),
                new Card(2, "My Card 2", 0),
                new Card(3, "My Card 3", 0),
                new Card(4, "My Card 4", 2),
                new Card(5, "My Card 5", 2),
        };

        for (Card card : newCards) {
            newGame.addCard(card);
        }
        assertEquals(false, newGame.hasCompletableBadges());

    }

    @Test
    void checkAGameThatAlreadyCompletedABadgeButCannotCompletedOneMore() {

        Card[] newCards = new Card[]{
                new Card(1, "My Card 1", 1),
                new Card(2, "My Card 2", 1),
                new Card(3, "My Card 3", 1),
                new Card(4, "My Card 4", 1),
                new Card(5, "My Card 5", 1),
        };

        for (Card card : newCards) {
            newGame.addCard(card);
        }
        assertEquals(false, newGame.hasCompletableBadges());
        newGame.clearCards();
    }

    @Test
    void checkAGameThatCanCompleteBadgesWithOneExtraCard() {

        Card[] newCards = {
                new Card(1, "My Card 1", 1),
                new Card(2, "My Card 2", 2),
                new Card(3, "My Card 3", 2),
                new Card(4, "My Card 4", 2),
                new Card(5, "My Card 5", 2),
        };

        for (Card card : newCards) {
            newGame.addCard(card);
        }
        assertEquals(true, newGame.canCompleteBadgeWith(1));
    }

    @Test
    void checkAGameThatCantCompleteBadgesWithOneExtraCard() {

        Card[] newCards = new Card[]{
                new Card(1, "My Card 1", 0),
                new Card(2, "My Card 2", 0),
                new Card(3, "My Card 3", 0),
                new Card(4, "My Card 4", 2),
                new Card(5, "My Card 5", 2),
        };

        for (Card card : newCards) {
            newGame.addCard(card);
        }
        assertEquals(false, newGame.canCompleteBadgeWith(1));
    }

    @Test
    void countHowManyBadgesHasAlreadyCompleted() {

        Card[] newCards = {
                new Card(1, "My Card 1", 1),
                new Card(2, "My Card 2", 2),
                new Card(3, "My Card 3", 3),
                new Card(4, "My Card 4", 2),
                new Card(5, "My Card 5", 5),
        };

        for (Card card : newCards) {
            newGame.addCard(card);
        }
        assertEquals(1, newGame.countBadgesAlreadyComplete());
        newGame.clearCards();

        newCards = new Card[]{
                new Card(1, "My Card 1", 0),
                new Card(2, "My Card 2", 0),
                new Card(3, "My Card 3", 0),
                new Card(4, "My Card 4", 2),
                new Card(5, "My Card 5", 2),
        };

        for (Card card : newCards) {
            newGame.addCard(card);
        }
        assertEquals(0, newGame.countBadgesAlreadyComplete());
    }

    @Test
    void assignAQuantityToACardOnAGameByItsName()
    {
        Card[] newCards = {
                new Card(1, "My Card 1", 0),
                new Card(2, "My Card 2", 0),
        };

        for (Card card : newCards) {
            newGame.addCard(card);
        }

        newGame.setOwnQuantityUsingName("My Card 1", 1);
        ArrayList<Card> cards = newGame.getCads();

        assertEquals(1, cards.get(0).getOwn());
        assertEquals(0, cards.get(1).getOwn());
    }

    @Test
    void assignClassIdToACardOnAGameByItsName()
    {
        Card[] newCards = {
                new Card(1, "My Card 1", 0),
                new Card(2, "My Card 2", 0),
        };

        for (Card card : newCards) {
            newGame.addCard(card);
        }

        newGame.setClassIdUsingName("My Card 1", "112212");
        ArrayList<Card> cards = newGame.getCads();

        assertEquals("112212", cards.get(0).getClassId());
        assertEquals(null, cards.get(1).getClassId());
    }
}