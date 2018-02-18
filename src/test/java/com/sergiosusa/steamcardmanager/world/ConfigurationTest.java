package com.sergiosusa.steamcardmanager.world;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConfigurationTest {

    @Test
    void tryToLoadAnEmptyConfigurationFile()
    {
        Configuration configuration = new Configuration("src/test/resources/empty_config.properties");

        assertTrue(configuration.getSteamApiId().isEmpty());
        assertTrue(configuration.getSteamUserId().isEmpty());
        assertTrue(configuration.getSteamUserId64().isEmpty());
        assertTrue(configuration.isNotConfigured());
    }

    @Test
    void loadAFilledConfiguration()
    {
        Configuration configuration = new Configuration("src/test/resources/config.properties");

        assertEquals("ABCDEFGHIJK1234567890", configuration.getSteamApiId());
        assertEquals("test_user", configuration.getSteamUserId());
        assertEquals("123123123123", configuration.getSteamUserId64());
        assertFalse(configuration.isNotConfigured());
    }
}