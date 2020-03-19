package com.example.asus.denem;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import java.util.List;

public class Kullanici_Ekle2 extends AppCompatActivity {
    List<BolumModel> bolumler=new ArrayList<>();

    List<RolModel> roller=new ArrayList<>();
    String ip="172.31.129.130";
    Spinner spinner_bolum,spinner_rol,spinner_cinsiyet;
    Button btn_ekle;
    String[] cinsiyet=new String[]{"Cinsiyet Seçiniz","Bay","Bayan"};
    ArrayAdapter<String> adapter_cinsiyet;
    EditText tc,adsoyad,kulad,cep,mail,adres;
    int bolum_id,cinsiyet_id,rol_id;
    boolean flag_bolum=false, flag_rol=false;
    VeriGonder veriGonder=new VeriGonder();

    static String eklenen;
    boolean flag=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kullanici__ekle2);
        bolumler.add(new BolumModel("Bölüm Seçiniz",0));
        roller.add(new RolModel(0,"Rol Seçiniz"));
        new arkaPlan().execute("http://" + ip + ":50491/api/Bolum/Get");
        new arkaPlan2().execute("http://" + ip + ":50491/api/Bolum/Roller");

        spinner_bolum=(Spinner)findViewById(R.id.spinner_bolum);
        spinner_cinsiyet=(Spinner)findViewById(R.id.spinner_cinsiyet);
        spinner_rol=(Spinner)findViewById(R.id.spinner_rol);
        tc=(EditText)findViewById(R.id.ekle_tc);
        adsoyad=(EditText)findViewById(R.id.ekle_adsoyad);
        kulad=(EditText)findViewById(R.id.ekle_kulad);
        cep=(EditText)findViewById(R.id.ekle_cep);
        mail=(EditText)findViewById(R.id.ekle_mail);
        adres=(EditText)findViewById(R.id.ekle_adres);

        btn_ekle=(Button)findViewById(R.id.kul_ekle);
        adapter_cinsiyet=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cinsiyet);
        spinner_cinsiyet.setAdapter(adapter_cinsiyet);

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
        spinner_rol.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                rol_id=roller.get(position).getRolid();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btn_ekle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonObject = new JSONObject();
                boolean kontrol=kulad.getText().toString().contains(" ");

                try {
                    String []kul_adsoyad=adsoyad.getText().toString().split(" ");
                    kul_adsoyad[0]=kul_adsoyad[0].substring(0,1).toUpperCase()+kul_adsoyad[0].substring(1).toLowerCase();
                    kul_adsoyad[1]=kul_adsoyad[1].toUpperCase();
                    if(kontrol==true || tc.getText().length()<11)
                    {
                        if(kontrol==true)
                        {
                            toast_mesaj("Kullanıcı adı boşluk içeremez !");
                        }
                        else
                            toast_mesaj("TC kimlik numarası 11 haneli olmalıdır.");
                    }
                    else {

                        jsonObject.put("TC", tc.getText());
                        jsonObject.put("KUL_ADSOYAD", kul_adsoyad[0] + " " + kul_adsoyad[1]);
                        jsonObject.put("KUL_AD", kulad.getText());
                        jsonObject.put("SİFRE",tc.getText());
                        jsonObject.put("CEP", cep.getText());
                        jsonObject.put("ADRES", adres.getText());
                        jsonObject.put("DURUM",1);
                        jsonObject.put("MAİL", mail.getText());
                        jsonObject.put("BOLUM_ID", bolum_id);
                        jsonObject.put("CINSIYET", cinsiyet_id);

                        if (bolum_id == 0) {
                            alterdialog("Bölüm Seçilmedi", "Lütfen eklemek istediğiniz kullanıcının bölümünü seçiniz !");
                        } /*else if (rol_id == 0) {
                            alterdialog("Rol Seçilmedi", "Lütfen eklemek istediğiniz kullanıcının rolünü seçiniz !");
                        }*/ else {
                            veriGonder.webservis("http://" + ip + ":50491/api/Personel", jsonObject);
                            alterdialog("ONAY","Kullanıcı başarıyla eklendi");
                          flag=true;

                        }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String son_eklenen=VeriGonder.dosya1;
                //test();



            }
        });


    }
    public void test()
    {
        if(flag==true)
        {
            String son_eklenen=VeriGonder.dosya1;
            try {
                JSONObject jsonObject=new JSONObject(son_eklenen);
                JSONObject jsonObject1=new JSONObject();
                jsonObject1.put("ROL_ID",rol_id);
                jsonObject1.put("KULLANICI_ID",jsonObject.getString("KUL_ID"));
                veriGonder.webservis("http://" + ip + ":50491/api/KullaniciRol",jsonObject1);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    public void toast_mesaj(String mesaj)
    {
        TextView text;
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
    public void alterdialog(String title,String mesaj)
    {
        AlertDialog.Builder builder1=new AlertDialog.Builder(Kullanici_Ekle2.this);
        builder1.setTitle(title);
        builder1.setMessage(mesaj);
        builder1.setNeutralButton("KAPAT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder1.show();
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
               // ArrayAdapter<BolumModel> adapter_bolumler=new ArrayAdapter<BolumModel>(Kullanici_Ekle2.this,android.R.layout.simple_spinner_item,bolumler);
                SpinnerAdapter adapter_bolumler=new Spinner_Adapter(Kullanici_Ekle2.this,bolumler,roller,flag_bolum,flag_rol);
                spinner_bolum.setAdapter(adapter_bolumler);


            } catch (JSONException e) {
                e.printStackTrace();
            }
           flag_bolum=false
                   ;


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
                SpinnerAdapter adapter_roller=new Spinner_Adapter(Kullanici_Ekle2.this,bolumler,roller,flag_bolum,flag_rol);
                spinner_rol.setAdapter(adapter_roller);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            flag_rol=false;
        }
    }


}
