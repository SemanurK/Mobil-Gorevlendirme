package com.example.asus.denem;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Gorev_Arsivi extends AppCompatActivity {
    Bundle gelen;
    ListView arsiv_listesi;
    String al_id,al_kullanicilar;

    List<GorevAlanKisi> arsiv=new ArrayList<GorevAlanKisi>();
    Context context=this;
    ArsivAdapter adapter;
    String ip="172.31.129.130";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gorev__arsivi);
        gelen = getIntent().getExtras();
        arsiv_listesi=(ListView)findViewById(R.id.list_arsiv);
        al_id=gelen.getString("id");
      //  al_kullanicilar=gelen.getString("kullanicilar");
      // String url2="http://"+ip+":50491/api/Gorev/Get";
      //  url2+=al_id.toString();
     //   new arkaPlan2().execute(url2);
        String url="http://"+ip+":50491/api/GorevArsiv/Get/";
        url+=al_id;
        new arkaPlan().execute(url);

    }



 class arkaPlan extends AsyncTask<String, String, String>
    {
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
            try {
                JSONArray jsonArray=new JSONArray(s);
                JSONObject jsonObject=new JSONObject();
                for(int i=0; i<jsonArray.length(); i++)
                {
                    jsonObject=jsonArray.getJSONObject(i);
                    arsiv.add(new GorevAlanKisi(jsonObject.getString("Ad_soyad"),jsonObject.getString("Son_tarih"),jsonObject.getString("Onaylanma"),jsonObject.getInt("Gorev_id"),jsonObject.getInt("Kisi_id"),jsonObject.getBoolean("Cinsiyet"),jsonObject.getString("Gorev_aciklama")));
                }
                adapter = new ArsivAdapter(Gorev_Arsivi.this, arsiv);
                arsiv_listesi.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }

           /* String gorevlendiren_ad = null,gorev_aciklama,son_tarih;
             boolean cinsiyet = false;
            int gorev_id;
            String gorevlendiren_id;

            try {
                JSONArray jsonArray=new JSONArray(s);
                JSONArray jsonArray1=new JSONArray(al_kullanicilar);
                JSONObject jsonObject=new JSONObject();
                JSONObject jsonObject1=new JSONObject();
                JSONObject jsonObject2=new JSONObject();

                for(int i=0; i<jsonArray.length(); i++)
                {
                    jsonObject2=jsonArray.getJSONObject(i);
                    for(int k=0; k<gorevler.length(); k++) {
                       jsonObject=gorevler.getJSONObject(k);
                        if(jsonObject.getString("GOREV_ID").equals(jsonObject2.getString("GOREV_ID"))) {
                            gorevlendiren_id = jsonObject.getString("GOREVLENDİREN_ID");
                            for (int j = 0; j < jsonArray1.length(); j++) {
                                jsonObject1 = jsonArray1.getJSONObject(j);
                                if ((jsonObject1.getString("KUL_ID")).equals(gorevlendiren_id)) {
                                    gorevlendiren_ad = jsonObject1.getString("KUL_ADSOYAD");
                                    cinsiyet = jsonObject1.getBoolean("CINSIYET");
                                    break;
                                }
                            }
                            gorev_id = jsonObject.getInt("GOREV_ID");
                            gorev_aciklama = jsonObject.getString("GOREV_ACIKLAMA");
                            son_tarih = jsonObject.getString("SON_TARIH");
                            arsiv.add(new Gorev_Detay(gorevlendiren_ad, cinsiyet, gorev_aciklama, son_tarih, gorev_id));
                            break;
                        }

                    }
                }
                adapter = new CustomAdapter(Gorev_Arsivi.this, arsiv);

                arsiv_listesi.setAdapter(adapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }*/

        }
    }
}
