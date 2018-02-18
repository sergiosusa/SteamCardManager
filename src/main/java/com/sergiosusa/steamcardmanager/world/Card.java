package com.sergiosusa.steamcardmanager.world;

import java.io.Serializable;

public class Card  implements Serializable {

    private static final long serialVersionUID = -1516226733091596775L;

    private String title;

    private int own;

    private int id;

    public Card(int id, String title, int own)
    {
        this.id = id;
        this.title = title;
        this.own = own;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getOwn() {
        return own;
    }
}
