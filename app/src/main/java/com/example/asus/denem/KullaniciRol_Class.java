package com.example.asus.denem;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class KullaniciRol_Class extends AsyncTask<String,String,String> {
     static String sonuc = null;
    static int rol_id;

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
                sonuc=dosya;


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
        try {
            JSONObject jsonObject=new JSONObject(s);
            rol_id=jsonObject.getInt("ROL_ID");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
