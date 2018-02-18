package com.sergiosusa.steamcardmanager.world;

import com.sergiosusa.steamcardmanager.world.utils.FileClient;
import com.sergiosusa.steamcardmanager.world.utils.TestClient;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SteamBadgePageScraperTest {

    @Test
    void checkIfAGameHasABadgePageWithA200ResponseFromClient()
    {
        SteamBadgePageScraper steamBadgePageScraper = new SteamBadgePageScraper(
                new TestClient(200, "src/test/resources/steamCardPage.html", new FileClient()),
                "s3rxus"
        );

        Game myGame = new Game("My Game", 123456, "","");
        assertTrue(steamBadgePageScraper.hasBadgePage(myGame));
    }

    @Test
    void checkIfAGameHasNotABadgePageWithA302ResponseFromClient()
    {
        SteamBadgePageScraper steamBadgePageScraper = new SteamBadgePageScraper(
                new TestClient(302, "src/test/resources/steamCardPage.html", new FileClient()),
                "s3rxus"
        );

        Game myGame = new Game("My Game", 123456, "","");
        assertFalse(steamBadgePageScraper.hasBadgePage(myGame));
    }

    @Test
    void checkIfScrappingProcessFillAllInformationToAGame()
    {
        SteamBadgePageScraper steamBadgePageScraper = new SteamBadgePageScraper(
                new TestClient(200, "src/test/resources/steamCardPage.html", new FileClient()),
                "s3rxus"
        );

        Game myGame = new Game("", 123456, "","");
        steamBadgePageScraper.scrapeBadgePage(myGame);

        assertEquals(5 ,myGame.getTotalCards());

        ArrayList<Card> cards = myGame.getCads();

        assertEquals("The Cosmic Lord" ,cards.get(0).getTitle());
        assertEquals("Sniper" ,cards.get(1).getTitle());
        assertEquals("Lord of the Wand" ,cards.get(2).getTitle());
        assertEquals("Storm Hammer" ,cards.get(3).getTitle());
        assertEquals("Yeaaah, Rock!" ,cards.get(4).getTitle());

        assertEquals(2, myGame.getBadgeLevel());
        assertEquals("EM: Shader Attack", myGame.getName());
    }
}