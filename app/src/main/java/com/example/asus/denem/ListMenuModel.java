package com.example.asus.denem;

public class ListMenuModel {
    public String getMenuAdi() {
        return menuAdi;
    }

    public void setMenuAdi(String menuAdi) {
        this.menuAdi = menuAdi;
    }

    private  String menuAdi;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    private String icon;
    public ListMenuModel(String menuAdi, String icon)
    {
        super();
        this.menuAdi=menuAdi;
        this.icon = icon;
    }
}
