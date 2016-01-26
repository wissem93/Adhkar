package com.example.root.adhkar.classobject;

/**
 * Created by root on 22/07/15.
 */
public class ItemMenu {
   private String title;
    private int icon;

    public ItemMenu(String title,int icon) {
        this.title=title;
        this.icon=icon;
    }

    public int getIcon() {
        return icon;
    }

    public String getTitle() {
        return title;
    }

    public ItemMenu(int icon) {
        this.icon = icon;
    }
}
