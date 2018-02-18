package com.sergiosusa.steamcardmanager.world;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;

public class SteamGamesReader {

    private static final String STEAM_OWNED_GAMES_ENDPOINT = "http://api.steampowered.com/IPlayerService/GetOwnedGames/v0001/?key=[steamApiId]&steamid=[steamUserId]&include_appinfo=1";

    private Configuration configuration;

    public SteamGamesReader(Configuration configuration)
    {
        this.configuration = configuration;
    }

    public ArrayList<Game> read()
    {

        ArrayList<Game> gamesList = new ArrayList<Game>();

        String ownGamesUrl = STEAM_OWNED_GAMES_ENDPOINT.replace("[steamUserId]", configuration.getSteamUserId64()).replace("[steamApiId]", configuration.getSteamApiId());

        String jsonStr = this.getString(ownGamesUrl);

        JSONObject array = new JSONObject(jsonStr);
        JSONArray newArray = array.getJSONObject("response").getJSONArray("games");

        for (int i = 0; i < newArray.length(); i++) {

            int appId = newArray.getJSONObject(i).getInt("appid");
            String name = newArray.getJSONObject(i).getString("name");
            String logo = newArray.getJSONObject(i).getString("img_logo_url");
            String icon = newArray.getJSONObject(i).getString("img_icon_url");

            gamesList.add(new Game(
                    name,
                    appId,
                    icon,
                    logo
            ));
        }

        return gamesList;
    }

    private String getString(String url) {
        String doc = null;
        try {
            doc = Jsoup.connect(url).ignoreContentType(true).maxBodySize(0).execute().body();
        } catch (IOException ex) {
            System.out.println("Excepción al obtener el HTML de la página" + ex.getMessage());
        }
        return doc;
    }
}
