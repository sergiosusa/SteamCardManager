package com.sergiosusa.steamcardmanager.world.utils;

import org.jsoup.nodes.Document;

public interface Client {

    public String getString(String path);

    public int getStatusConnectionCode(String path);

    public Document getHtmlDocument(String path);

}
