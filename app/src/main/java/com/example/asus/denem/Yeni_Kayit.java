package com.example.asus.denem;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

public class Yeni_Kayit extends AppCompatActivity {
    String[] cinsiyet=new String[]{"Cinsiyet Seçiniz","Bay","Bayan"};
    String[] sorular=new String[]{"Güvenlik sorusu seçiniz","İlk okul öğretmeniniz ?","İlk hayvanınızın adı ?","En sevdiğiniz renk ?","En sevdiğiniz yemek ?","En sevdiğiniz araba markası ?"};

    List<BolumModel> bolumler=new ArrayList<>();
    TextView text;

    List<RolModel> roller=new ArrayList<>();
    ArrayAdapter<String> adapter_cinsiyet,adapter_soru;
    Spinner spinner_cinsiyet,spinner_soru,spinner_bolum,spinner_rol;
    EditText tc,adsoyad,kulad,cep,mail,sifre,cevap,edt_dogum_tarih;
    Button kayitol,dogum_tarih;
    int bolum_id,cinsiyet_id,rol_id;
    String yedek_sifre;

    boolean flag_bolum=false, flag_rol=false;
    VeriGonder veriGonder=new VeriGonder();
    String ip="172.31.129.130";

    private static final Pattern PASSWORD_PATTERN=
            Pattern.compile("^"+
                    "(?=.*[0-9])"+
                    "(?=.*[a-z])"+
                    "(?=.*[A-Z])"+
                    "(?=.*[@#$%^&+=])"+
                    "(?=\\S+$)"+
                    ".{6,}"+
                    "$");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yeni__kayit);



        bolumler.add(new BolumModel("Bölüm Seçiniz",0));
        roller.add(new RolModel(0,"Rol Seçiniz"));
        new arkaPlan().execute("http://" + ip + ":50491/api/Bolum/Get");
       // new arkaPlan2().execute("http://" + ip + ":50491/api/Bolum/Roller");
        spinner_cinsiyet=(Spinner)findViewById(R.id.yk_spinner_cinsiyet);
        spinner_soru=(Spinner)findViewById(R.id.yk_soru);
        spinner_bolum=(Spinner)findViewById(R.id.yk_spinner_bolum);
      //  spinner_rol=(Spinner)findViewById(R.id.yk_spinner_rol);

        adapter_cinsiyet=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cinsiyet);
        spinner_cinsiyet.setAdapter(adapter_cinsiyet);
        adapter_soru=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sorular);
        spinner_soru.setAdapter(adapter_soru);

        adsoyad=(EditText)findViewById(R.id.yk_adsoyad);
        kulad=(EditText)findViewById(R.id.yk_kulad);

        cep=(EditText)findViewById(R.id.yk_cep);
        mail=(EditText)findViewById(R.id.yk_mail);
        sifre=(EditText)findViewById(R.id.yk_sifre);
        cevap=(EditText)findViewById(R.id.yk_cevap);
        tc=(EditText)findViewById(R.id.yk_tc);
        edt_dogum_tarih=(EditText)findViewById(R.id.yk_dogumtarihi);

        kayitol=(Button)findViewById(R.id.yk_kayitol);
        dogum_tarih=(Button)findViewById(R.id.yk_tarih_sec);



        dogum_tarih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  klavye_kapat();
                Calendar takvim=Calendar.getInstance();
                //takvim nesnesi oluşturulur.
                int yil,ay,gun;
                yil=takvim.get(Calendar.YEAR);
                ay=takvim.get(Calendar.MONTH);
                gun=takvim.get(Calendar.DAY_OF_MONTH);
                //güncel gün ay ve yıl değerlerini alıyoruz
                DatePickerDialog dpd=new DatePickerDialog(Yeni_Kayit.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        edt_dogum_tarih.setText(year+"-"+(month+1)+"-"+dayOfMonth);

                    }
                },yil,ay,gun);
                dpd.setButton(DatePickerDialog.BUTTON_POSITIVE, "Seç", dpd);
                dpd.setButton(DatePickerDialog.BUTTON_NEGATIVE, "İptal", dpd);
                dpd.show();
            }
        });

        spinner_cinsiyet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0) {
                    cinsiyet_id = position - 1;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_bolum.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bolum_id=bolumler.get(position).getBolum_id();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
        kayitol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean kontrol=kulad.getText().toString().contains(" ");

                String []kul_adsoyad=adsoyad.getText().toString().split(" ");
                kul_adsoyad[0]=kul_adsoyad[0].substring(0,1).toUpperCase()+kul_adsoyad[0].substring(1).toLowerCase();
                kul_adsoyad[1]=kul_adsoyad[1].toUpperCase();
               // String kul_adsoyad = adsoyad.getText().toString().substring(0,1).toUpperCase() + adsoyad.getText().toString().substring(1).toLowerCase();

                if(TextUtils.isEmpty(tc.getText().toString()) || TextUtils.isEmpty(adsoyad.getText().toString()) ||TextUtils.isEmpty(kulad.getText().toString())||TextUtils.isEmpty(sifre.getText().toString())|| spinner_bolum.getSelectedItemPosition()==0 ||spinner_soru.getSelectedItemPosition()==0 ||TextUtils.isEmpty(cevap.getText().toString())) {
                // Toast.makeText(Yeni_Kayit.this, "Zorunlu (*) alanlar boş bırakılamaz ", Toast.LENGTH_LONG).show();
                    toast_mesaj("Zorunlu (*) alanlar boş bırakılamaz ");
                    return;
                }
                else if(validatePassword()==false)
                {
                    toast_mesaj("Güçsüz Şifre, şifre en az bir küçük, bir büyük , bir özel karakter ve bir rakamdan oluşmalıdır");
                }
                else {

                    if(kontrol==true || tc.getText().length()<11) {
                        if (kontrol == true) {
                            toast_mesaj("Kullanıcı adı boşluk içeremez !");
                        } else
                            toast_mesaj("TC kimlik numarası 11 haneli olmalıdır.");
                    }
                    else
                    {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("KUL_ADSOYAD", kul_adsoyad[0]+" "+kul_adsoyad[1]);
                        jsonObject.put("TC", tc.getText());
                        jsonObject.put("KUL_AD", kulad.getText());
                        jsonObject.put("CEP", cep.getText());
                        jsonObject.put("SIFRE", sifre.getText());
                        jsonObject.put("MAİL", mail.getText());
                        jsonObject.put("BOLUM_ID", bolum_id);
                        jsonObject.put("DOGUM_TARIHI", edt_dogum_tarih);
                        jsonObject.put("CINSIYET", cinsiyet_id);
                        jsonObject.put("SORU",spinner_soru.getSelectedItem());
                        jsonObject.put("CEVAP",cevap.getText());

                            veriGonder.webservis("http://" + ip + ":50491/api/YeniKayit", jsonObject);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    alterdialog("BAŞARILI","Kaydınız başarılı şekilde alınmıştır, Yetkili kullanıcı kaydınızı onayladığında sisteme giriş yapabilirsiniz");
                    }
                }
            }
        });
    }
    private boolean validatePassword()
    {
        String password=sifre.getText().toString().trim();
        if(!PASSWORD_PATTERN.matcher(password).matches()){
            return false;
        }
        else return true;
        //return false;
    }
   /* public void klavye_kapat()
    {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            //         }
    }*/
    public void toast_mesaj(String mesaj)
    {

        LayoutInflater inflater;
        View layout;
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
               // ArrayAdapter<BolumModel> adapter_bolumler=new ArrayAdapter<BolumModel>(Yeni_Kayit.this,android.R.layout.simple_spinner_item,bolumler);
               SpinnerAdapter adapter_bolumler=new Spinner_Adapter(Yeni_Kayit.this,bolumler,roller,flag_bolum,flag_rol);
                spinner_bolum.setAdapter(adapter_bolumler);


            } catch (JSONException e) {
                e.printStackTrace();
            }
            flag_bolum=false
            ;


        }
    }
   /* class arkaPlan2 extends AsyncTask<String, String, String> {
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
        public void rolad()
        {

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
                SpinnerAdapter adapter_roller=new Spinner_Adapter(Yeni_Kayit.this,bolumler,roller,flag_bolum,flag_rol);
                spinner_rol.setAdapter(adapter_roller);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            flag_rol=false;
        }
    }*/
    public void alterdialog(String title,String mesaj)
    {
        AlertDialog.Builder builder1=new AlertDialog.Builder(Yeni_Kayit.this);
        builder1.setTitle(title);
        builder1.setMessage(mesaj);
        builder1.setNeutralButton("KAPAT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i=new Intent(Yeni_Kayit.this,Giris.class);
                startActivity(i);
            }
        });
        builder1.show();
    }
}
