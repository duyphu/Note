package com.example.note.model;

/**
 * Created by phund on 3/4/2016.
 */
public class DialogItem {
    public final String text;
    public final int icon;
    public DialogItem(String text, Integer icon) {
        this.text = text;
        this.icon = icon;
    }
    @Override
    public String toString() {
        return text;
    }
}
