package com.example.asus.denem;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

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

public class Islemler extends AppCompatActivity {
    Spinner spinner_bolum,spinner_rol;
    List<BolumModel> bolumler=new ArrayList<>();

    List<RolModel> roller=new ArrayList<>();
    String ip="172.31.129.130";
    Button rol_ekle,bolum_ekle,rol_sil,bolum_sil,duzenlerol_btn,duzenleb_btn;
    EditText edt_rolad,edt_bolumad,duzenle_rol,duzenle_bolum;
    RadioGroup RG,BRG;
    boolean flag_bolum=false, flag_rol=false;
    int bolum_id,cinsiyet_id,rol_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_islemler2);
        bolumler.add(new BolumModel("Bölüm Seçiniz",0));
        roller.add(new RolModel(0,"Rol Seçiniz"));
        RG=findViewById(R.id.radiogroup);
        BRG=findViewById(R.id.b_radiogroup);

        spinner_bolum=(Spinner) findViewById(R.id.islem_bolum);
        spinner_rol=(Spinner) findViewById(R.id.islem_rol);
        duzenle_rol=(EditText)findViewById(R.id.rol_adi_duzenle);
        duzenle_bolum=(EditText)findViewById(R.id.bolum_adi_duzenle);


        edt_bolumad=(findViewById(R.id.islem_bolum_adi));
        edt_rolad=findViewById(R.id.rol_adi);

        new arkaPlan().execute("http://" + ip + ":50491/api/Bolum/Get");
        new arkaPlan2().execute("http://" + ip + ":50491/api/Bolum/Roller");

        rol_ekle=(Button)findViewById(R.id.yeni_rol);
        duzenlerol_btn=(Button)findViewById(R.id.rol_duzenle);
        duzenleb_btn=(Button)findViewById(R.id.bolum_duzenle);
        bolum_ekle=(Button)findViewById(R.id.yeni_bolum);
      //  rol_sil=findViewById(R.id.sil_rol);
        bolum_sil=findViewById(R.id.sil_bolum);
        rol_ekle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(edt_rolad.getText().toString()))
                {
                    JSONObject jsonObject=new JSONObject();
                    try {
                        jsonObject.put("ROL_ADI",edt_rolad.getText().toString());
                        VeriGonder veriGonder=new VeriGonder();
                        veriGonder.webservis("http://" + ip + ":50491/api/Bolum/PostRol",jsonObject);
                        alterdialog("BAŞARILI","Rol sisteme başarılı bir şekilde eklenmiştir.");
                        roller.clear();
                        new arkaPlan2().execute("http://" + ip + ":50491/api/Bolum/Roller");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else                         alterdialog("HATA","Rol Adı boş bırakılamaz");

            }
        });
        RG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (group.getCheckedRadioButtonId())
                {
                    case R.id.r_radioButton:
                        duzenle_rol.setVisibility(View.INVISIBLE);
                        duzenlerol_btn.setVisibility(View.INVISIBLE);


                        if(rol_id==0)
                        {
                            alterdialog("HATA","Silmek istediğiniz rolü seçiniz");
                        }else
                        {
                            AlertDialog.Builder builder2=new AlertDialog.Builder(Islemler.this);

                            builder2.setTitle("ROL SİLME");
                            builder2.setMessage("Eğer EVET butonuna basarsanız bu rol sistemden silenecektir. Bu işlemi gerçekleştirmek istiyormusunuz ?");

                          builder2.setPositiveButton("EVET", new DialogInterface.OnClickListener() {
                              @Override
                              public void onClick(DialogInterface dialog, int which) {
                                  VeriSil veriSil=new VeriSil();
                                  veriSil.webservis("http://" + ip + ":50491/api/Bolum/DeleteR/"+rol_id);
                                  alterdialog("BAŞARILI","Rol sistemden başarılı bir şekilde silinmiştir.");
                                  roller.clear();
                                  new arkaPlan2().execute("http://" + ip + ":50491/api/Bolum/Roller");

                              }
                          });
                          builder2.setNegativeButton("HAYIR", new DialogInterface.OnClickListener() {
                              @Override
                              public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                              }
                          });
                          builder2.show();

                        }
                        break;
                    case R.id.r_radioButton2:

                        if(rol_id==0)
                        {
                            alterdialog("HATA","Düzenlemek istediğiniz rolü seçiniz");
                        }
                        else
                        {
                            if(rol_id==1||rol_id==2)
                            {
                                alterdialog("YETKİ SINIRI","Bu rolün yetkisinden dolayı düzenlemeye izin verilmemektedir.");
                            }
                            else {
                                duzenle_rol.setVisibility(View.VISIBLE);
                                duzenlerol_btn.setVisibility(View.VISIBLE);
                                for(int i=0; i<roller.size(); i++)
                                {
                                    if(roller.get(i).getRolid()==rol_id)
                                    {
                                        duzenle_rol.setText(roller.get(i).getRol_adi());
                                        break;
                                    }
                                }
                            }


                        }


                        break;
                }

            }
        });
        BRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (group.getCheckedRadioButtonId())
                {
                case R.id.b_radioButton:
                duzenle_bolum.setVisibility(View.INVISIBLE);
                duzenleb_btn.setVisibility(View.INVISIBLE);


                if(bolum_id==0)
                {
                    alterdialog("HATA","Silmek istediğiniz bolümü seçiniz");
                }else
                {
                    AlertDialog.Builder builder2=new AlertDialog.Builder(Islemler.this);

                    builder2.setTitle("BÖLÜM SİLME");
                    builder2.setMessage("Eğer EVET butonuna basarsanız bu bölüm sistemden silenecektir. Bu işlemi gerçekleştirmek istiyormusunuz ?");

                    builder2.setPositiveButton("EVET", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                                VeriSil veriSil=new VeriSil();
                                veriSil.webservis("http://" + ip + ":50491/api/Bolum/DeleteB/"+bolum_id);
                                alterdialog("BAŞARILI","Bölüm sistemden başarılı bir şekilde silinmiştir.");
                                bolumler.clear();
                                new arkaPlan().execute("http://" + ip + ":50491/api/Bolum/Get");



                        }
                    });
                    builder2.setNegativeButton("HAYIR", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder2.show();

                }
                break;
                case R.id.b_radioButton2:

                if(bolum_id==0)
                {
                    alterdialog("HATA","Düzenlemek istediğiniz bölümü seçiniz");
                }
                else
                {

                        duzenle_bolum.setVisibility(View.VISIBLE);
                        duzenleb_btn.setVisibility(View.VISIBLE);
                        for(int i=0; i<bolumler.size(); i++)
                        {
                            if(bolumler.get(i).getBolum_id()==bolum_id)
                            {
                                duzenle_bolum.setText(bolumler.get(i).getBolum_adi());
                                break;
                            }
                        }



                }


                break;
            }

            }
        });
        duzenlerol_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!duzenle_rol.getText().toString().isEmpty())
                {
                    VeriGuncelle veriGuncelle=new VeriGuncelle();
                    JSONObject jsonObject=new JSONObject();
                    try {
                        jsonObject.put("ROL_ADI",duzenle_rol.getText().toString());
                        veriGuncelle.webservis("http://"+ip+":50491/api/Bolum/PutR/"+rol_id,jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        duzenleb_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!duzenle_bolum.getText().toString().isEmpty())
                {
                    VeriGuncelle veriGuncelle=new VeriGuncelle();
                    JSONObject jsonObject=new JSONObject();
                    try {
                        jsonObject.put("BOLUM_AD",duzenle_bolum.getText().toString());
                        veriGuncelle.webservis("http://"+ip+":50491/api/Bolum/PutB/"+bolum_id,jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
       /* rol_sil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rol_id==0)
                {
                    alterdialog("HATA","Silmek istediğiniz rolü seçiniz");
                }else
                {
                    VeriSil veriSil=new VeriSil();
                    veriSil.webservis("http://" + ip + ":50491/api/Bolum/DeleteR/"+rol_id);
                    alterdialog("BAŞARILI","Rol sistemden başarılı bir şekilde silinmiştir.");
                    roller.clear();
                    new arkaPlan2().execute("http://" + ip + ":50491/api/Bolum/Roller");

                }
            }
        });*/
        bolum_ekle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(edt_bolumad.getText().toString()))
                {
                    JSONObject jsonObject=new JSONObject();
                    try {
                        jsonObject.put("BOLUM_AD",edt_bolumad.getText().toString());
                        VeriGonder veriGonder=new VeriGonder();
                        veriGonder.webservis("http://" + ip + ":50491/api/Bolum/Post",jsonObject);
                        alterdialog("BAŞARILI","Bölüm sisteme başarılı bir şekilde eklenmiştir.");
                        bolumler.clear();
                        new arkaPlan().execute("http://" + ip + ":50491/api/Bolum/Get");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else                         alterdialog("HATA","Bölüm Adı boş bırakılamaz");

            }
        });
       /* bolum_sil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bolum_id==0)
                {
                    alterdialog("HATA","Silmek istediğiniz bolümü seçiniz");
                }else
                {
                    VeriSil veriSil=new VeriSil();
                    veriSil.webservis("http://" + ip + ":50491/api/Bolum/DeleteB/"+bolum_id);
                    alterdialog("BAŞARILI","Bölüm sistemden başarılı bir şekilde silinmiştir.");
                    bolumler.clear();
                    new arkaPlan().execute("http://" + ip + ":50491/api/Bolum/Get");

                }
            }
        });*/
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

    }
    public void alterdialog(String title,String mesaj)
    {
        AlertDialog.Builder builder1=new AlertDialog.Builder(Islemler.this);
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
                SpinnerAdapter adapter_bolumler=new Spinner_Adapter(Islemler.this,bolumler,roller,flag_bolum,flag_rol);
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
                SpinnerAdapter adapter_roller=new Spinner_Adapter(Islemler.this,bolumler,roller,flag_bolum,flag_rol);
                spinner_rol.setAdapter(adapter_roller);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            flag_rol=false;
        }
    }
}
