package com.example.asus.denem;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
Button btn;
TextView tx;
Bundle gelen=getIntent().getExtras();
 String al_ad,al_soyad,al_gorev,al_unvan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        al_ad=gelen.getString("adı");
        al_soyad=gelen.getString("soyadı");
        al_gorev=gelen.getString("gorev");
        al_unvan=gelen.getString("unvan");

        btn=(Button)findViewById(R.id.button);
        tx=(TextView)findViewById(R.id.textView);
        tx.setText(al_unvan+" "+al_ad +" " +al_soyad+" kişisine ait görevler burada listelenecektir ." );
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new arkaPlan().execute("http://10.61.207.100:50491/api/Personel");

            }
        });
    }
    class arkaPlan extends AsyncTask<String ,String ,String> {
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader br = null;
            try {
                URL url = new URL(params[0]);
              //  URL url = new URL(params[0]);
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
        protected void onPostExecute(String s)
        {
            Log.d("execute dan geleen",s);
            tx.setText(s);
        }
    }
}
