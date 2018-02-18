package com.sergiosusa.steamcardmanager.world.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class FileClient implements Client {

    @Override
    public String getString(String path) {

        StringBuilder result = new StringBuilder("");
        File file = new File(path);

        try (Scanner scanner = new Scanner(file)) {

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                result.append(line).append("\n");
            }
            scanner.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result.toString();
    }

    @Override
    public int getStatusConnectionCode(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return 404;
        }
        return 200;
    }

    @Override
    public Document getHtmlDocument(String path) {
        String page = getString(path);
        return Jsoup.parse(page);
    }
}
