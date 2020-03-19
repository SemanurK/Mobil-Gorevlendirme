package com.example.asus.denem;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class VeriSil {
    private String url;




    public void webservis(String url) {
        this.url = url;
         new arkaplansil().execute(url);

    }
    class arkaplansil extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            InputStream in = null;
            try {

                URL mUrl = new URL(params[0]);
                connection = (HttpURLConnection) mUrl.openConnection();
                connection.setConnectTimeout(5 * 1000);
                connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestMethod("DELETE");
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setUseCaches(false);
                //getTypeFace Response
                int response = connection.getResponseCode();
                if (response == 204) {
                    in = new BufferedInputStream(connection.getInputStream());
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));

                    String satır;
                    String dosya = "";
                    while ((satır = br.readLine()) != null) {
                        Log.d("satır :", satır);
                        dosya += satır;
                    }
                    return dosya;
                } else {
                    throw new Exception();
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

    }


}
