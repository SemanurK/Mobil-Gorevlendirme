package com.example.asus.denem;

public class Gorev_Detay2 {

    public Gorev_Detay2(String gorev_aciklama,String sontarih,int gorev_id ,int gorevlendiren_id)
    {
        super();
        this.gorev_aciklama=gorev_aciklama;
        this.gorevlendiren_id=gorevlendiren_id;
        this.son_tarih=sontarih;
        this.gorev_id=gorev_id;

    }
    public boolean isGorev_durum() {
        return gorev_durum;
    }

    public void setGorev_durum(boolean gorev_durum) {
        this.gorev_durum = gorev_durum;
    }

    private boolean gorev_durum;
    public int getGorev_id() {
        return gorev_id;
    }

    public void setGorev_id(int gorev_id) {
        this.gorev_id = gorev_id;
    }

    public String getGorev_aciklama() {
        return gorev_aciklama;
    }

    public void setGorev_aciklama(String gorev_aciklama) {
        this.gorev_aciklama = gorev_aciklama;
    }

    public String getSon_tarih() {
        return son_tarih;
    }

    public void setSon_tarih(String son_tarih) {
        this.son_tarih = son_tarih;
    }

    private int gorevlendiren_id;
    private int gorev_id;
    private  String gorev_aciklama;
    private String son_tarih;

    public int getGorevlendiren_id() {
        return gorevlendiren_id;
    }

    public void setGorevlendiren_id(int gorevlendiren_id) {
        this.gorevlendiren_id = gorevlendiren_id;
    }
}
