package com.sergiosusa.steamcardmanager.world;

import com.sergiosusa.steamcardmanager.world.utils.Client;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class SteamApiReader {

    private static final String STEAM_OWNED_GAMES_ENDPOINT = "http://api.steampowered.com/IPlayerService/GetOwnedGames/v0001/?key=[steamApiId]&steamid=[steamUserId]&include_appinfo=1";

    private static final String STEAM_INVENTORY_ENDPOINT = "https://steamcommunity.com/id/[steamUserId]/inventory/json/753/6/?trading=1&start=[limit]";

    private Client client;
    private Configuration configuration;

    public SteamApiReader(Client client, Configuration configuration) {
        this.client = client;
        this.configuration = configuration;
    }

    public ArrayList<Game> getGamesFromSteamApi(String steamUserId64, String steamApiId) {

        ArrayList<Game> gamesList = new ArrayList<>();

        String ownGamesUrl = STEAM_OWNED_GAMES_ENDPOINT.replace("[steamUserId]", steamUserId64).replace("[steamApiId]", steamApiId);

        String jsonStr = client.getString(ownGamesUrl);

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

    public HashMap<String, String[]> readInventory() {
        int limit = 0;
        boolean go = true;
        HashMap<String, String[]> result = new HashMap<>();

        while (go) {
            String inventoryUrl = STEAM_INVENTORY_ENDPOINT.
                    replace("[steamUserId]", configuration.getSteamUserId()).
                    replace("[limit]", String.valueOf(limit));

            System.out.println(inventoryUrl);

            String jsonStr = null;

            while(null == jsonStr || jsonStr.equals("null")){
                jsonStr = client.getString(inventoryUrl);
            }
            JSONObject array = new JSONObject(jsonStr);

            JSONObject inventoryPage = array.getJSONObject("rgInventory");
            Iterator<?> ikeys = inventoryPage.keys();

            HashMap<String, String[]> tmp = new HashMap<>();

            while (ikeys.hasNext()) {
                String key = (String) ikeys.next();
                if (inventoryPage.get(key) instanceof JSONObject) {
                    JSONObject node = (JSONObject) inventoryPage.get(key);
                    String classId = node.getString("classid");
                    String amount = node.getString("amount");
                    String[] data = {
                            classId,
                            amount
                    };

                    if (tmp.containsKey(classId)){
                        tmp.get(classId)[1] = String.valueOf(Integer.parseInt(tmp.get(classId)[1])+1);
                    } else{
                        tmp.put(classId, data);
                    }
                }
            }

            JSONObject inventaryPage = array.getJSONObject("rgDescriptions");

            Iterator<?> keys = inventaryPage.keys();

            while (keys.hasNext()) {
                String key = (String) keys.next();
                if (inventaryPage.get(key) instanceof JSONObject) {

                    JSONObject node = (JSONObject) inventaryPage.get(key);

                    int appId = node.getInt("market_fee_app");
                    String classId = node.getString("classid");
                    String name = node.getString("name");
                    
                    if (!node.getString("type").contains("Trading Card")) {
                        continue;
                    }

                    String[] res = {
                            String.valueOf(appId),
                            classId,
                            name,
                            tmp.get(classId)[1]
                    };

                    result.put(classId, res);
                }
            }

            if (tryParseInt(array.get("more_start").toString())) {
                limit = array.getInt("more_start");
            } else {
                go=false;
            }
        }

        return result;
    }

    boolean tryParseInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
