package com.example.asus.denem;

public class Gorev_Detay {
    private String gorevlendiren_Ad;

    public int getKul_gorevID() {
        return kul_gorevID;
    }

    public void setKul_gorevID(int kul_gorevID) {
        this.kul_gorevID = kul_gorevID;
    }

    private int kul_gorevID;

    public int getGorev_id() {
        return gorev_id;
    }

    public void setGorev_id(int gorev_id) {
        this.gorev_id = gorev_id;
    }

    private int gorev_id;
    public Gorev_Detay(String gorevlendiren_Ad,boolean cinsiyet,String gorev_aciklama,String sontarih,int gorev_id,int kul_gorevID)
    {
        super();
        this.gorevlendiren_Ad=gorevlendiren_Ad;
        this.cinsiyet=cinsiyet;
        this.gorev_aciklama=gorev_aciklama;
        this.sontarih=sontarih;
        this.gorev_id=gorev_id;
        this.kul_gorevID=kul_gorevID;
    }
    public Gorev_Detay(String gorevlendiren_Ad,boolean cinsiyet,String gorev_aciklama,String sontarih,int gorev_id)
    {
        super();
        this.gorevlendiren_Ad=gorevlendiren_Ad;
        this.cinsiyet=cinsiyet;
        this.gorev_aciklama=gorev_aciklama;
        this.sontarih=sontarih;
        this.gorev_id=gorev_id;

    }

    private boolean gorev_durum;

    public String getGorevlendiren_Ad() {
        return gorevlendiren_Ad;
    }

    public void setGorevlendiren_Ad(String gorevlendiren_Ad) {
        this.gorevlendiren_Ad = gorevlendiren_Ad;
    }

    public boolean isCinsiyet() {
        return cinsiyet;
    }

    public void setCinsiyet(boolean cinsiyet) {
        this.cinsiyet = cinsiyet;
    }

    private boolean cinsiyet;

    public String getGorev_aciklama() {
        return gorev_aciklama;
    }

    public void setGorev_aciklama(String gorev_aciklama) {
        this.gorev_aciklama = gorev_aciklama;
    }

    private String gorev_aciklama;

    public String getSontarih() {
        return sontarih;
    }

    public void setSontarih(String sontarih) {
        this.sontarih = sontarih;
    }

    private String sontarih;

    public boolean isGorev_durum() {
        return gorev_durum;
    }

    public void setGorev_durum(boolean gorev_durum) {
        this.gorev_durum = gorev_durum;
    }
}
