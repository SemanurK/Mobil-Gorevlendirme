package com.example.asus.denem;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import static android.R.layout.simple_list_item_1;
import static com.example.asus.denem.R.layout.activity_menu_goruntule;

public class MenuGoruntule extends AppCompatActivity {

    String al_id,al_adsoyad,al_kullanicilar,al_rolid;
    TextView tx;
    Bundle gelen;
    ListView yetki_menu;
  //  List<String> gelen_menu;
    List<ListMenuModel> gelen_menu;
    Bundle bundle;
    int rol_id,al_bolum_id;
    int gelen_rolid;
    String ip="172.31.129.130";
    String kul_bilgis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_menu_goruntule);

        gelen=getIntent().getExtras();
        yetki_menu=(ListView)findViewById(R.id.listView1);
        tx=(TextView)findViewById(R.id.k_ad_soyad);
        al_id=gelen.getString("id");
        al_bolum_id=gelen.getInt("bolumid");
        al_rolid=gelen.getString("rol_ad");
        gelen_rolid=gelen.getInt("rol_id");
        kul_bilgis=gelen.getString("json");
        al_adsoyad=gelen.getString("adsoyad");
        al_kullanicilar=gelen.getString("kullanicilar");
        tx.setText(al_adsoyad);
       String url="http://"+ip+":50491/api/MenuRol/Get/";

      //  String url="http://"+ip+":50491/api/YeniKayit";
      //  String url2="http://"+ip+":50491/api/KullaniciRol/Get/"+al_id;
        //String url="http://172.29.24.101:50491/api/MenuRol/Get/";
        url+=al_id.toString();
        new arkaPlan().execute(url);
        new arkaPlan2().execute("http://"+ip+":50491/api/Personel/bildirim/"+gelen_rolid);





        yetki_menu.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                String ad=gelen_menu.get(position).getMenuAdi();
                Intent ıntent;
                switch (ad)
                {
                    case "Yeni Eklenenler":
                        ıntent=new Intent(MenuGoruntule.this, Kullanici_Listele2.class);
                        bundle=new Bundle();
                        bundle.putString("yeni","yeni");
                        ıntent.putExtras(bundle);
                        startActivity(ıntent);
                        break;
                    case "İşlemler":
                        ıntent=new Intent(MenuGoruntule.this, Islemler.class);
                        startActivity(ıntent);
                        break;

                    case "İzin Düzenle":
                        ıntent=new Intent(MenuGoruntule.this, Rol_Duzenle.class);
                        startActivity(ıntent);

                        break;
                    case "Görev Ekle":
                         ıntent = new Intent(MenuGoruntule.this,Gorev_Ekle.class);
                         bundle =new Bundle();
                         bundle.putString("id",al_id);
                         bundle.putString("kullanicilar",al_kullanicilar);
                         bundle.putString("rol_ad",al_rolid);
                         bundle.putInt("bolumid",al_bolum_id);
                         ıntent.putExtras(bundle);
                        startActivity(ıntent);
                        break;
                    case "Görev Ayarları":
                        ıntent = new Intent(MenuGoruntule.this,Gorev_Ayarlari.class);
                        bundle =new Bundle();
                        bundle.putString("id",al_id);
                        ıntent.putExtras(bundle);
                        startActivity(ıntent);
                    break;
                    case "Görev Arşivi":
                        ıntent=new Intent(MenuGoruntule.this, Gorev_Arsivi.class);
                        bundle=new Bundle();
                        bundle.putString("id",al_id);
                        bundle.putString("kullanicilar",al_kullanicilar);
                        ıntent.putExtras(bundle);
                        startActivity(ıntent);
                        break;
                    case "Görevlerim":
                        ıntent=new Intent(MenuGoruntule.this,Gorev.class);
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
                           ıntent=new Intent(MenuGoruntule.this,Kullanici_Listele.class);
                           Bundle rol_gonder=new Bundle();
                           rol_gonder.putInt("rol_id",gelen_rolid);
                           ıntent.putExtras(rol_gonder);
                           startActivity(ıntent);
                       }
                       else
                       {
                           ıntent=new Intent(MenuGoruntule.this,Kullanici_Listele2.class);
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
                        ıntent=new Intent(MenuGoruntule.this,Profil.class);
                        Bundle kul_bilgi=new Bundle();
                        kul_bilgi.putString("kul_bilgi",kul_bilgis);
                        ıntent.putExtras(kul_bilgi);
                        startActivity(ıntent);
                        break;
                    case "Kullanıcı Ekle":
                        ıntent=new Intent(MenuGoruntule.this,Kullanici_Ekle2.class);
                        startActivity(ıntent);
                        break;
                    case "Çıkış Yap":
                        ıntent=new Intent(MenuGoruntule.this,Giris.class);
                        startActivity(ıntent);
                }


            }
        });

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
                ListAdapter adapter=new ListAdapter(MenuGoruntule.this,gelen_menu);



                yetki_menu.setAdapter(adapter);



            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
    class arkaPlan2 extends AsyncTask<String, String,String>{

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
        AlertDialog.Builder builder1=new AlertDialog.Builder(MenuGoruntule.this);
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
