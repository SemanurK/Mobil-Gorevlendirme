package com.example.asus.denem;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.SpinnerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class Rol_Class extends AsyncTask<String,String,String> {
   static List<RolModel> roller;



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
    static JSONArray jsonArray1;

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        //flag_rol=true;
        Log.d("executadan gelen", s);
        JSONObject jsonObject = new JSONObject();
        try {
            JSONArray jsonArray = new JSONArray(s);
            jsonArray1=new JSONArray(s);
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                roller.add(new RolModel(jsonObject.getInt("ROL_ID"),jsonObject.getString("ROL_ADI")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
