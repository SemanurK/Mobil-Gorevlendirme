package com.example.asus.denem;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Gorev extends AppCompatActivity {

    Bundle gelen,bdn;
    String al_adsoyad, al_id,al_kullanicilar;
    TextView tx,goruldu;
    ListView gorev_listesi;

    List<Gorev_Detay> gelen_gorevler=new ArrayList<Gorev_Detay>();
    Context context=this;
    Button gorev_tamamla;
    CustomAdapter adapter;

    String kul_gorevler;
    String url;
    String ip="172.31.129.130";
    String gorulme_tarihi;
    String onaylanma_tarihi;

    //tarih
    SimpleDateFormat bicim2=new SimpleDateFormat("dd-M-yyyy");
    Date tarih=new Date();
    Time today = new Time(Time.getCurrentTimezone());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gorev);
        gelen = getIntent().getExtras();

        gorev_listesi=(ListView)findViewById(R.id.list_gorev);

        gorev_tamamla=(Button)findViewById(R.id.gorev_tamamla);
        goruldu=(TextView)findViewById(R.id.goruldu);

        al_adsoyad = gelen.getString("adsoyad");
        al_id = gelen.getString("id");
        al_kullanicilar=gelen.getString("kullanicilar");

        //SİSTEMİN SAATİ ALINIR. KULLANICININ GÖREVLERİ GÖRDÜĞÜ SAAT

        today.setToNow();
        gorulme_tarihi=bicim2.format(tarih)+" "+today.format("%k:%M:%S");
        //goruldu.setText(bicim2.format(tarih)+" "+today.format("%k:%M:%S"));
        JSONObject jsonObject=new JSONObject();
        try {
            String url_gorulme_tarihi="http://"+ip+":50491/api/Kullanici_Gorev/Put2";
            jsonObject.put("KULLANICI_ID",al_id);
            jsonObject.put("GORULME_TARIH",gorulme_tarihi);
            VeriGuncelle veriGuncelle=new VeriGuncelle();
            veriGuncelle.webservis(url_gorulme_tarihi,jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        String url_kulGorev="http://"+ip+":50491/api/Kullanici_Gorev";
      //  new arkaPlan2().execute(url_kulGorev);
        url="http://"+ip+":50491/api/Gorev/Kul_Gorev/";
        url+=al_id.toString();
        new arkaPlan().execute(url);


        //LİSTVİEWDEKİ VERİLER SEÇİLDİĞİNDE
        final List<Gorev_Detay> tamamlananlar=new ArrayList<Gorev_Detay>();
        gorev_listesi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Gorev_Detay model=gelen_gorevler.get(position);
            if(model.isGorev_durum())
            {
                model.setGorev_durum(false);
                if(tamamlananlar.contains(model))
                {
                    tamamlananlar.remove(model);
                }
            }
            else {
                SimpleDateFormat formatter1=new SimpleDateFormat("dd.MM.yyyy");
                Date date = new Date();
                Date date1=null;
                int gun_farki = 0;
                try {
                    date1=formatter1.parse(model.getSontarih());
                    long fark=date.getTime()-date1.getTime();
                    gun_farki=Math.abs((int)(fark / (1000*60*60*24)));

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if(!date1.before(date) || gun_farki==0) {
                    model.setGorev_durum(true);
                    tamamlananlar.add(model);
                }
            }
            gelen_gorevler.set(position,model);
            adapter.update(gelen_gorevler);





          }
      });
     gorev_tamamla.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             today.setToNow();
             onaylanma_tarihi=bicim2.format(tarih)+" "+today.format("%k:%M:%S");
             AlertDialog.Builder builder = new AlertDialog.Builder(Gorev.this);
             final AlertDialog.Builder builder2=new AlertDialog.Builder(Gorev.this);
             if(tamamlananlar.size()!=0) {

                 builder.setTitle("GÖREV TAMAMLAMA");
                 builder.setMessage("Seçilen görevleri tamamlamak istiyormusunuz ?");
                 builder.setNegativeButton("HAYIR", new DialogInterface.OnClickListener() {
                     public void onClick(DialogInterface dialog, int id) {
                     }
                 });


                 builder.setPositiveButton("EVET", new DialogInterface.OnClickListener() {
                     public void onClick(DialogInterface dialog, int id) {
                         Gorev_Detay g_d;
                         String sil_url=null;


                         for(int i=0; i<tamamlananlar.size(); i++)
                         {
                             sil_url="http://"+ip+":50491/api/Kullanici_Gorev/Delete/";
                             String ot_guncelle_url="http://"+ip+":50491/api/Kullanici_Gorev/OT_Put";
                             g_d=tamamlananlar.get(i);

                             JSONObject jsonObject1=new JSONObject();
                             try {
                                 jsonObject1.put("KULLANICI_ID",al_id);
                                 jsonObject1.put("GOREV_ID",g_d.getGorev_id());
                                 jsonObject1.put("ONAYLANMA_TARIH",onaylanma_tarihi);
                             } catch (JSONException e) {
                                 e.printStackTrace();
                             }

                             sil_url+=g_d.getKul_gorevID();
                             VeriGuncelle veriGuncelle=new VeriGuncelle();
                             veriGuncelle.webservis(ot_guncelle_url,jsonObject1);
                             VeriSil veriSil=new VeriSil();
                             veriSil.webservis(sil_url);

                         }

                         builder2.setTitle("YENİLEME");
                         builder2.setMessage("Görev listesini yenilemek istermisiz ?");
                         builder2.setPositiveButton("YENİLE", new DialogInterface.OnClickListener() {
                             @Override
                             public void onClick(DialogInterface dialog, int which) {
                                 gelen_gorevler.clear();
                                 tamamlananlar.clear();
                                 new arkaPlan().execute(url);

                             }
                         });
                         builder2.show();
                     }
                 });


                 builder.show();
             }
             else
             {
                 builder.setTitle("BOŞ GÖREV LİSTESİ !");
                 builder.setMessage("Tamamlanacak görev listesi boş, Lütfen tamamlamak istediklerinizi seçiniz");
                 builder.setNegativeButton("KAPAT", new DialogInterface.OnClickListener() {
                     public void onClick(DialogInterface dialog, int id) {


                     }
                 });
                 builder.show();


             }

         }
     });



    }
   /* JSONArray jsonArray_kulgorev=null;

    class arkaPlan2 extends  AsyncTask<String,String,String>
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
                 jsonArray_kulgorev=new JSONArray(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }*/
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
                    //  gelen_gorevler.add(new Gorev_Detay(gorevlendiren_ad, cinsiyet, gorev_aciklama, son_tarih, gorev_id,kulGorev_id));

                    gelen_gorevler.add(new Gorev_Detay(jsonObject.getString("Ad_soyad"),jsonObject.getBoolean("Cinsiyet"),jsonObject.getString("Gorev_aciklama"),jsonObject.getString("Son_tarih"),jsonObject.getInt("Gorev_id"),jsonObject.getInt("Kul_gorevId")));

                }
                adapter = new CustomAdapter(Gorev.this, gelen_gorevler);

                gorev_listesi.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            /*String gorevlendiren_ad = null, gorevlendiren_id = null;
            boolean cinsiyet = false;
            String gorev_aciklama, son_tarih;
            int gorev_id, kulGorev_id=0;

            JSONObject jsonObject2 = new JSONObject();
            JSONArray jsonArray2 = null;

            JSONObject jsonObject = new JSONObject();
            JSONObject jsonObject_kulgorev = new JSONObject();
            JSONArray jsonArray=null;
                try {
                    jsonArray=new JSONArray(s);
                    jsonArray2 = new JSONArray(al_kullanicilar);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);

                        gorevlendiren_id = jsonObject.getString("GOREVLENDİREN_ID");
                        for (int j = 0; j < jsonArray2.length(); j++) {
                            jsonObject2 = jsonArray2.getJSONObject(j);
                            if ((jsonObject2.getString("KUL_ID")).equals(gorevlendiren_id)) {
                                gorevlendiren_ad = jsonObject2.getString("KUL_ADSOYAD");
                                cinsiyet = jsonObject2.getBoolean("CINSIYET");
                                break;
                            }

                        }
                        gorev_id = jsonObject.getInt("GOREV_ID");
                        String k_id;int g_id;
                        for(int z=0; z<jsonArray_kulgorev.length(); z++)
                        {
                            jsonObject_kulgorev=jsonArray_kulgorev.getJSONObject(z);
                            k_id=jsonObject_kulgorev.getString("KULLANICI_ID");
                            g_id=jsonObject_kulgorev.getInt("GOREV_ID");
                            if(al_id.equals(k_id)&&gorev_id==g_id)
                            {
                                kulGorev_id=jsonObject_kulgorev.getInt("ID");
                                break;
                            }
                        }
                        gorev_aciklama = jsonObject.getString("GOREV_ACIKLAMA");
                        son_tarih = jsonObject.getString("SON_TARIH");
                      //  gelen_gorevler.add(new Gorev_Detay(gorevlendiren_ad, cinsiyet, gorev_aciklama, son_tarih, gorev_id,kulGorev_id));


                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                        Date date = new Date();
                        String son = jsonObject.getString("SON_TARIH");
                        Date sontarih = dateFormat.parse(son);
                        long fark = date.getTime() - sontarih.getTime();
                        int gun_farki = (int) (fark / (1000 * 60 * 60 * 24));
                    }

                    adapter = new CustomAdapter(Gorev.this, gelen_gorevler);

                    gorev_listesi.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }*/

            }
        }

    }



