package com.example.asus.denem;

import java.util.Date;

public class GorevAlanKisi {
    String ad_soyad;

    public String getGorev_aciklama() {
        return gorev_aciklama;
    }

    public void setGorev_aciklama(String gorev_aciklama) {
        this.gorev_aciklama = gorev_aciklama;
    }

    String gorev_aciklama;

    public GorevAlanKisi(String ad_soyad, String gorulme_tarih, String onaylanma_tarih, int gorev_id, int kullanici_id, boolean cinsiyet,String aciklama) {
        this.ad_soyad = ad_soyad;
        this.gorulme_tarih = gorulme_tarih;
        this.onaylanma_tarih = onaylanma_tarih;
        this.gorev_id = gorev_id;
        this.gorev_aciklama=aciklama;
        this.kullanici_id = kullanici_id;
        this.cinsiyet = cinsiyet;
    }

    String gorulme_tarih;
    String  onaylanma_tarih;

    public boolean isDurum() {
        return durum;
    }

    public void setDurum(boolean durum) {
        this.durum = durum;
    }

    boolean durum;

    public int getGorev_id() {
        return gorev_id;
    }

    public void setGorev_id(int gorev_id) {
        this.gorev_id = gorev_id;
    }

    public int getKullanici_id() {
        return kullanici_id;
    }

    public void setKullanici_id(int kullanici_id) {
        this.kullanici_id = kullanici_id;
    }

    int gorev_id,kullanici_id;



    public boolean isCinsiyet() {
        return cinsiyet;
    }

    public void setCinsiyet(boolean cinsiyet) {
        this.cinsiyet = cinsiyet;
    }

    boolean cinsiyet;


    public String getAd_soyad() {
        return ad_soyad;
    }

    public void setAd_soyad(String ad_soyad) {
        this.ad_soyad = ad_soyad;
    }

    public String getGorulme_tarih() {
        return gorulme_tarih;
    }

    public void setGorulme_tarih(String gorulme_tarih) {
        this.gorulme_tarih = gorulme_tarih;
    }

    public String getOnaylanma_tarih() {
        return onaylanma_tarih;
    }

    public void setOnaylanma_tarih(String onaylanma_tarih) {
        this.onaylanma_tarih = onaylanma_tarih;
    }


    /*public GorevAlanKisi(String ad_soyad, String gorulme_tarih, String onaylanma_tarih) {
        this.ad_soyad = ad_soyad;
        this.gorulme_tarih = gorulme_tarih;
        this.onaylanma_tarih = onaylanma_tarih;
    }*/


}
