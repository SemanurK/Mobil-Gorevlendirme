package com.example.asus.denem;

import android.accounts.Account;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

public class Kullanici_Listele extends AppCompatActivity {
   // ListView kullanici_listesi;
    ListView bolum_list;
    List<Kullanicilar> kullanicilars = new ArrayList<Kullanicilar>();
    List<BolumModel> bolumler=new ArrayList<>();
    Bundle gelen;
    Bundle bundle,bundle2;
    int bolum_idd;


    EditText edt_ara;


    String al_kullanicilar;
    Context context = this;
    String url;
    int rol_id;
    Bolum_Adapter adapter;
    Button k_sil,k_ekle,btn_ara;
    String []kul_adsoyad;
    String ip="172.31.129.130";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kullanici__listele);
      gelen=getIntent().getExtras();
       rol_id=gelen.getInt("rol_id");
       // kullanici_listesi = (ListView) findViewById(R.id.tum_kullanicilar);
        bolum_list = (ListView) findViewById(R.id.tum_kullanicilar);
        edt_ara=(EditText)findViewById(R.id.edt_kul_ara);
        btn_ara=(Button)findViewById(R.id.btn_kul_ara);
      //  k_sil = (Button) findViewById(R.id.k_sil);
       // k_ekle = (Button) findViewById(R.id.k_ekle);
        new arkaPlan().execute("http://" + ip + ":50491/api/Bolum/Get");
        final List<Kullanicilar> silinecekler = new ArrayList<Kullanicilar>();
        btn_ara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kul_adsoyad=edt_ara.getText().toString().split(" ");
                kul_adsoyad[0]=kul_adsoyad[0].substring(0,1).toUpperCase()+kul_adsoyad[0].substring(1).toLowerCase();
                kul_adsoyad[1]=kul_adsoyad[1].toUpperCase();
                new arkaPlan3().execute("http://"+ip+":50491/api/Personel");


            }
        });


        bolum_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int bolum_id=bolumler.get(position).getBolum_id();
                String bolum_ad=bolumler.get(position).getBolum_adi();
                Intent intent =new Intent(Kullanici_Listele.this,Kullanici_Listele2.class);
                bundle=new Bundle();
                bundle.putString("bolumadi",bolum_ad);
               bundle.putInt("rol_id",rol_id);
                bundle.putString("yeni","bos");
                bundle.putInt("bolumid",bolum_id);
                intent.putExtras(bundle);
                startActivity(intent);


                // Toast.makeText(getApplicationContext(),"bos",Toast.LENGTH_LONG).show();
            }
        });

     /*   kullanici_listesi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder=new AlertDialog.Builder(Kullanici_Listele.this);
                final Kullanicilar model=kullanicilars.get(position);
                builder.setTitle("YAPILACAK İŞLEM");
                builder.setMessage("Görevi düzenlemek mi silmek mi istiyorsunuz ?");
                builder.setPositiveButton("Düzenle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
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
                builder.show();


            }
        });
        k_ekle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(Kullanici_Listele.this);
                View view1=getLayoutInflater().inflate(R.layout.activity_islemler,null);
                //                        final EditText edt_tarih=(EditText)view1.findViewById(R.id.dialog_tarih);
                final String dosya=VeriGonder.dosya1;


                final EditText ad_soyad=(EditText)view1.findViewById(R.id.kk_ad_soyad);
                final EditText TC=(EditText)view1.findViewById(R.id.tc);
                final EditText sifre=(EditText)view1.findViewById(R.id.sifre);
                final EditText kul_ad=(EditText)view1.findViewById(R.id.kul_ad);
                final EditText d_t=(EditText)view1.findViewById(R.id.dogum_yil);
                final EditText d_y=(EditText)view1.findViewById(R.id.dogum_yer);
                final EditText mail=(EditText)view1.findViewById(R.id.mail);
                final EditText cep=(EditText)view1.findViewById(R.id.cep);
                final EditText adres=(EditText)view1.findViewById(R.id.Adres);



                builder.setPositiveButton("Ekle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

               JSONObject jsonObject=new JSONObject();
               Editable al_as=ad_soyad.getText();
               Editable tc=TC.getText();
               Editable al_sifre=sifre.getText();
               Editable al_kul_ad=kul_ad.getText();
               Editable al_d_t=d_t.getText();
               Editable al_d_y=d_y.getText();
               Editable al_mail=mail.getText();
               Editable al_cep=cep.getText();
               Editable al_adres=adres.getText();
             //  int al_rol= (int) rol.getSelectedItemPosition();
             //  al_rol+=1;

                        try {
                            jsonObject.put("TC",tc);
                            jsonObject.put("KUL_ADSOYAD",al_as);
                            jsonObject.put("SİFRE",al_sifre);
                            jsonObject.put("KUL_AD",al_kul_ad);
                            jsonObject.put("DOGUM_TARIHI",al_d_t);
                            jsonObject.put("DOGUM_YERI",al_d_y);
                            jsonObject.put("CEP",al_cep);
                            jsonObject.put("ADRES",al_adres);
                            jsonObject.put("MAİL",al_mail);
                            jsonObject.put("CINSIYET",0);
                            VeriGonder veriGonder=new VeriGonder();
                            veriGonder.webservis("http://"+ip+":50491/api/Personel",jsonObject);

                           /* int son_eklenen;

                                JSONObject js=new JSONObject(dosya);
                                JSONObject js2=new JSONObject();


                                son_eklenen=js.getInt("KUL_ID");
                                js2.put("KULLANICI_ID",son_eklenen);
                                js2.put("ROL_ID",al_rol);

                                veriGonder.webservis("http://172.31.158.195:50491/api/KullaniciRol",jsonObject);



                            silinecekler.clear();
                            kullanicilars.clear();
                            new arkaPlan().execute("http://"+ip+":50491/api/Personel");




                        } catch (JSONException e) {
                            e.printStackTrace();
                        }









                    }
                });
                builder.setView(view1);
                AlertDialog dialog1=builder.create();
                dialog1.show();


            }
        });
        k_sil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder b2=new AlertDialog.Builder(Kullanici_Listele.this);
                if(silinecekler.size()!=0)
                {
                    Kullanicilar detay2;
                    String sil_url;
                    for(int i=0; i<silinecekler.size(); i++)
                    {
                        sil_url="http://"+ip+":50491/api/Personel/Delete/";
                        detay2=silinecekler.get(i);
                        sil_url+=detay2.getKUL_ID();
                        VeriSil veriSil=new VeriSil();
                        veriSil.webservis(sil_url);
                    }
                    b2.setTitle("YENİLEME");
                    b2.setMessage("Değişiklikleri görebilmek için görev listesini yenileyiniz.");
                    b2.setPositiveButton("YENİLE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            silinecekler.clear();
                            kullanicilars.clear();
                            new arkaPlan().execute("http://"+ip+":50491/api/Personel");

                        }
                    });
                    b2.show();

                }
                else
                    alterdialog(" ","Lütfen silinecek görev seçiniz !");
            }
        });

    }*/
   /* public void alterdialog(String title,String mesaj)
    {
        android.app.AlertDialog.Builder builder1=new android.app.AlertDialog.Builder(Kullanici_Listele.this);
        builder1.setTitle(title);
        builder1.setMessage(mesaj);
        builder1.setNeutralButton("KAPAT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder1.show();
    }*/
    }
    public void toast_mesaj(String mesaj)
    {

        LayoutInflater inflater;
        View layout;
        TextView text;
        inflater = getLayoutInflater();
        layout = inflater.inflate(R.layout.toast_layout,
                (ViewGroup) findViewById(R.id.custom_toast_container));
        text = layout.findViewById(R.id.text);
        text.setText(mesaj);
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
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

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("executadan gelen", s);

            try {
                JSONArray jsonArray= new JSONArray(s);
                JSONObject jsonObject=new JSONObject();
                for(int i=0; i<jsonArray.length(); i++)
                {
                    boolean flag=true;

                    jsonObject=jsonArray.getJSONObject(i);
                    if(jsonObject.getString("KUL_ADSOYAD").equals(kul_adsoyad[0]+" "+kul_adsoyad[1]))
                    {
                       bolum_idd=jsonObject.getInt("BOLUM_ID");
                        for (BolumModel customer : bolumler) {
                            if (customer.getBolum_id()==bolum_idd) {
                              String bb=customer.getBolum_adi();
                              toast_mesaj("Aranan kullanıcı"+" "+bb+" "+"bölümünde mevcuttur.");
                              flag=false;

                            }
                        }

                       break;
                    }
                    if(flag==true)
                    {
                        toast_mesaj("Aranan kullanıcı bulunamadı");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
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
            Log.d("executadan gelen", s);
            JSONObject jsonObject = new JSONObject();
            try {
                JSONArray jsonArray = new JSONArray(s);
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    bolumler.add(new BolumModel(jsonObject.getString("BOLUM_AD"), jsonObject.getInt("BOLUM_ID")));
                }
              adapter = new Bolum_Adapter(Kullanici_Listele.this, bolumler);

               bolum_list.setAdapter(adapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
}

