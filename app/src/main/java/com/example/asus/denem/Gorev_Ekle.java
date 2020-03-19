package com.example.asus.denem;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Gorev_Ekle extends AppCompatActivity {
    EditText gorev_aciklamasi,tarih;
    TextView txt,text,deneme1;
    RadioButton tum,secili;
    ListView kullanici_sec;
    EditText edt_aranan;
    ListView aranan_kul;
    ListView bolum_kullanici;
    CustomAdapter2 adapter;
    TextWatcher textWatcher=null;
    Button kisi_Sec,tarih_sec,gorev_olustur,kul_gorev_tanimla,tum_Sec,ekle;

    Bundle gelen;
    String id,kullanicilar,ad_soyad,bolumad,kul_rolid;
    String[] dizi;
    boolean [] sec;
    boolean flag=false,  flag_bolum=false, flag_rol=false;
    List<BolumModel> bolumler=new ArrayList<>();
    List<RolModel> roller=new ArrayList<>();
   // List<String> tum_kullanicilar=new ArrayList<>();
    List<Kullanicilar> kullanicilars=new ArrayList<>();
    List<Kullanicilar> aranan_kullanicilars=new ArrayList<>();
    ArrayList<Integer> userItem=new ArrayList<>();
    List<Kullanicilar> eklenecek_kul=new ArrayList<Kullanicilar>();
    VeriGonder veriGonder=new VeriGonder();
    RadioGroup RG;

    int bolum_id;


    RadioButton r,r2,r3;
    String ip="172.31.129.130";
    int kulbolum_id;
    LayoutInflater inflater;
    View layout,view1,view2;
    Spinner spinner_bolum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gorev__ekle);

         inflater = getLayoutInflater();
         layout = inflater.inflate(R.layout.toast_layout,
                (ViewGroup) findViewById(R.id.custom_toast_container));

        gorev_aciklamasi=(EditText)findViewById(R.id.gorev_detay_acıklama);



      //  kisi_Sec=(Button)findViewById(R.id.kisi_sec);
        gorev_olustur=(Button)findViewById(R.id.gorev_olustur);
       // kul_gorev_tanimla=(Button)findViewById(R.id.kul_gorev_tanımla);
       // tum_Sec=(Button)findViewById(R.id.tum_sec);
        tarih_sec=(Button)findViewById(R.id.tarih_sec);
        tarih=(EditText)findViewById(R.id.tarih);
      //  txt=(TextView)findViewById(R.id.textView4);

       RG=findViewById(R.id.radiogroup);

        r=findViewById(R.id.radioButton);
        r2=findViewById(R.id.radioButton2);
        r3=findViewById(R.id.radioButton3);

        text = layout.findViewById(R.id.text);

        gelen=getIntent().getExtras();
        id =gelen.getString("id");
        kulbolum_id=gelen.getInt("bolumid");
        kul_rolid=gelen.getString("rol_ad");


        new arkaPlan().execute("http://" + ip + ":50491/api/Personel/KullaniciTumBilgi/0");



        RG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                final RadioButton rb=(RadioButton)findViewById(checkedId);

               switch (group.getCheckedRadioButtonId())
                {
                    case R.id.radioButton:
                         final String son_eklenen = VeriGonder.dosya1;
                        AlertDialog.Builder builder2=new AlertDialog.Builder(Gorev_Ekle.this);

                            builder2.setTitle("GÖREV EKLEME");
                            builder2.setMessage("Eğer EVET butonuna basarsanız sistemde bulunan tüm kullanıcılara tanımlanan görev eklenecektir." +
                                    "Onaylıyor musunuz ?");
                            builder2.setPositiveButton("EVET", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                        try {
                                            JSONObject jsonObject = new JSONObject(son_eklenen);

                                            int gorev_id = jsonObject.getInt("GOREV_ID");

                                            for (int i = 0; i < kullanicilars.size(); i++) {
                                                String url2 = "http://" + ip + ":50491/api/Kullanici_Gorev";

                                                if (!kullanicilars.get(i).getRol_Ad().equals("Dekan")) {
                                                    JSONObject jsonObject1 = new JSONObject();
                                                    jsonObject1.put("KULLANICI_ID", kullanicilars.get(i).getKUL_ID());
                                                    jsonObject1.put("GOREV_ID", gorev_id);
                                                    VeriGonder veriGonder2 = new VeriGonder();

                                                    veriGonder2.webservis(url2, jsonObject1);
                                                }
                                            }
                                            alterdialog("ONAY", "Sistemde bulunan tüm kullanıcılara görev tanımlandı.");


                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                }
                            });
                            builder2.setNegativeButton("HAYIR", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            builder2.show();


                        break;
                    case R.id.radioButton2:

                       AlertDialog.Builder builder1=new AlertDialog.Builder(Gorev_Ekle.this);

                        view1=getLayoutInflater().inflate(R.layout.g_ekle_bolumsec,null);
                        spinner_bolum=(Spinner)view1.findViewById(R.id.g_ekle_bolumler);
                        new arkaPlan2().execute("http://" + ip + ":50491/api/Bolum/Get");
                        spinner_bolum.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                bolum_id =bolumler.get(position).getBolum_id();
                                bolumad=bolumler.get(position).getBolum_adi();
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                        builder1.setTitle("BÖLÜM SEÇME İŞLEMİ");
                        builder1.setNegativeButton("Kapat", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                            }
                        });
                        builder1.setPositiveButton("Seç", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final String son_eklenen = VeriGonder.dosya1;
                                if(!kul_rolid.equals("Dekan"))
                                {
                                    if(bolum_id!=kulbolum_id)
                                    {
                                        alterdialog("UYARI","Bu bölüme görev ekleme yetkiniz bulunmamaktadır !");
                                    }
                                    else
                                    {
                                        JSONObject jsonObject= null;
                                        try {
                                            jsonObject = new JSONObject(son_eklenen);
                                            int gorev_id=jsonObject.getInt("GOREV_ID");
                                            String url2 = "http://"+ip+":50491/api/Kullanici_Gorev";
                                            for(int i=0; i<kullanicilars.size(); i++)
                                            {
                                                if(kullanicilars.get(i).getBOLUM_ID()==kulbolum_id) {
                                                    if(!kullanicilars.get(i).getRol_Ad().equals("Dekan")&&!kullanicilars.get(i).getRol_Ad().equals("Bölüm Başkanı")) {
                                                        JSONObject jsonObject1 = new JSONObject();
                                                        jsonObject1.put("KULLANICI_ID", kullanicilars.get(i).getKUL_ID());
                                                        jsonObject1.put("GOREV_ID", gorev_id);
                                                        VeriGonder veriGonder2 = new VeriGonder();
                                                        veriGonder2.webservis(url2, jsonObject1);
                                                    }
                                                }

                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        alterdialog("ONAY",bolumad+" "+"bölümde bulunan tüm kullanıcılara görev tanımlandı.");
                                    }
                                }
                                else
                                {
                                    JSONObject jsonObject= null;
                                    try {
                                        jsonObject = new JSONObject(son_eklenen);
                                        int gorev_id=jsonObject.getInt("GOREV_ID");
                                        String url2 = "http://"+ip+":50491/api/Kullanici_Gorev";
                                        for(int i=0; i<kullanicilars.size(); i++)
                                        {
                                            if(kullanicilars.get(i).getBOLUM_ID()==bolum_id) {
                                                if(!kullanicilars.get(i).getRol_Ad().equals("Dekan")) {
                                                    JSONObject jsonObject1 = new JSONObject();
                                                    jsonObject1.put("KULLANICI_ID", kullanicilars.get(i).getKUL_ID());
                                                    jsonObject1.put("GOREV_ID", gorev_id);
                                                    VeriGonder veriGonder2 = new VeriGonder();
                                                    veriGonder2.webservis(url2, jsonObject1);
                                                }
                                            }

                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    alterdialog("ONAY",bolumad+" "+"bölümde bulunan tüm kullanıcılara görev tanımlandı.");
                                }
                            }
                        });
                        builder1.setView(view1);
                        builder1.show();
                        break;
                    case R.id.radioButton3:
                        AlertDialog.Builder builder3=new AlertDialog.Builder(Gorev_Ekle.this);
                        view2=getLayoutInflater().inflate(R.layout.kullanici_ara,null);
                        edt_aranan=(EditText)view2.findViewById(R.id.edt_ara2);
                        aranan_kul=(ListView)view2.findViewById(R.id.aranan_kul);
                       // deneme1=(TextView)view2.findViewById(R.id.deneme1);
                        textWatcher=new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                                aranan_kullanicilars.clear();
                                for(int i=0; i<kullanicilars.size(); i++)
                                {
                                    String adsoyad_knt=kullanicilars.get(i).getKUL_ADSOYAD().toString();

                                    //kulbolum_id
                                    if(!kul_rolid.equals("Dekan"))
                                    {
                                        if(kulbolum_id==kullanicilars.get(i).getBOLUM_ID())
                                        {
                                            if (adsoyad_knt.toLowerCase().contains(s)) {
                                                if (!kullanicilars.get(i).getRol_Ad().equals("Dekan")) {
                                                    aranan_kullanicilars.add(kullanicilars.get(i));
                                                }
                                            }
                                        }
                                    }
                                    else {
                                        if (adsoyad_knt.toLowerCase().contains(s)) {
                                            if (!kullanicilars.get(i).getRol_Ad().equals("Dekan")) {
                                                aranan_kullanicilars.add(kullanicilars.get(i));
                                            }
                                        }
                                    }
                                }
                                 adapter = new CustomAdapter2(Gorev_Ekle.this, aranan_kullanicilars);
                                 aranan_kul.setAdapter(adapter);
                                 aranan_kul.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                     @Override
                                     public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                         Kullanicilar model=aranan_kullanicilars.get(position);
                                         if(model.isDurum())
                                         {
                                             model.setDurum(false);
                                             if(eklenecek_kul.contains(model))
                                             {
                                                 eklenecek_kul.remove(model);
                                             }
                                         }
                                         else {
                                             model.setDurum(true);
                                             eklenecek_kul.add(model);
                                         }
                                         aranan_kullanicilars.set(position,model);
                                         adapter.update(aranan_kullanicilars);

                                     }
                                 });

                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                            }
                        };
                        edt_aranan.addTextChangedListener(textWatcher);
                        builder3.setPositiveButton("EKLE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                final String son_eklenen = VeriGonder.dosya1;
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(son_eklenen);
                                    int gorev_id = jsonObject.getInt("GOREV_ID");
                                    String url2 = "http://"+ip+":50491/api/Kullanici_Gorev";
                                    for(int i=0; i<eklenecek_kul.size(); i++)
                                    {
                                        JSONObject jsonObject1 = new JSONObject();
                                        jsonObject1.put("KULLANICI_ID", eklenecek_kul.get(i).getKUL_ID());
                                        jsonObject1.put("GOREV_ID", gorev_id);
                                        VeriGonder veriGonder2 = new VeriGonder();
                                        veriGonder2.webservis(url2, jsonObject1);
                                    }
                                    alterdialog("ONAY", "Seçilen kullanıcılara görev başarıyla eklendi.");

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }
                        });
                        builder3.setNegativeButton("VAZGEÇ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        builder3.setView(view2);
                        builder3.show();
                        break;
                    default:
                        break;
                }

            }
        });

        tarih_sec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // klavye_kapat();
                Calendar takvim=Calendar.getInstance();
                //takvim nesnesi oluşturulur.
                int yil,ay,gun;
                yil=takvim.get(Calendar.YEAR);
                ay=takvim.get(Calendar.MONTH);
                gun=takvim.get(Calendar.DAY_OF_MONTH);
                //güncel gün ay ve yıl değerlerini alıyoruz
                DatePickerDialog dpd=new DatePickerDialog(Gorev_Ekle.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        tarih.setText(year+"-"+(month+1)+"-"+dayOfMonth);

                    }
                },yil,ay,gun);
                dpd.setButton(DatePickerDialog.BUTTON_POSITIVE, "Seç", dpd);
                dpd.setButton(DatePickerDialog.BUTTON_NEGATIVE, "İptal", dpd);
                dpd.show();
                }

        });

        gorev_olustur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String url1 = "http://"+ip+":50491/api/Gorev";
                JSONObject jsonObject = new JSONObject();

                String aciklama = gorev_aciklamasi.getText().toString();
                String son_tarih = tarih.getText().toString();
                if (!aciklama.trim().equals("")) {
                    if(!son_tarih.trim().equals("")) {
                        try {
                            jsonObject.put("GOREV_ACIKLAMA", aciklama);
                            jsonObject.put("SON_TARIH", son_tarih);
                            jsonObject.put("GOREVLENDİREN_ID", id);
                            //VeriGonder veriGonder=new VeriGonder();
                            veriGonder.webservis(url1, jsonObject);

                            alterdialog("BAŞARILI","Görev başarılı bir şekilde oluşturuldu");
                            if(kul_rolid.equals("Dekan"))
                            {
                                r.setEnabled(true);
                                r2.setEnabled(true);
                                r3.setEnabled(true);
                            }
                            else
                            {
                                r2.setEnabled(true);
                                r3.setEnabled(true);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else {
                        alterdialog("","Görevin son geçerlilik tarihini giriniz !");
                    }
                }
                else {
                    alterdialog("","Görevin açıklamasını giriniz !");

                }
            }
        });
    }

    public void toast_mesaj(String mesaj)
    {
        text.setText(mesaj);
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
    public void alterdialog(String title,String mesaj)
    {
        AlertDialog.Builder builder1=new AlertDialog.Builder(Gorev_Ekle.this);
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

                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        kullanicilars.add(new Kullanicilar(jsonObject.getString("KUL_ADSOYAD1"), jsonObject.getBoolean("CINSIYET1"), jsonObject.getInt("KUL_ID1"), jsonObject.getString("ADRES1"), jsonObject.getString("CEP1"), jsonObject.getString("MAİL1"), jsonObject.getString("ROL_ADI1"),jsonObject.getInt("BOLUM_ID1"),jsonObject.getBoolean("DURUM1")));

                    }

              //  adapter = new CustomAdapter2(Gorev_Ekle.this, kullanicilars);
               // bolum_kullanici.setAdapter(adapter);
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
                SpinnerAdapter adapter_bolumler=new Spinner_Adapter(Gorev_Ekle.this,bolumler,roller,flag_bolum,flag_rol);
                spinner_bolum.setAdapter(adapter_bolumler);


            } catch (JSONException e) {
                e.printStackTrace();
            }
            flag_bolum=false
            ;


        }
    }




}
