package com.sergiosusa.steamcardmanager.world.utils;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class HttpClient implements Client {

    @Override
    public String getString(String path) {
        String doc = null;
        try {
            doc = Jsoup.connect(path).ignoreContentType(true).ignoreHttpErrors(true).timeout(10000).maxBodySize(0).execute().body();
        } catch (IOException ex) {
            System.out.println("Excepción al obtener el HTML de la página" + ex.getMessage());
        }
        return doc;
    }

    @Override
    public int getStatusConnectionCode(String path) {
        Connection.Response response;

        try {
            response = Jsoup.connect(path).userAgent("Mozilla/5.0").timeout(100000).ignoreHttpErrors(true).execute();

            if (response.statusCode() == 200) {
                if (!response.url().toString().equals(path)) {
                    return 302;
                }
            }
            return response.statusCode();
        } catch (IOException ex) {
            return 404;
        }
    }

    @Override
    public Document getHtmlDocument(String path) {
        Document doc = null;
        try {
            doc = Jsoup.connect(path).userAgent("Mozilla/5.0").timeout(100000).get();
        } catch (IOException ex) {
            System.out.println("Excepción al obtener el HTML de la página" + ex.getMessage());
        }
        return doc;
    }
}
