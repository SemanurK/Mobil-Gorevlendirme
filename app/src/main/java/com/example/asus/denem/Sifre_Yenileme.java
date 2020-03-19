package com.example.asus.denem;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import java.util.regex.Pattern;

public class Sifre_Yenileme extends AppCompatActivity {
    String[] sorular=new String[]{"Sistemdeki güvenlik sorusu seçiniz","İlk okul öğretmeniniz ?","İlk hayvanınızın adı ?","En sevdiğiniz renk ?","En sevdiğiniz yemek ?","En sevdiğiniz araba markası ?"};
    Spinner spinner_soru;
    ArrayAdapter<String> adapter_soru;

   EditText su_tc,su_kulad,su_cevap,su_sifre,su_t_sifre;
   Button dogrula,sorgula,degistir;
   JSONObject kullanici;
   TextView text;
   String ip="172.31.129.130";
   String tc,kulad;
   List<JSONObject> list_aranan=new ArrayList<>();
    boolean flagsorgula=false,flagdogrula=false;
    int sayac=3;

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
        setContentView(R.layout.activity_sifre__yenileme);
        spinner_soru=(Spinner)findViewById(R.id.su_soru);
        adapter_soru=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sorular);
        spinner_soru.setAdapter(adapter_soru);

        su_tc=(EditText)findViewById(R.id.su_tc);
        su_kulad=(EditText)findViewById(R.id.su_kulad);
        su_cevap=(EditText)findViewById(R.id.su_cevap);
        su_sifre=(EditText)findViewById(R.id.su_sifre);
        su_t_sifre=(EditText)findViewById(R.id.su_tekrarsifre);

        text=(TextView)findViewById(R.id.deneme);
        dogrula=(Button)findViewById(R.id.dogrula);
        sorgula=(Button)findViewById(R.id.sorgula);
        degistir=(Button)findViewById(R.id.degistir);

        spinner_soru.setEnabled(false);
        su_cevap.setEnabled(false);
        dogrula.setEnabled(false);
        su_sifre.setEnabled(false);
        su_t_sifre.setEnabled(false);
        degistir.setEnabled(false);


        sorgula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new arkaPlan3().execute("http://"+ip+":50491/api/Personel");

                }



        });
        dogrula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(kullanici.getString("SORU").equals(spinner_soru.getSelectedItem()) && kullanici.getString("CEVAP").equals(su_cevap.getText().toString()))
                    {
                      //  alterdialog("KONTROL","Soruya doğru cevap vererek"+" "+kullanici.getString("KUL_ADSOYAD").toString()+" "+ "adlı kullanıcı olduğunuzu doğruladık ");
                        toast_mesaj("Güvenlik sorusunu doğru cevapyalarak"+" "+kullanici.getString("KUL_ADSOYAD").toString()+" "+"olduğunuzu kanıtladınız. Şifrenizi Yenileyebilirsiniz");
                        su_sifre.setEnabled(true);
                        su_t_sifre.setEnabled(true);
                        degistir.setEnabled(true);


                    }
                    else
                    {
                        sayac--;
                        if(sayac<=0)
                        {
                            toast_mesaj("Doğrulama seçeneklerine 3 kez yanlış cevap verdiniz !"+"Kaydınız bloke edilmiştir. Lütfen Yetkili ile görüşünüz");
                            Intent ıntent = new Intent(Sifre_Yenileme.this, Giris.class);
                            startActivity(ıntent);
                        }
                        else{
                            toast_mesaj("Girdiğiniz bilgiler sistemdekiyle uyuşmuyor ! Lütfen tekrar deneyin ");
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        degistir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flagsorgula=false;
                if((su_sifre.getText().toString().equals(su_t_sifre.getText().toString())&&validatePassword()==true))
                {
                    try {
                        kullanici.put("SİFRE",su_sifre.getText().toString());
                        VeriGuncelle veriGuncelle=new VeriGuncelle();
                        veriGuncelle.webservis("http://"+ip+":50491/api/Personel/Put/"+kullanici.getString("KUL_ID"),kullanici);
                        AlertDialog.Builder builder1=new AlertDialog.Builder(Sifre_Yenileme.this);
                        builder1.setTitle("ONAY");
                        builder1.setMessage("Şifre değiştirme işlemi başarıyla gerçekleşti.");
                        builder1.setNeutralButton("KAPAT", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i=new Intent(Sifre_Yenileme.this,Giris.class);
                                startActivity(i);
                            }
                        });
                        builder1.show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else
                    toast_mesaj("Girilen aynı ve  en az bir küçük, bir büyük , bir özel karakter ve bir rakamdan oluşmalıdır .");

            }
        });


    }
    private boolean validatePassword()
    {
        String password=su_sifre.getText().toString().trim();
        if(!PASSWORD_PATTERN.matcher(password).matches()){
            return false;
        }
        else return true;
        //return false;
    }
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
    public void alterdialog(String title,String mesaj)
    {
        AlertDialog.Builder builder1=new AlertDialog.Builder(Sifre_Yenileme.this);
        builder1.setTitle(title);
        builder1.setMessage(mesaj);


    builder1.setPositiveButton("EVET", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

                spinner_soru.setEnabled(true);
                su_cevap.setEnabled(true);
                dogrula.setEnabled(true);


        }
    });
    builder1.setNegativeButton("HAYIR", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
           // list_aranan.clear();
            su_tc.setText("");
            su_kulad.setText("");
            dialog.dismiss();
        }
    });


        builder1.show();
    }
    boolean flag=false;

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
            Log.d("executadan gelen", s);

            kullanici=new JSONObject();

             boolean flag=true;
             boolean flag_tc=true;
            String  adsoyad = null;
            tc=su_tc.getText().toString();
            kulad=su_kulad.getText().toString();
            try {
                JSONArray jsonArray= new JSONArray(s);
                JSONObject jsonObject=new JSONObject();
                for(int i=0; i<jsonArray.length(); i++)
                {

                    jsonObject=jsonArray.getJSONObject(i);
                    if(jsonObject.getString("TC").equals(tc))
                    {
                        kullanici=jsonObject;
                        flag_tc=false;
                        if(jsonObject.getString("KUL_AD").equals(kulad))
                        {
                            if(jsonObject.getBoolean("DURUM"))
                            {

                                adsoyad = jsonObject.getString("KUL_ADSOYAD").toString();
                                flag=false;
                                break;
                            }
                            else
                                toast_mesaj("Sistemde kaydınız engellidir. Lütfen yetkili ile görüşünüz !");

                        }

                    }
                }
                if(flag_tc==true){
                    toast_mesaj("Sistemde bu TC kimlik numarasına ait kayıt bulunamadı !");
                }
                if(flag==false)
                    alterdialog("KONTROL",adsoyad+" "+"adlı kullanıcı siz misiniz ?");
                else if(flag=true && flag_tc==false)
                {
                    sayac--;
                    if(sayac<=0)
                    {
                        toast_mesaj("Doğrulama seçeneklerine 3 kez yanlış cevap verdiniz !"+"Kaydınız bloke edilmiştir. Lütfen Yetkili ile görüşünüz");
                        kullanici.put("DURUM",false);
                        VeriGuncelle veriGuncelle=new VeriGuncelle();
                        veriGuncelle.webservis("http://"+ip+":50491/api/Personel/Put/"+kullanici.getString("KUL_ID"),kullanici);
                        Intent ıntent = new Intent(Sifre_Yenileme.this, Giris.class);
                        startActivity(ıntent);
                    }
                    else {
                        toast_mesaj(sayac+" adet deneme hakkınız kalmıştır.");
                    }

                }




            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
