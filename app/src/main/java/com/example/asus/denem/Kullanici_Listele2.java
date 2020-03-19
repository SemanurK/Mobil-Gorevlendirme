package com.example.asus.denem;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

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

import javax.xml.transform.Result;

public class Kullanici_Listele2 extends AppCompatActivity {
    ListView bolum_kullanici;
    List<Kullanicilar> kullanicilars=new ArrayList<>();

    Button k_ekle,k_sil,yk_onayla;
    TextView bolum_adi,yk_d_kulad,yk_d_adsoyad,yk_d_rol,yk_d_bolum,yk_d_tc;
    Bundle gelen;
    CustomAdapter2 adapter;
    RadioGroup RG;
    String ip="172.31.129.130";
    List<String> sonuclar=new ArrayList<>();
    String sonuc="";
    int kullanici_rolid;
    Spinner spinner_bolum;
    Spinner spinner_rol;
    List<BolumModel> bolumler=new ArrayList<>();
    List<RolModel> roller=new ArrayList<>();
    boolean flag_bolum=false, flag_rol=false;
    String url;
    boolean yeni_kontorl=false;
    View view1,view2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kullanici__listele2);
        gelen=getIntent().getExtras();
        bolum_adi=(TextView)findViewById(R.id.k_bolumadi);
        bolum_adi.setText(gelen.getString("bolumadi"));
        final int bolum_id=gelen.getInt("bolumid");
        bolum_kullanici=(ListView)findViewById(R.id.bolum_kullanicilar);
        k_sil=(Button)findViewById(R.id.k_sil);
        yk_onayla=(Button)findViewById(R.id.yk_onayla);
        yk_onayla.setVisibility(View.INVISIBLE);
       // String url="http://" + ip + ":50491/api/Bolum/K_Bolumler/"+bolum_id;
        String gelenn=gelen.getString("yeni");
        kullanici_rolid=gelen.getInt("rol_id");

        if(gelen.getString("yeni").equals("yeni"))
        {
            yeni_kontorl=true;
            url="http://" + ip + ":50491/api/YeniKayit";
            yk_onayla.setVisibility(View.VISIBLE);
            new arkaPlan().execute(url);

        }
        else {
            yeni_kontorl=false;
            url = "http://" + ip + ":50491/api/Personel/KullaniciTumBilgi/" + bolum_id;
            new arkaPlan().execute(url);
        }


        final List<Kullanicilar> silinecekler = new ArrayList<Kullanicilar>();
        final List<Kullanicilar> onaylanacaklar = new ArrayList<Kullanicilar>();
        bolum_kullanici.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final AlertDialog.Builder builder=new AlertDialog.Builder(Kullanici_Listele2.this);

                final Kullanicilar model=kullanicilars.get(position);


                builder.setTitle("YAPILACAK İŞLEM");
                builder.setMessage("Kullanıcıyı düzenlemek mi işaretlemek mi istiyorsunuz ?");
                builder.setPositiveButton("Düzenle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new arkaPlan2().execute("http://" + ip + ":50491/api/Bolum/Get");
                        new arkaPlan3().execute("http://" + ip + ":50491/api/Bolum/Roller");
                        final AlertDialog.Builder builder2=new AlertDialog.Builder(Kullanici_Listele2.this);
                        AlertDialog.Builder builder1=new AlertDialog.Builder(Kullanici_Listele2.this);
                        builder1.setTitle("KULLANICI DÜZENLEME İŞLEMİ");

                        view1=getLayoutInflater().inflate(R.layout.activity_islemler,null);
                        spinner_bolum=(Spinner)view1.findViewById(R.id.Kspinner_bolum);
                        spinner_rol=(Spinner)view1.findViewById(R.id.Kspinner_rol);
                        RG=view1.findViewById(R.id.radiogroup);
                        RadioButton rb=view1.findViewById(R.id.i_radioButton);
                        RadioButton rb2=view1.findViewById(R.id.i_radioButton2);
                    if(model.isDURUM1()){
                        RG.check(R.id.i_radioButton);
                    }
                    else {
                         RG.check(R.id.i_radioButton2); }


                      //  new arkaPlan2().execute("http://" + ip + ":50491/api/Bolum/Get");
                       // new arkaPlan3().execute("http://" + ip + ":50491/api/Bolum/Roller");
                        EditText id=(EditText)view1.findViewById(R.id.kul_id);

                        id.setText(String.valueOf( model.getKUL_ID()),TextView.BufferType.EDITABLE);
                        id.setEnabled(false);
                        final int[] bolum_id = new int[1];
                        final int[] rol_id = new int[1];
                        final boolean[] durum = new boolean[1];


                        RG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup group, int checkedId) {
                                final RadioButton rb=(RadioButton)view1.findViewById(checkedId);

                                switch (group.getCheckedRadioButtonId()) {
                                    case R.id.i_radioButton:
                                        durum[0] =true;
                                        break;
                                    case R.id.i_radioButton2:
                                        durum[0]=false;
                                        break;
                                }

                            }
                        });



                        spinner_rol.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                rol_id[0] =roller.get(position).getRolid();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        spinner_bolum.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                bolum_id[0] =bolumler.get(position).getBolum_id();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        builder1.setNegativeButton("Kapat", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                            }
                        });
                        builder1.setPositiveButton("Güncelle", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                JSONObject jsonObject=new JSONObject();
                                JSONObject jsonObject2=new JSONObject();
                                try {
                                    jsonObject.put("BOLUM_ID",bolum_id[0]);
                                    jsonObject.put("DURUM",durum[0]);
                                    jsonObject2.put("KULLANICI_ID",model.getKUL_ID());
                                    jsonObject2.put("ROL_ID",rol_id[0]);
                                    VeriGuncelle veriGuncelle=new VeriGuncelle();
                                    VeriGonder veriGonder=new VeriGonder();
                                    String url2="http://"+ip+":50491/api/Personel/Put/"+model.getKUL_ID();
                                    veriGuncelle.webservis(url2,jsonObject);
                                    veriGonder.webservis("http://"+ip+":50491/api/KullaniciRol",jsonObject2);
                                    builder2.setTitle("YENİLEME");
                                    builder2.setMessage("Sistemdeki yenilikleri görebilmek için sayfayı  yenilemek istermisiz ?");
                                    builder2.setPositiveButton("YENİLE", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            kullanicilars.clear();
                                            new arkaPlan().execute(url);

                                        }
                                    });
                                    builder2.show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }
                        });

                        builder1.setView(view1);
                        AlertDialog dialog1=builder1.create();
                        dialog1.show();

                    }
                });
                if(yeni_kontorl==true)
                {
                    builder.setPositiveButton("Detay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AlertDialog.Builder builder3=new AlertDialog.Builder(Kullanici_Listele2.this);
                            view2=getLayoutInflater().inflate(R.layout.yeni_kullanici_detay,null);
                            yk_d_adsoyad=(TextView)view2.findViewById(R.id.yk_d_adsoyad);
                            yk_d_kulad=(TextView)view2.findViewById(R.id.yk_d_kulad);
                            yk_d_bolum=(TextView)view2.findViewById(R.id.yk_d_bolum);
                            yk_d_rol=(TextView)view2.findViewById(R.id.yk_d_rol);
                            yk_d_tc=(TextView)view2.findViewById(R.id.yk_d_tc);

                            yk_d_adsoyad.setText(String.valueOf( model.getKUL_ADSOYAD()),TextView.BufferType.EDITABLE);
                            yk_d_kulad.setText(String.valueOf( model.getKUL_AD()),TextView.BufferType.EDITABLE);
                            yk_d_bolum.setText(String.valueOf(model.getBOLUM_ADI()),TextView.BufferType.EDITABLE);
                            yk_d_rol.setText(String.valueOf(model.getRol_Ad()),TextView.BufferType.EDITABLE);
                            yk_d_tc.setText(String.valueOf(model.getTC()),TextView.BufferType.EDITABLE);
                            builder3.setNegativeButton("Kapat", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                            builder3.setView(view2);
                            builder3.show();

                        }
                    });
                    builder.setNegativeButton("İşaretle", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(!silinecekler.contains(model)) {

                                model.setDurum(true);
                                silinecekler.add(model);
                                kullanicilars.set(position, model);
                                adapter.update(kullanicilars);
                            }

                        }
                    });
                    builder.setNeutralButton("Vazgec", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(model.isDurum())
                            {
                                model.setDurum(false);
                                if(silinecekler.contains(model))
                                {
                                    silinecekler.remove(model);
                                }
                            }
                            kullanicilars.set(position,model);
                            adapter.update(kullanicilars);
                        }
                    });
                }
                else
                {
                    builder.setNegativeButton("işaretle", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(!silinecekler.contains(model)) {

                                model.setDurum(true);
                                silinecekler.add(model);
                                kullanicilars.set(position, model);
                                adapter.update(kullanicilars);
                            }
                        }
                    });
                    builder.setNeutralButton("Vazgec", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(model.isDurum())
                            {
                                model.setDurum(false);
                                if(silinecekler.contains(model))
                                {
                                    silinecekler.remove(model);
                                }
                            }
                            kullanicilars.set(position,model);
                            adapter.update(kullanicilars);
                        }
                    });
                }


                builder.show();


            }
        });
        yk_onayla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //burada silinecekler listesi işaretli olan kullanıcıları simgeler
                AlertDialog.Builder b=new AlertDialog.Builder(Kullanici_Listele2.this);
                b.setTitle("ONAYLAMA İŞLEMİ");
                b.setMessage("Seçilen kullanıcıları onaylamak istiyor musunuz ?");
                b.setPositiveButton("Onayla", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(silinecekler.size()!=0)
                        {
                            VeriGonder veriGonder=new VeriGonder();
                            for(int i=0; i<silinecekler.size(); i++)
                            {
                                JSONObject jsonObject=new JSONObject();
                                Kullanicilar onaylanacak_k=silinecekler.get(i);
                                try {
                                    jsonObject.put("TC",onaylanacak_k.getTC());
                                    jsonObject.put("KUL_ADSOYAD",onaylanacak_k.getKUL_ADSOYAD());
                                    jsonObject.put("KUL_AD",onaylanacak_k.getKUL_AD());
                                    jsonObject.put("SİFRE",onaylanacak_k.getSIFRE());
                                    jsonObject.put("DURUM",1);
                                    jsonObject.put("CEP",onaylanacak_k.getCEP());
                                    jsonObject.put("ADRES",onaylanacak_k.getADRES());
                                    jsonObject.put("MAİL",onaylanacak_k.getMAIL());
                                    jsonObject.put("CINSIYET",onaylanacak_k.isCINSIYET());
                                    jsonObject.put("DOGUM_TARIHI",onaylanacak_k.getDOGUM_T());
                                    jsonObject.put("SORU",onaylanacak_k.getSORU());
                                    jsonObject.put("CEVAP",onaylanacak_k.getCEVAP());
                                    jsonObject.put("BOLUM_ID",onaylanacak_k.getBOLUM_ID());
                                    veriGonder.webservis("http://" + ip + ":50491/api/Personel", jsonObject);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            alterdialog2("ONAY","Onaylanan kullanıcılar mevcut kullanıcı listesine eklenmiştir.","http://" + ip + ":50491/api/YeniKayit");
                            if(kullanicilars.size()==0)
                            {
                                alterdialog("BOŞ LİSTE","Sistemde yeni kullanıcı bulunamıyor.");
                            }

                        }
                        else
                            alterdialog(" ","Lütfen onaylanacak kullanıcıları seçiniz !");

                    }
                });
                b.setNegativeButton("Vazgeç", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       dialog.dismiss();
                    }
                });
                b.show();



            }
        });
        k_sil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder b2=new AlertDialog.Builder(Kullanici_Listele2.this);
                if(silinecekler.size()!=0)
                {
                    Kullanicilar detay2;
                    String sil_url,yeni_sil_url;

                    for(int i=0; i<silinecekler.size(); i++)
                    {
                        sil_url="http://"+ip+":50491/api/Personel/Delete/";
                        yeni_sil_url="http://"+ip+":50491/api/YeniKayit/Delete/";
                        detay2=silinecekler.get(i);
                        VeriSil veriSil=new VeriSil();

                        if(yeni_kontorl==true)
                        {
                            yeni_sil_url+=detay2.getKUL_ID();
                            veriSil.webservis(yeni_sil_url);
                        }
                        else
                        {
                            sil_url+=detay2.getKUL_ID();
                            veriSil.webservis(sil_url);
                        }


                    }
                    b2.setTitle("YENİLEME");
                    b2.setMessage("Değişiklikleri görebilmek için kullanıcı listesini yenileyiniz.");
                    b2.setPositiveButton("YENİLE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            silinecekler.clear();
                            kullanicilars.clear();
                            if (yeni_kontorl==true)
                            {
                                new arkaPlan().execute("http://" + ip + ":50491/api/YeniKayit");
                            }
                            else
                            new arkaPlan().execute("http://" + ip + ":50491/api/Personel/KullaniciTumBilgi/"+bolum_id);

                        }
                    });
                    b2.show();

                }
                else
                    alterdialog(" ","Lütfen silinecek kullanıcı seçiniz !");
            }

        });
    }
    public void alterdialog2(String title, String mesaj, final String gelen_url)
    {
        android.app.AlertDialog.Builder builder1=new android.app.AlertDialog.Builder(Kullanici_Listele2.this);
        builder1.setTitle(title);
        builder1.setMessage(mesaj);
        builder1.setNeutralButton("YENİLE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                kullanicilars.clear();
             new arkaPlan().execute(gelen_url);
            }
        });
        builder1.show();
    }
    public void alterdialog(String title,String mesaj)
    {
        android.app.AlertDialog.Builder builder1=new android.app.AlertDialog.Builder(Kullanici_Listele2.this);
        builder1.setTitle(title);
        builder1.setMessage(mesaj);
        builder1.setNeutralButton("KAPAT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder1.show();
    }

    class  arkaPlan extends  AsyncTask<String,String, String>
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
                    sonuc=dosya;
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
            JSONArray jsonArray= null;
            try {
                jsonArray = new JSONArray(s);
                JSONObject jsonObject=new JSONObject();
                if(yeni_kontorl==true)
                {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        kullanicilars.add(new Kullanicilar(jsonObject.getString("KUL_ADSOYAD1"),jsonObject.getString("KUL_AD1"),jsonObject.getString("TC1"), jsonObject.getString("SIFRE1"),jsonObject.getBoolean("CINSIYET1"), jsonObject.getInt("KUL_ID1"), jsonObject.getString("ADRES1"), jsonObject.getString("CEP1"), jsonObject.getString("MAİL1"), jsonObject.getString("ROL_ADI1"),jsonObject.getString("BOLUM_ADI1"),jsonObject.getInt("BOLUM_ID1"),jsonObject.getString("SORU1"),jsonObject.getString("CEVAP1"),true));

                    }
                }
                else {
                    if(kullanici_rolid==1)
                    {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonObject = jsonArray.getJSONObject(i);
                            kullanicilars.add(new Kullanicilar(jsonObject.getString("KUL_ADSOYAD1"), jsonObject.getBoolean("CINSIYET1"), jsonObject.getInt("KUL_ID1"), jsonObject.getString("ADRES1"), jsonObject.getString("CEP1"), jsonObject.getString("MAİL1"), jsonObject.getString("ROL_ADI1"),jsonObject.getInt("BOLUM_ID1"),jsonObject.getBoolean("DURUM1")));

                        }
                    }
                    else
                    {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonObject = jsonArray.getJSONObject(i);
                            if(!jsonObject.getString("ROL_ADI1").equals("Dekan"))
                            {
                                kullanicilars.add(new Kullanicilar(jsonObject.getString("KUL_ADSOYAD1"), jsonObject.getBoolean("CINSIYET1"), jsonObject.getInt("KUL_ID1"), jsonObject.getString("ADRES1"), jsonObject.getString("CEP1"), jsonObject.getString("MAİL1"), jsonObject.getString("ROL_ADI1"),jsonObject.getInt("BOLUM_ID1"),jsonObject.getBoolean("DURUM1")));

                            }

                        }
                    }

                }
                adapter = new CustomAdapter2(Kullanici_Listele2.this, kullanicilars);
                bolum_kullanici.setAdapter(adapter);
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
            flag_bolum=true;
            super.onPostExecute(s);
            Log.d("executadan gelen", s);
            JSONObject jsonObject = new JSONObject();
            try {
                JSONArray jsonArray = new JSONArray(s);
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    bolumler.add(new BolumModel(jsonObject.getString("BOLUM_AD"), jsonObject.getInt("BOLUM_ID")));
                }
                // ArrayAdapter<BolumModel> adapter_bolumler=new ArrayAdapter<BolumModel>(Kullanici_Ekle2.this,android.R.layout.simple_spinner_item,bolumler);
                SpinnerAdapter adapter_bolumler=new Spinner_Adapter(Kullanici_Listele2.this,bolumler,roller,flag_bolum,flag_rol);
                spinner_bolum.setAdapter(adapter_bolumler);


            } catch (JSONException e) {
                e.printStackTrace();
            }
            flag_bolum=false
            ;


        }
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
            flag_rol=true;
            Log.d("executadan gelen", s);
            JSONObject jsonObject = new JSONObject();
            try {
                JSONArray jsonArray = new JSONArray(s);
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    roller.add(new RolModel(jsonObject.getInt("ROL_ID"),jsonObject.getString("ROL_ADI")));
                }
                // ArrayAdapter<RolModel> adapter_roller=new ArrayAdapter<RolModel>(Kullanici_Ekle2.this,android.R.layout.simple_spinner_item,roller);
                SpinnerAdapter adapter_roller=new Spinner_Adapter(Kullanici_Listele2.this,bolumler,roller,flag_bolum,flag_rol);
                spinner_rol.setAdapter(adapter_roller);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            flag_rol=false;
        }
    }




}
