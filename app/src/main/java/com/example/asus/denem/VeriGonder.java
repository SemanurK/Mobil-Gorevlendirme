package com.example.asus.denem;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class VeriGonder
{
    private String url;
    private JSONObject jsonObject;
    public static JSONArray jsonArray;
    static String dosya1;
    static boolean flag=false;
    String kul_id;


    public String webservis(String url, JSONObject jsonObject) {
        this.url = url;
        this.jsonObject = jsonObject;
        new arkaplan().execute();
        return "";
    }


    class arkaplan extends AsyncTask<String, String, String> {
        protected String doInBackground(String... params) {

            HttpURLConnection connection = null;
            InputStream in = null;
            try {

                URL mUrl = new URL(url);
                connection = (HttpURLConnection) mUrl.openConnection();
                connection.setConnectTimeout(5 * 1000);
                connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                connection.setRequestProperty("Accept", "application/json");
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setUseCaches(false);
                // request body
                OutputStreamWriter streamWriter = new OutputStreamWriter(connection.getOutputStream());
                //DIŞ KAYNAĞA VERİ GÖNDERİRKEN OUTPUTSTREAMWRİTER KULLANILIR
                streamWriter.write(jsonObject.toString());
                streamWriter.flush();
                //getTypeFace Response
                int response = connection.getResponseCode();
                if (response == 200 || response==201 ) {
                    in = new BufferedInputStream(connection.getInputStream());
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));
                    String satır;
                    String dosya = "";
                    while ((satır = br.readLine()) != null) {
                        Log.d("satır :", satır);
                        dosya += satır;
                        dosya1=dosya;
                        flag=true;

                    }
                    return dosya;
                } else {
                    throw new Exception();
                }

            } catch (Exception e) {
                return String.format("%s Error for service call:%s", e.getMessage(), url);
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
            //return "hata";
        }

        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d("executedan gelen", s);
            try {
                JSONObject jsonObject1=new JSONObject();
                jsonArray=new JSONArray(s);


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}
