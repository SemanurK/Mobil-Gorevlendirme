package com.example.asus.denem;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
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
import java.util.ArrayList;
import java.util.List;

public class Menu2 extends AppCompatActivity {
    String ip = "172.31.129.130";
    List<ListMenuModel> gelen_menu;
    Bundle gelen;
    int rol_id, al_bolum_id;
    String al_id, al_rolid,al_kullanicilar,al_adsoyad;
    int gelen_rolid;
    GridView gridLayout;
    Bundle bundle;
    String kul_bilgis;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu2);
        gelen = getIntent().getExtras();

        al_id = gelen.getString("id");
        al_bolum_id = gelen.getInt("bolumid");
        al_rolid = gelen.getString("rol_ad");
        gelen_rolid = gelen.getInt("rol_id");
        kul_bilgis=gelen.getString("json");
        al_kullanicilar=gelen.getString("kullanicilar");
        al_adsoyad=gelen.getString("adsoyad");
        gridLayout = (GridView) findViewById(R.id.menu_grid);


        String url = "http://" + ip + ":50491/api/MenuRol/Get/";
        url += al_id.toString();
        new arkaPlan().execute(url);
        new arkaPlan2().execute("http://" + ip + ":50491/api/Personel/bildirim/" + gelen_rolid);

        gridLayout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String menuAdi = gelen_menu.get(position).getMenuAdi();
                Intent ıntent;
                switch (menuAdi)
                {
                    case "Yeni Eklenenler":
                        ıntent=new Intent(Menu2.this, Kullanici_Listele2.class);
                        bundle=new Bundle();
                        bundle.putString("yeni","yeni");
                        ıntent.putExtras(bundle);
                        startActivity(ıntent);
                        break;
                    case "İşlemler":
                        ıntent=new Intent(Menu2.this, Islemler.class);
                        startActivity(ıntent);
                        break;

                    case "İzin Düzenle":
                        ıntent=new Intent(Menu2.this, Rol_Duzenle.class);
                        startActivity(ıntent);

                        break;
                    case "Görev Ekle":
                        ıntent = new Intent(Menu2.this,Gorev_Ekle.class);
                        bundle =new Bundle();
                        bundle.putString("id",al_id);
                        bundle.putString("kullanicilar",al_kullanicilar);
                        bundle.putString("rol_ad",al_rolid);
                        bundle.putInt("bolumid",al_bolum_id);
                        ıntent.putExtras(bundle);
                        startActivity(ıntent);
                        break;
                    case "Görev Ayarları":
                        ıntent = new Intent(Menu2.this,Gorev_Ayarlari.class);
                        bundle =new Bundle();
                        bundle.putString("id",al_id);
                        ıntent.putExtras(bundle);
                        startActivity(ıntent);
                        break;
                    case "Görev Arşivi":
                        ıntent=new Intent(Menu2.this, Gorev_Arsivi.class);
                        bundle=new Bundle();
                        bundle.putString("id",al_id);
                        bundle.putString("kullanicilar",al_kullanicilar);
                        ıntent.putExtras(bundle);
                        startActivity(ıntent);
                        break;
                    case "Görevlerim":
                        ıntent=new Intent(Menu2.this,Gorev.class);
                        Bundle gorev_giden=new Bundle();
                        gorev_giden.putString("adsoyad", al_adsoyad);
                        gorev_giden.putString("id",al_id);
                        gorev_giden.putString("kullanicilar",al_kullanicilar);
                        ıntent.putExtras(gorev_giden);
                        startActivity(ıntent);
                        break;
                    case "Kullanıcı Listele":
                        Bundle kul=new Bundle();
                        if(gelen_rolid==1)
                        {
                            ıntent=new Intent(Menu2.this,Kullanici_Listele.class);
                            Bundle rol_gonder=new Bundle();
                            rol_gonder.putInt("rol_id",gelen_rolid);
                            ıntent.putExtras(rol_gonder);
                            startActivity(ıntent);
                        }
                        else
                        {
                            ıntent=new Intent(Menu2.this,Kullanici_Listele2.class);
                            kul.putInt("rol_id",gelen_rolid);
                            kul.putString("yeni","bos");
                            kul.putInt("bolumid",al_bolum_id);
                            ıntent.putExtras(kul);
                            startActivity(ıntent);
                        }
                        // kul.putString("kullanicilar",al_kullanicilar);
                        // ıntent.putExtras(kul);
                        break;
                    case "Profilim":
                        ıntent=new Intent(Menu2.this,Profil.class);
                        Bundle kul_bilgi=new Bundle();
                        kul_bilgi.putString("kul_bilgi",kul_bilgis);
                        ıntent.putExtras(kul_bilgi);
                        startActivity(ıntent);
                        break;
                    case "Kullanıcı Ekle":
                        ıntent=new Intent(Menu2.this,Kullanici_Ekle2.class);
                        startActivity(ıntent);
                        break;
                    case "Çıkış Yap":
                        ıntent=new Intent(Menu2.this,Giris.class);
                        startActivity(ıntent);
                }

            }
        });


    }


    private void setSingleEvent(GridLayout gridLayout) {
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            CardView cardView = (CardView) gridLayout.getChildAt(i);
            final int finalI = i;
            if (gelen_menu.size() <= gridLayout.getChildCount()) {
                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent ıntent = new Intent(Menu2.this, Kullanici_Ekle2.class);

                        startActivity(ıntent);
                    }
                });
            } else {
                cardView.setVisibility(View.INVISIBLE);
            }

        }
    }

    class arkaPlan extends AsyncTask<String, String, String> {

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
            Log.d("gelen", s);
            JSONObject jsonObject = new JSONObject();
            String menuadi;
            String icon;
            JSONArray jsonArray = null;
            try {
                gelen_menu = new ArrayList<>();

                jsonArray = new JSONArray(s);
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    menuadi = jsonObject.getString("MENU_ADI");
                    icon = jsonObject.getString("BOS2");
                    //  gelen_menu.add(jsonObject.getString("MENU_ADI"));
                    gelen_menu.add(new ListMenuModel(menuadi, icon));
                }

                for(int i=0; i<gridLayout.getChildCount(); i++)
                {
                    CardView cardView = (CardView) gridLayout.getChildAt(i);
                    if(gelen_menu.size()<=i)
                    {
                        cardView.setVisibility(View.INVISIBLE);
                    }

                }
                YeniMenu adapter=new YeniMenu(Menu2.this,gelen_menu);

                gridLayout.setAdapter(adapter);



            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

        class arkaPlan2 extends AsyncTask<String, String, String> {

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
           try {
                JSONObject jsonObject=new JSONObject(s);
                if(jsonObject.getBoolean("Yeni_kayit_durum"))
                {
                    alterdialog("YENİ KAYIT","Sisteme yeni kayıt olmuş kullanıcılar bulunmakta !");
                }
                if(jsonObject.getBoolean("Gorem_durum"))
                {
                    alterdialog("GÖREV","Sistemde görevleriniz mevcut");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            }

        }
    public void alterdialog(String title,String mesaj)
    {
        android.app.AlertDialog.Builder builder1=new android.app.AlertDialog.Builder(Menu2.this);
        builder1.setTitle(title);
        builder1.setMessage(mesaj);
        builder1.setNeutralButton("KAPAT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder1.show();
    }


   /* class arkaPlan extends AsyncTask<String, String, String> {
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
            s.toString();
            Log.d("executadan gelen", s);
        }

       /* @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("executadan gelen", s);
            //yetki_menu=(ListView)findViewById(R.id.listView1);

            JSONObject jsonObject=new JSONObject();
            String menuadi;
            String icon;
            JSONArray jsonArray=null;
            try {
               gelen_menu=new ArrayList<>();

                jsonArray=new JSONArray(s);
                for (int i=0; i<jsonArray.length(); i++)
                {
                    jsonObject=jsonArray.getJSONObject(i);
                    menuadi=jsonObject.getString("MENU_ADI");
                    icon=jsonObject.getString("BOS2");
                    //  gelen_menu.add(jsonObject.getString("MENU_ADI"));
                    gelen_menu.add(new ListMenuModel(menuadi,icon));
                }

                //  ArrayAdapter<String> m= new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,gelen_menu);

                //yetki_menu.setAdapter(m);
              /*  ListAdapter adapter=new ListAdapter(Menu2.this,gelen_menu);



                yetki_menu.setAdapter(adapter);



            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }*/

}
