package com.sergiosusa.steamcardmanager.world;

import java.io.*;
import java.util.Properties;

public class Configuration {

    private static final String STEAM_API_ID = "steam_api_id";
    private static final String STEAM_USER_ID_64 = "steam_user_id_64";
    private static final String STEAM_USER_ID = "steam_user_id";

    private String steamApiId;
    private String steamUserId64;
    private String steamUserId;

    public Configuration()
    {
        this.loadConfiguration();
    }

    private void loadConfiguration()
    {
        Properties prop = new Properties();
        InputStream input;
        FileOutputStream output;

        try {
            File file = new File("config.properties");
            if(file.exists() && !file.isDirectory()) {

                input = new FileInputStream(file);
                prop.load(input);

                String steamApiId = prop.getProperty("steam_api_id");
                String steamUserId64 = prop.getProperty("steam_user_id_64");
                String steamUserId = prop.getProperty("steam_user_id");

                this.steamApiId = steamApiId;
                this.steamUserId64 = steamUserId64;
                this.steamUserId = steamUserId;

            } else {

                prop.setProperty(Configuration.STEAM_API_ID, "");
                prop.setProperty(Configuration.STEAM_USER_ID, "");
                prop.setProperty(Configuration.STEAM_USER_ID_64, "");

                output = new FileOutputStream("config.properties");

                prop.store(output, null);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getSteamApiId() {
        return steamApiId;
    }

    public String getSteamUserId64() {
        return steamUserId64;
    }

    public String getSteamUserId() {
        return steamUserId;
    }

    public Object[][] getObjects()
    {
        Object[][] objects = {
                {STEAM_API_ID, this.getSteamApiId()},
                {STEAM_USER_ID_64, this.getSteamUserId64()},
                {STEAM_USER_ID, this.getSteamUserId()}
        };

        return objects;
    }

    public boolean isUnconfigured() {
        return getSteamUserId().isEmpty() || this.getSteamUserId64().isEmpty() || this.getSteamApiId().isEmpty();
    }
}
