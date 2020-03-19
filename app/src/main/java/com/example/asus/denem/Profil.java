package com.example.asus.denem;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

public class Profil extends AppCompatActivity {
    Bundle gelen;
    EditText ad,kulad,cep,mail,adres,cevap,sifre;
    Button guncelle;
    Spinner spinner_soru;
    String ip="172.31.129.130";
    ArrayAdapter<String> adapter_cinsiyet,adapter_soru;
    String[] sorular=new String[]{"Güvenlik sorusu seçiniz","İlk okul öğretmeniniz ?","İlk hayvanınızın adı ?","En sevdiğiniz renk ?","En sevdiğiniz yemek ?","En sevdiğiniz araba markası ?"};
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
        setContentView(R.layout.activity_profil);
        gelen=getIntent().getExtras();
        String kul_bilgi=gelen.getString("kul_bilgi");

        ad=(EditText)findViewById(R.id.p_adsoyad);
        guncelle=(Button)findViewById(R.id.p_guncelle);
        sifre=(EditText)findViewById(R.id.p_sifre);
        kulad=(EditText)findViewById(R.id.p_kul_ad);
        cep=(EditText)findViewById(R.id.p_cep);
        mail=(EditText)findViewById(R.id.p_mail);
        cevap=(EditText)findViewById(R.id.p_cevap);
        spinner_soru=(Spinner)findViewById(R.id.p_soru);
        adapter_soru=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sorular);
        spinner_soru.setAdapter(adapter_soru);
        try {
            final JSONObject jsonObject=new JSONObject(kul_bilgi);
            ad.setText(jsonObject.getString("KUL_ADSOYAD1"));
            kulad.setText(jsonObject.getString("KUL_AD1"));
            cep.setText(jsonObject.getString("CEP1"));
            sifre.setText(jsonObject.getString("SIFRE1"));
            mail.setText(jsonObject.getString("MAİL1"));
            cevap.setText(jsonObject.getString("CEVAP1"));
           if(jsonObject.getString("SORU1").equals(""))
            {
                spinner_soru.setSelection(0);
            }
           else {
               for(int i=1; i<sorular.length; i++)
               {
if(sorular[i].equals(jsonObject.getString("SORU1").toString()))
{
    spinner_soru.setSelection(i);

}
               }
           }
           guncelle.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   JSONObject jsonObject1=new JSONObject();
                   if(validatePassword()) {

                       try {
                           jsonObject1.put("KUL_ADSOYAD", ad.getText());
                           jsonObject1.put("KUL_AD", kulad.getText());
                           jsonObject1.put("CEP", cep.getText());
                           jsonObject1.put("SİFRE", sifre.getText());
                           jsonObject1.put("MAİL", mail.getText());
                           jsonObject1.put("SORU", spinner_soru.getSelectedItem());
                           jsonObject1.put("CEVAP", cevap.getText());
                           jsonObject1.put("TC", jsonObject.getString("TC1"));
                           jsonObject1.put("CINSIYET", jsonObject.getBoolean("CINSIYET1"));
                           jsonObject1.put("BOLUM_ID", jsonObject.getInt("BOLUM_ID1"));
                           VeriGuncelle veriGuncelle = new VeriGuncelle();
                           veriGuncelle.webservis("http://" + ip + ":50491/api/Personel/Put/" + jsonObject.getString("KUL_ID1"), jsonObject1);

                           android.app.AlertDialog.Builder builder1 = new android.app.AlertDialog.Builder(Profil.this);
                           builder1.setTitle("ONAY");
                           builder1.setMessage("Bilgileriniz güncellenmiştir. Değişiklikleri görebilmek için tekrar giriş yapınız !");
                           builder1.setNeutralButton("KAPAT", new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialog, int which) {
                                   Intent intent = new Intent(Profil.this, Giris.class);
                                   startActivity(intent);

                               }
                           });
                           builder1.show();


                       } catch (JSONException e) {
                           e.printStackTrace();
                       }
                   }else
                       toast_mesaj("Şifre en az 1 küçük harf,1 büyük harf , 1 özel karakter ,1 sayı ve en az 6 karakterden olmak zorundadır.");
                /*   jsonObject1.put("KUL_ADSOYAD1")
                   VeriGuncelle veriGuncelle=new VeriGuncelle();
                   veriGuncelle.webservis(url_gorulme_tarihi,jsonObject);*/
               }
           });




        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void toast_mesaj(String mesaj)
    {

        LayoutInflater inflater;
        View layout;
        inflater = getLayoutInflater();
        layout = inflater.inflate(R.layout.toast_layout,
                (ViewGroup) findViewById(R.id.custom_toast_container));
     TextView text = layout.findViewById(R.id.text);
        text.setText(mesaj);
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
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
}
