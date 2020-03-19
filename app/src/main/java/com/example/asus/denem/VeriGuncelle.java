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
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class VeriGuncelle {
    private String url;
    private JSONObject jsonObject;
    private JSONArray jsonArray;



    public String webservis(String url, JSONObject jsonObject) {
        this.url = url;
        this.jsonObject = jsonObject;
        new arkaplan().execute();
        return "";
    }
    class arkaplan extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
                InputStream in = null;
                try {

                URL mUrl = new URL(url);
                connection = (HttpURLConnection) mUrl.openConnection();
                connection.setConnectTimeout(5 * 1000);
                connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestMethod("PUT");
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setUseCaches(false);
                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(jsonObject.toString().getBytes());


                int response = connection.getResponseCode();
                if (response == 200) {

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
