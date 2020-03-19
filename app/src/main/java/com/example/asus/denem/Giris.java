package com.example.asus.denem;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Giris extends AppCompatActivity {

    Button giris, kayitol,sifre_unuttum,kudret;
    EditText kulad, sifre;
    TextView tx;

    String ip="172.31.129.130";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris);
        giris = (Button) findViewById(R.id.btn_giris);
        kayitol=(Button)findViewById(R.id.kayıt_ol);
       List<Integer> list=new ArrayList<>();


     //   deneme = (Button) findViewById(R.id.button2);
        kulad = (EditText) findViewById(R.id.edt_kulad);
        sifre = (EditText) findViewById(R.id.edt_sifre);
        sifre_unuttum=(Button)findViewById(R.id.sifremi_unuttum);
    //    tx = (TextView) findViewById(R.id.txt);

        sifre_unuttum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ıntent = new Intent(Giris.this,Sifre_Yenileme.class);
                startActivity(ıntent);
            }
        });
       kayitol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ıntent = new Intent(Giris.this,Yeni_Kayit.class);
                startActivity(ıntent);
            }
        });
     giris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //String url="http://"+ip+":50491/api/Personel";
               // new arkaPlan3().execute(url);
              //  new arkaPlan3().execute("http://" + ip + ":50491/api/Personel/KullaniciTumBilgi/0");
                new arkaPlan3().execute("http://"+ip+":50491/api/Personel/KullaniciTumBilgi/0");
            }
        });

    }
    class arkaPlan3 extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {

            BufferedReader br = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(5 * 1000);
                connection.setReadTimeout(15 * 1000);

                connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                connection.setRequestProperty("Accept", "application/json");
                connection.setDoInput(true);
                connection.connect();
                InputStream is = connection.getInputStream();

                br = new BufferedReader(new InputStreamReader(is));
                String satir;
                String dosya = "";
                while ((satir = br.readLine()) != null) {
                    Log.d("satır:", satir);
                    dosya += satir;

                }
                return dosya;
            } catch (Exception e) {
                e.printStackTrace();

            }
            return "hata";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("executadan gelen", s);

            String kntrl_kulad, kntrl_sifre, karsılastır_kulad, karsılastır_sifre, id, ad_soyad,rol_ad;
            int bolum_id,rol_id;
            JSONObject jsonObject = new JSONObject();
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                kntrl_kulad = kulad.getText().toString();
                kntrl_sifre = sifre.getText().toString();
                String mesaj = "Sifre veya Kullanıcı adı yanlıştır";
                boolean kayitbulundu = false;
                Bundle bundle = new Bundle();

                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    karsılastır_kulad = jsonObject.getString("KUL_AD1");
                    if (kntrl_kulad.equals(karsılastır_kulad)) {
                        karsılastır_sifre = jsonObject.getString("SIFRE1");
                        if (kntrl_sifre.equals(karsılastır_sifre)) {
                            if(jsonObject.getBoolean("DURUM1")) {
                                if (jsonObject.getString("ROL_ADI1").equals("Rol belirlenmemiştir.")) {
                                    alterdialog("HATA", "Henüz yönetici tarafından rolünüz belirlenmemiştir.Bu yüzden sisteme giriş yapamazsınız.");
                                } else {
                                    ad_soyad = jsonObject.getString("KUL_ADSOYAD1");

                                    id = jsonObject.getString("KUL_ID1");
                                    bolum_id = jsonObject.getInt("BOLUM_ID1");
                                    rol_ad = jsonObject.getString("ROL_ADI1");
                                    rol_id = jsonObject.getInt("ROL_ID1");

                                    // Intent ıntent = new Intent(Giris.this, MenuGoruntule.class);
                                    Intent ıntent = new Intent(Giris.this, Menu2.class);
                                    bundle.putString("adsoyad", ad_soyad);
                                    bundle.putString("id", id);
                                    bundle.putString("json", String.valueOf(jsonObject));
                                    bundle.putInt("bolumid", bolum_id);
                                    bundle.putString("rol_ad", rol_ad);
                                    bundle.putInt("rol_id", rol_id);
                                    ıntent.putExtras(bundle);
                                    kayitbulundu = true;
                                    startActivity(ıntent);
                                    break;
                                }
                            }
                            else
                                alterdialog("PASİF KULLANICI","Sistemde kaydınız aktif değildir. Lütfen yetkili kullanıcı ile iletişime geçiniz.");
                        }
                        else
                            alterdialog("HATA","Şifreniz hatalıdır ! Tekrar deneyiniz");
                    }



                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
    public void alterdialog(String title,String mesaj)
    {
        android.app.AlertDialog.Builder builder1=new android.app.AlertDialog.Builder(Giris.this);
        builder1.setTitle(title);
        builder1.setMessage(mesaj);
        builder1.setNeutralButton("KAPAT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder1.show();
    }
}