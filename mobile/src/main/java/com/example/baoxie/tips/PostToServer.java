package com.example.baoxie.tips;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by samue on 11/30/2015.
 */
public class PostToServer extends AsyncTask<String, Void, String> {

    protected String doInBackground(String... item) {
        JSONObject currFood = new JSONObject();
        try {
            currFood.put("name", "Peanut Butter");
            currFood.put("icon", "lol");
            currFood.put("instructions", "lol");
            currFood.put("images", "lol");
        } catch (JSONException e) {
            Log.i("SERVER", "ERROR 1");
            e.printStackTrace();
        }

        try {
            String request = "http://107.170.212.70/foods";
            URL url = new URL(request);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.connect();

            OutputStream os = conn.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
            osw.write(currFood.toString());
            osw.flush();
            osw.close();

            Log.i("SERVER", conn.getResponseMessage());
            return "OK";
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("SERVER", "ERROR 2");
            return null;
        }
    }

    protected void onPostExecute(String params) {

    }
}