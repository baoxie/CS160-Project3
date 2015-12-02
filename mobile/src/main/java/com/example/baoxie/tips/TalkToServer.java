package com.example.baoxie.tips;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by samue on 11/30/2015.
 */
public class TalkToServer extends AsyncTask<String, Void, String> {

    // First arg is CRUD METHOD
    // Second arg is Object Name
    // Third arg, and rest, are object parameters if needed
    protected String doInBackground(String... args) {
        if (args[0] == "GET") {
            try {
                String request = "http://107.170.212.70/" + args[1] + ".json";
                URL url = new URL(request);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                Log.i("GET RESULT", response.toString());
                return "OK";
            } catch (IOException e) {
                e.printStackTrace();
                Log.i("SERVER", "ERROR 2");
                return null;
            }
        }

        else if (args[0] == "POST") {
            JSONObject currFood = new JSONObject();
            try {
                currFood.put("name", args[2]);
                currFood.put("icon", "lol");
                currFood.put("instructions", "lol");
                currFood.put("images", "lol");
            } catch (JSONException e) {
                Log.i("SERVER", "ERROR 1");
                e.printStackTrace();
            }

            try {
                String request = "http://107.170.212.70/" + args[1];
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
        return null;
    }

    protected void onPostExecute(String params) {

    }
}