package com.sergiosusa.steamcardmanager.world;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    private static final int DEFAULT_ID = 1;
    private static final String DEFAULT_TITLE = "My Card";
    private static final int DEFAULT_OWN = 5;
    private Card newCard;

    @BeforeEach
    void init() {
        newCard = new Card(
                DEFAULT_ID,
                DEFAULT_TITLE,
                DEFAULT_OWN
        );
    }

    @Test
    void createUsingParams() {
        assertEquals(DEFAULT_ID, newCard.getId());
        assertEquals(DEFAULT_TITLE, newCard.getTitle());
        assertEquals(DEFAULT_OWN, newCard.getOwn());
    }

    @Test
    void createABaseCardFromCard()
    {
        Card newCloneCard = new Card(newCard);

        assertEquals(DEFAULT_ID, newCloneCard.getId());
        assertEquals(DEFAULT_TITLE, newCloneCard.getTitle());
        assertEquals(0, newCloneCard.getOwn());
    }
}