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
import android.widget.ListView;
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

public class Rol_Duzenle extends AppCompatActivity {
    List<BolumModel> bolumler=new ArrayList<>();
    List<RolModel> roller=new ArrayList<>();
    List<IzinModel> izinmodel=new ArrayList<>();
    boolean flag_bolum=false, flag_rol=false;
    Spinner spinner_rol;
    int rol_id;
    String ip="172.31.129.130";
    ListView izinlist;
    Button izin_btn,rol_ekle;
    String rol_ad;
    EditText edt_rolad;
    IzinAdapter adapter;
    boolean flag=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rol__duzenle);
        spinner_rol=(Spinner)findViewById(R.id.rol_spinner);
        izinlist=(ListView)findViewById(R.id.rol_izinList);
        new arkaPlan3().execute("http://" + ip + ":50491/api/Bolum/Roller");
        izin_btn=(Button)findViewById(R.id.btn_izin);



          izin_btn.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  AlertDialog.Builder builder=new AlertDialog.Builder(Rol_Duzenle.this);
                  builder.setTitle("ONAYLAMA");
                  builder.setMessage(rol_ad+" "+"Rolünün yetkilerini değiştirmek istediğinize emin misiniz ?");
                  builder.setPositiveButton("EVET", new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialog, int which) {
                          VeriSil veriSil=new VeriSil();
                          veriSil.webservis("http://"+ip+":50491/api/MenuRol/Delete/"+rol_id);
                          for(int i=0; i<izinmodel.size(); i++)
                          {
                              if(izinmodel.get(i).isDurum())
                              {
                                  VeriGonder veriGonder=new VeriGonder();
                                  JSONObject jsonObject=new JSONObject();
                                  try {
                                      jsonObject.put("ROL_ID",izinmodel.get(i).getRol_id());
                                      jsonObject.put("MENU_ID",izinmodel.get(i).getMenu_id());
                                      veriGonder.webservis("http://"+ip+":50491/api/MenuRol",jsonObject);

                                  } catch (JSONException e) {
                                      e.printStackTrace();
                                  }
                              }
                          }

                      }
                  });
                  builder.setNegativeButton("HAYIR", new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialog, int which) {
                          dialog.dismiss();
                      }
                  });
                  builder.show();
              }
          });

        spinner_rol.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                izinmodel.clear();
                rol_id=roller.get(position).getRolid();
                rol_ad=roller.get(position).getRol_adi();
                new arkaPlan().execute("http://" + ip + ":50491/api/MenuRol/Izinler/"+rol_id);
                flag=true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        izinlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                IzinModel model=izinmodel.get(position);
                if(model.isDurum())
                {
                    model.setDurum(false);

                }
                else
                {
                    model.setDurum(true);

                }

                izinmodel.set(position,model);
                adapter.update(izinmodel);
            }
        });

    }
    public void alterdialog(String title,String mesaj)
    {
        AlertDialog.Builder builder1=new AlertDialog.Builder(Rol_Duzenle.this);
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
            super.onPostExecute(s);
            flag_rol=true;
            Log.d("executadan gelen", s);
            JSONObject jsonObject = new JSONObject();
            try {
                JSONArray jsonArray = new JSONArray(s);
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    izinmodel.add(new IzinModel(jsonObject.getString("MenuAd1"),jsonObject.getInt("RolID1"),jsonObject.getInt("MenuID1"),jsonObject.getBoolean("Izin")));
                }
                // ArrayAdapter<RolModel> adapter_roller=new ArrayAdapter<RolModel>(Kullanici_Ekle2.this,android.R.layout.simple_spinner_item,roller);
                 adapter=new IzinAdapter(Rol_Duzenle.this,izinmodel);
                izinlist.setAdapter(adapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            flag_rol=false;
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
                SpinnerAdapter adapter_roller=new Spinner_Adapter(Rol_Duzenle.this,bolumler,roller,flag_bolum,flag_rol);
                spinner_rol.setAdapter(adapter_roller);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            flag_rol=false;
        }
    }
}
