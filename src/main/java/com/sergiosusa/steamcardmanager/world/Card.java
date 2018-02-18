package com.sergiosusa.steamcardmanager.world;

import java.io.Serializable;

public class Card  implements Serializable {

    private static final long serialVersionUID = -1516226733091596775L;

    private String title;

    private int own;

    private int id;

    private String classId;

    public Card(int id, String title, int own)
    {
        this.id = id;
        this.title = title;
        this.own = own;
    }

    public Card(Card card) {

        this.id = card.id;
        this.title = card.title;
        this.own = 0;
        this.classId = card.getClassId();

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

    public void setOwn(int own) {
        this.own = own;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClassId() {
        return classId;
    }
}
