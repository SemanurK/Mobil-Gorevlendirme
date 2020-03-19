package com.example.asus.denem;

public class IzinModel {
    String MenuAdi;

    public IzinModel(String menuAdi, int rol_id, int menu_id, boolean durum) {
        MenuAdi = menuAdi;
        Rol_id = rol_id;
        Menu_id = menu_id;
        this.durum = durum;
    }

    public String getMenuAdi() {
        return MenuAdi;
    }

    public void setMenuAdi(String menuAdi) {
        MenuAdi = menuAdi;
    }

    public int getRol_id() {
        return Rol_id;
    }

    public void setRol_id(int rol_id) {
        Rol_id = rol_id;
    }

    public int getMenu_id() {
        return Menu_id;
    }

    public void setMenu_id(int menu_id) {
        Menu_id = menu_id;
    }

    public boolean isDurum() {
        return durum;
    }

    public void setDurum(boolean durum) {
        this.durum = durum;
    }

    int Rol_id,Menu_id;
    boolean durum;
}
