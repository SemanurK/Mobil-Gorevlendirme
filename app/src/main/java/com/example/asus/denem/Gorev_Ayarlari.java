package com.example.asus.denem;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Gorev_Ayarlari extends AppCompatActivity {
    List<Gorev_Detay2> gorev_detay2=new ArrayList<Gorev_Detay2>();
    ListView ayar_list;

    //Gorev Alanları listelemek için
    ListView gorevAlan_list;
    List<GorevAlanKisi> ga_list=new ArrayList<GorevAlanKisi>();
    GorevAlanKisi_Adapter gorevAlanKisi_adapter;
    final List<GorevAlanKisi> ga_silinecekler=new ArrayList<GorevAlanKisi>();
    boolean flag;

    //tanımlama bitti

    String url,id;
    Bundle gelen;
    Context context=this;
    Gorev_Ayarlari_Adapter adapter;
    Button gorev_sil;

    String ip="172.31.129.130";
   // String url3="http//172.29.24.101:50491/api/Gorev/Get/";
    String url3="http//"+ip+":50491/api/Gorev/Get/";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gorev__ayarlari);
        gorev_sil=(Button)findViewById(R.id.gorev_sil);
        url="http://"+ip+":50491/api/Gorev/Get_Ayarlar/";
       // url="http://172.29.24.101:50491/api/Gorev/Get_Ayarlar/";
        gelen=getIntent().getExtras();
        id =gelen.getString("id");
        url+=id;
        new arkaplan().execute(url);
        ayar_list=(ListView)findViewById(R.id.gorev_ayarlari);
        final List<Gorev_Detay2> silinecekler=new ArrayList<Gorev_Detay2>();
        final AlertDialog.Builder builder=new AlertDialog.Builder(Gorev_Ayarlari.this);
       ayar_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                ///GÖREV İD ALINIYOR BURDA
                final Gorev_Detay2 model=gorev_detay2.get(position);
                builder.setTitle("YAPILACAK İŞLEM");
                builder.setMessage("Görevi düzenlemek mi silmek mi istiyorsunuz ?");
                //GÖREVİ ALAN KULLANICILARIN LİSTELENMESİ VE GÖREVİN DÜZENLENMESİ
                builder.setPositiveButton("Detay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ga_list.clear();
                       // String url_gakisiler="http://172.29.24.101:50491/api/Kullanici_Gorev/GorevAlanKisiler/";
                        String url_gakisiler="http://"+ip+":50491/api/Kullanici_Gorev/GorevAlanKisiler/";
                        url_gakisiler+=model.getGorev_id();
                        new arkaplan2().execute(url_gakisiler);

                    }
                });

                builder.setNegativeButton("işaretle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!silinecekler.contains(model)) {
                            model.setGorev_durum(true);
                            silinecekler.add(model);
                            gorev_detay2.set(position, model);
                            adapter.update(gorev_detay2);
                        }
                    }
                });
                builder.setNeutralButton("Vazgeç", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                if(model.isGorev_durum())
                {
                    model.setGorev_durum(false);
                    if(silinecekler.contains(model))
                    {
                        silinecekler.remove(model);
                    }
                }

                gorev_detay2.set(position,model);
                adapter.update(gorev_detay2);
                    }
                });
                builder.show();
            }
        });

       gorev_sil.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               AlertDialog.Builder b2=new AlertDialog.Builder(Gorev_Ayarlari.this);
               if(silinecekler.size()!=0)
               {
                   Gorev_Detay2 detay2;
                   String sil_url;
                   for(int i=0; i<silinecekler.size(); i++)
                   {
                       sil_url="http://"+ip+":50491/api/Gorev/Delete/";
                     //  sil_url="http://172.29.24.101:50491/api/Gorev/Delete/";
                       detay2=silinecekler.get(i);
                       sil_url+=detay2.getGorev_id();
                       VeriSil veriSil=new VeriSil();
                       veriSil.webservis(sil_url);
                   }
                   b2.setTitle("YENİLEME");
                   b2.setMessage("Değişiklikleri görebilmek için görev listesini yenileyiniz.");
                   b2.setPositiveButton("YENİLE", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           silinecekler.clear();
                           gorev_detay2.clear();
                           new arkaplan().execute(url);

                       }
                   });
                   b2.show();
                   //alterdialog("YENİLEME","Değişiklikleri görebilmek için görev listesini yenileyiniz.");

               }
               else
                   alterdialog(" ","Lütfen silinecek görev seçiniz !");

           }
       });

    }
    public void alterdialog(String title,String mesaj)
    {
        android.app.AlertDialog.Builder builder1=new android.app.AlertDialog.Builder(Gorev_Ayarlari.this);
        builder1.setTitle(title);
        builder1.setMessage(mesaj);
        builder1.setNeutralButton("KAPAT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder1.show();
    }
    class arkaplan extends AsyncTask<String, String, String>
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
            String aciklama,son_tarih;
            int gorev_id,gorevlendiren_id;
            try {
                JSONArray jsonArray=new JSONArray(s);
                JSONObject jsonObject=new JSONObject();
                for(int i=0; i<jsonArray.length(); i++)
                {
                    jsonObject=jsonArray.getJSONObject(i);
                    aciklama=jsonObject.getString("GOREV_ACIKLAMA");
                    son_tarih=jsonObject.getString("SON_TARIH");
                    gorev_id=jsonObject.getInt("GOREV_ID");
                    gorevlendiren_id=jsonObject.getInt("GOREVLENDİREN_ID");
                    gorev_detay2.add(new Gorev_Detay2(aciklama,son_tarih,gorev_id,gorevlendiren_id));

                }
                adapter=new Gorev_Ayarlari_Adapter(Gorev_Ayarlari.this, gorev_detay2);

                ayar_list.setAdapter(adapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class arkaplan2 extends AsyncTask<String,String,String>{

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
            String ad_soyad,gorulme_tarih,onaylanma_tarih;
            int gorev_id,kullanici_id;
            View view1=getLayoutInflater().inflate(R.layout.ozel_dialog2,null);
            AlertDialog.Builder gorevAlan= new AlertDialog.Builder(Gorev_Ayarlari.this);

            gorevAlan_list=(ListView)view1.findViewById(R.id.gorev_alan);

            boolean cinsiyet;
            try {
                JSONArray jsonArray=new JSONArray(s);
                JSONObject jsonObject=new JSONObject();
                for(int i=0; i<jsonArray.length(); i++)
                {
                    jsonObject=jsonArray.getJSONObject((i));
                    ad_soyad=jsonObject.getString("Ad_soyad");
                    gorulme_tarih=jsonObject.getString("Gorulme");
                    onaylanma_tarih=jsonObject.getString("Onaylanma");
                    cinsiyet=jsonObject.getBoolean("Cinsiyet");
                    gorev_id=jsonObject.getInt("Gorev_id");
                    kullanici_id=jsonObject.getInt("Kisi_id");
                    ga_list.add(new GorevAlanKisi(ad_soyad,gorulme_tarih,onaylanma_tarih,gorev_id,kullanici_id,cinsiyet,""));

                }
                if(ga_list.size()==0)
                {
                    alterdialog("BOŞ LİSTE","Sistemde bu görevi alan hiç bir kullanıcı bulunamamıştır !");
                }
                else {
                    gorevAlanKisi_adapter = new GorevAlanKisi_Adapter(Gorev_Ayarlari.this, ga_list);
                    gorevAlan_list.setAdapter(gorevAlanKisi_adapter);
                    gorevAlan.setView(view1);
                    AlertDialog dialog1 = gorevAlan.create();
                    dialog1.show();
                    final AlertDialog.Builder ga_builder=new AlertDialog.Builder(Gorev_Ayarlari.this);
                    gorevAlan_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            GorevAlanKisi gorevAlanKisi_model=ga_list.get(position);
                            if(gorevAlanKisi_model.isDurum())
                            {
                                gorevAlanKisi_model.setDurum(false);
                                if(ga_silinecekler.contains(gorevAlanKisi_model))
                                {
                                    ga_silinecekler.remove(gorevAlanKisi_model);
                                }
                            }
                            else
                            {
                                gorevAlanKisi_model.setDurum(true);
                                ga_silinecekler.add(gorevAlanKisi_model);
                            }
                            ga_list.set(position,gorevAlanKisi_model);
                            gorevAlanKisi_adapter.update(ga_list);

                        }
                    });
                }




            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
