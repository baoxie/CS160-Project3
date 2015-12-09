package com.example.baoxie.tips;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class StaticAddRecipeActivity extends Activity {
    /**
     * Called when the activity is first created.
     */


    private ListView instrMenu;
    private ArrayList<String> instructions;
    private CustomAdapter2 adapter;
    private EditText inputInstruction;
    private boolean newInst;
    private String[] drinkList;
    private String type;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addrecipe);
        instrMenu = (ListView)findViewById(R.id.recipe_menu_list);
        inputInstruction =(EditText)findViewById(R.id.editText2);

        Intent intent = getIntent();
        drinkList = intent.getStringArrayExtra("instructions");
        type = intent.getStringExtra("type");
        instructions = new ArrayList<String>();
        String temp = drinkList[1];
        newInst = true;

        if(!temp.equals("")) {
            newInst = false;
            String[] instr = drinkList[1].split(",");
            for (int i = 0; i < instr.length; i++) {
                instructions.add(instr[i]);
            }
        }

        adapter = new CustomAdapter2(instructions,this,drinkList);
        instrMenu.setAdapter(adapter);


        Button button = (Button) findViewById(R.id.recipe_button);
        Button saveBtn = (Button) findViewById(R.id.save_btn2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputValue = inputInstruction.getText().toString();
                if(!inputValue.equals("")) {
                    instructions.add(inputValue);
                    adapter.notifyDataSetChanged();
                    inputInstruction.setText("");
                }
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputValue = inputInstruction.getText().toString();
                if(!inputValue.equals("")) {
                    instructions.add(inputValue);
                    adapter.notifyDataSetChanged();
                    inputInstruction.setText("");
                }
                if(newInst){
                    Toast.makeText(getApplicationContext(), "New Recipe Saved", Toast.LENGTH_LONG).show();
                    addRecipe();
                }
            }
        });
    }


    public void addRecipe(){
        JSONObject currDrink = new JSONObject();
        try {
            currDrink.put("name",drinkList[0]);
            String temp = "";
            for(String s: instructions){
                if(s!=instructions.get(instructions.size()-1)) {
                    temp = temp + s + ",";
                }else{
                    temp += s;
                }
            }
            currDrink.put("icon","steam,pour,stir");
            currDrink.put("instructions",temp);
            currDrink.put("images", "Espresso");
            postServerData(currDrink);
        }catch(Exception e){
            Log.d("SERVER ERROR","sss");
        }
    }

    public void postServerData(JSONObject postData){
        final JSONObject finData = postData;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String request = getRequestURL();
                    URL url = new URL(request);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    conn.setUseCaches(false);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.connect();
                    Log.d("RECIPE",finData.toString());
                    OutputStream os = conn.getOutputStream();
                    OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
                    osw.write(finData.toString());
                    osw.flush();
                    osw.close();
                    Log.i("RECIPE", conn.getResponseMessage());
                    Log.d("RECIPE", "done pushing to server");
                } catch (Exception e) {
                    Log.d("RECIPE",e.toString());
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public String getRequestURL(){
        Log.d("RECIPE",type);
        if(type.equals("drinks")){
            return "http://107.170.212.70/drinks";
        }
        if(type.equals("food")){
            return "http://107.170.212.70/foods";
        }
        if(type.equals("supplies")){
            return "http://107.170.212.70/cleanings";
        }
        return null;
    }



}