package com.sergiosusa.steamcardmanager.world.utils;

import org.jsoup.nodes.Document;

public class TestClient implements Client {

    private int statusCode;
    private String path;

    private FileClient client;

    public TestClient(int statusCode, String path, FileClient client) {
        this.statusCode = statusCode;
        this.path = path;
        this.client = client;
    }


    @Override
    public String getString(String path) {
        return client.getString(this.path);
    }

    @Override
    public int getStatusConnectionCode(String path) {
        return statusCode;
    }

    @Override
    public Document getHtmlDocument(String path) {
        return client.getHtmlDocument(this.path);
    }
}
