package com.example.asus.denem;

public class BolumModel {
    public String getBolum_adi() {
        return bolum_adi;
    }

    public void setBolum_adi(String bolum_adi) {
        this.bolum_adi = bolum_adi;
    }

    public int getBolum_id() {
        return bolum_id;
    }

    public void setBolum_id(int bolum_id) {
        this.bolum_id = bolum_id;
    }

    public BolumModel(String bolum_adi, int bolum_id) {
        this.bolum_adi = bolum_adi;
        this.bolum_id = bolum_id;
    }

    public String bolum_adi;
    public int bolum_id;

    @Override
    public String toString() {
        return  bolum_adi
               ;
    }
}
