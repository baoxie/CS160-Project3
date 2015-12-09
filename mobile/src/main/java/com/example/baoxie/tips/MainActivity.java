package com.example.baoxie.tips;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */


    private ListView mainMenu;
    /* Main Menu options (as of now): Drinks, Cleaning */
    private List<String> mainOptions;
    private ArrayAdapter adapter;
    private static JSONArray drink_response;
    private static JSONArray food_response;
    private static JSONArray supply_response;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainMenu = (ListView)findViewById(R.id.main_menu_list);
        mainOptions = new ArrayList<String>();
        executeRequest();
        while(supply_response==null){
            //wait
        }
        mainOptions.add("Drinks");
        mainOptions.add("Food");
        mainOptions.add("Supplies");
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,mainOptions);
        mainMenu.setAdapter(adapter);

        mainMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected = (String) mainMenu.getItemAtPosition(position);
                if(selected.equals("Drinks")){
                    String[][] data = parseJSONResponse(drink_response);
                    launchActivity(data,"drinks");
                }
                if(selected.equals("Food")){
                    String[][] data = parseJSONResponse(food_response);
                    launchActivity(data,"food");
                }
                if(selected.equals("Supplies")){
                    String[][] data = parseJSONResponse(supply_response);
                    launchActivity(data,"supplies");
                }

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        executeRequest();
        while(supply_response==null){
            //do nothing
        }
    }

    // req is either GET or POST
    public void executeRequest(){
        new Thread(new Runnable() {
            @Override
            public void run() {
               drink_response = getServerData("drinks");
                food_response = getServerData("foods");
                supply_response = getServerData("cleanings");

            }
        }).start();
    }

    public JSONArray getServerData(final String resource){
        try {
            String request = "http://107.170.212.70/" + resource + ".json";
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
            JSONArray result = new JSONArray(response.toString());
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("SERVER", "ERROR 2");
            return null;
        }
    }

    public String[][] parseJSONResponse(JSONArray server_response) {
        String[][] result = new String[server_response.length()][4];
        try {
            for (int i = 0; i < server_response.length(); i++) {
                result[i][0] = (String) ((JSONObject) server_response.get(i)).get("name");
                result[i][1] = (String) ((JSONObject) server_response.get(i)).get("instructions");
                result[i][2] = (String) ((JSONObject) server_response.get(i)).get("icon");
                result[i][3] = (String) ((JSONObject) server_response.get(i)).get("images");
            }
        } catch (Exception e) {
            Log.d("DRINKS",e.toString());
            Log.i("SERVER", "ERROR 2");
        }
        return result;
    }

    public void launchActivity(String[][] newData,String indicator){
        Intent in = new Intent(MainActivity.this,StaticDrinksActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putSerializable("items",newData);
        in.putExtras(mBundle);
        in.putExtra("type",indicator);
        startActivity(in);
    }





}
