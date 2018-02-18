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

    private String propertiesPath;

    public Configuration(String propertiesPath)
    {
        this.propertiesPath = propertiesPath;
        this.loadProperties();
    }

    private void loadProperties()
    {
        Properties prop = new Properties();
        InputStream input;
        FileOutputStream output;

        try {
            File file = new File(propertiesPath);
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

                output = new FileOutputStream(this.propertiesPath);

                prop.store(output, null);
                output.close();
            }

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

    public boolean isNotConfigured() {
        return isNullOrEmpty(steamUserId) || isNullOrEmpty(steamUserId64) || isNullOrEmpty(steamApiId);
    }

    private boolean isNullOrEmpty(String value)
    {
        return null == steamUserId || value.isEmpty();
    }
}
