package com.example.asus.denem;

public class Kullanicilar
{
    private  boolean durum;
    private int KUL_ID;
    private String TC;
    private  String KUL_ADSOYAD;
    private String KUL_AD;
    private String SIFRE;
    private  String DOGUM_T;
    private  String DOGUM_Y;
    private String ADRES;
    private  String CEP;
    private  boolean DURUM1;

    public String getSORU() {
        return SORU;
    }

    public void setSORU(String SORU) {
        this.SORU = SORU;
    }

    private String SORU;

    public String getCEVAP() {
        return CEVAP;
    }

    public void setCEVAP(String CEVAP) {
        this.CEVAP = CEVAP;
    }

    private String CEVAP;


    public int getBOLUM_ID() {
        return BOLUM_ID;
    }

    public void setBOLUM_ID(int BOLUM_ID) {
        this.BOLUM_ID = BOLUM_ID;
    }

    private  int BOLUM_ID;

    public String getBOLUM_ADI() {
        return BOLUM_ADI;
    }

    public void setBOLUM_ADI(String BOLUM_ADI) {
        this.BOLUM_ADI = BOLUM_ADI;
    }

    private String BOLUM_ADI;

    private String Rol_Ad;
    private boolean CINSIYET;
    private  String MAIL;
    public Kullanicilar(String KUL_ADSOYAD, boolean CINSIYET,int kul_id,String adres,String cep, String mail,String Rol_Ad,int bolum_id,boolean durum)
    {
        super();
        this.KUL_ADSOYAD=KUL_ADSOYAD;
        this.KUL_ID=kul_id;
        this.ADRES=adres;
        this.CEP=cep;
        this.MAIL=mail;this.BOLUM_ID=bolum_id;
        this.CINSIYET=CINSIYET;
        this.Rol_Ad=Rol_Ad;
        this.DURUM1=durum;
    }
    public Kullanicilar(String KUL_ADSOYAD,String KUL_AD,String TC,String sifre, boolean CINSIYET,int kul_id,String adres,String cep, String mail,String Rol_Ad,String Bolum_ad,int Bolum_Id,String soru, String cevap,boolean durum)
    {
        super();
        this.KUL_ADSOYAD=KUL_ADSOYAD;
        this.KUL_ID=kul_id;
        this.ADRES=adres;
        this.CEP=cep;
        this.KUL_AD=KUL_AD;
        this.TC=TC;
        this.MAIL=mail;
        this.CINSIYET=CINSIYET;
        this.Rol_Ad=Rol_Ad;
        this.BOLUM_ADI=Bolum_ad;
        this.BOLUM_ID=Bolum_Id;
        this.SIFRE=sifre;
        this.SORU=soru;
        this.DURUM1=durum;
        this.CEVAP=cevap;
    }
    public String getRol_Ad() {
        return Rol_Ad;
    }

    public void setRol_Ad(String rol_Ad) {
        Rol_Ad = rol_Ad;
    }

    public int getKUL_ID() {
        return KUL_ID;
    }

    public void setKUL_ID(int KUL_ID) {
        this.KUL_ID = KUL_ID;
    }

    public String getTC() {
        return TC;
    }

    public void setTC(String TC) {
        this.TC = TC;
    }

    public String getKUL_ADSOYAD() {
        return KUL_ADSOYAD;
    }

    public void setKUL_ADSOYAD(String KUL_ADSOYAD) {
        this.KUL_ADSOYAD = KUL_ADSOYAD;
    }

    public String getKUL_AD() {
        return KUL_AD;
    }

    public void setKUL_AD(String KUL_AD) {
        this.KUL_AD = KUL_AD;
    }

    public String getSIFRE() {
        return SIFRE;
    }

    public void setSIFRE(String SIFRE) {
        this.SIFRE = SIFRE;
    }

    public String getDOGUM_T() {
        return DOGUM_T;
    }

    public void setDOGUM_T(String DOGUM_T) {
        this.DOGUM_T = DOGUM_T;
    }

    public String getDOGUM_Y() {
        return DOGUM_Y;
    }

    public void setDOGUM_Y(String DOGUM_Y) {
        this.DOGUM_Y = DOGUM_Y;
    }

    public String getADRES() {
        return ADRES;
    }

    public void setADRES(String ADRES) {
        this.ADRES = ADRES;
    }

    public String getCEP() {
        return CEP;
    }

    public void setCEP(String CEP) {
        this.CEP = CEP;
    }

    public boolean isCINSIYET() {
        return CINSIYET;
    }

    public void setCINSIYET(boolean CINSIYET) {
        this.CINSIYET = CINSIYET;
    }

    public String getMAIL() {
        return MAIL;
    }

    public void setMAIL(String MAIL) {
        this.MAIL = MAIL;
    }


    public boolean isDurum() {
        return durum;
    }

    public void setDurum(boolean durum) {
        this.durum = durum;
    }


    public boolean isDURUM1() {
        return DURUM1;
    }

    public void setDURUM1(boolean DURUM1) {
        this.DURUM1 = DURUM1;
    }
}
/*class YeniKullanici extends Kullanicilar {
    public String getBolumAdi() {
        return BolumAdi;
    }

    public void setBolumAdi(String bolumAdi) {
        BolumAdi = bolumAdi;
    }

    private String BolumAdi;
    public YeniKullanici(String KUL_ADSOYAD, boolean CINSIYET, int kul_id, String adres, String cep, String mail, String Rol_Ad, String bolumAdi) {
        super(KUL_ADSOYAD, CINSIYET, kul_id, adres, cep, mail, Rol_Ad);
        BolumAdi = bolumAdi;
    }
}*/

