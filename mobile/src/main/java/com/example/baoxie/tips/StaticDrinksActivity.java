package com.example.baoxie.tips;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class StaticDrinksActivity extends Activity {
    /**
     * Called when the activity is first created.
     */


    private ListView drinkMenu;
    private ArrayList<String> drinks;
    private CustomAdapter adapter;
    private static String[][] drinkStuff;
    private static String type;
    private EditText inputDrink;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staticdrinks);
        drinkMenu = (ListView)findViewById(R.id.drink_menu_list);
        inputDrink = (EditText)findViewById(R.id.editText);

        Intent intent = getIntent();
        drinkStuff = (String[][])intent.getExtras().getSerializable("items");
        type = intent.getStringExtra("type");

        drinks = new ArrayList<String>();
        for(int i=0; i< drinkStuff.length; i++){
            drinks.add(drinkStuff[i][0]);
        }
        adapter = new CustomAdapter(drinks,this,drinkStuff,type);
        drinkMenu.setAdapter(adapter);


        Button button = (Button) findViewById(R.id.drinks_button);
        Button saveBtn = (Button) findViewById(R.id.save_btn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputValue = inputDrink.getText().toString();
                if(!inputValue.equals("")) {
                    drinks.add(inputValue);
                    adapter.notifyDataSetChanged();
                    inputDrink.setText("");
                }
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputValue = inputDrink.getText().toString();
                if(!inputValue.equals("")) {
                    drinks.add(inputValue);
                    adapter.notifyDataSetChanged();
                    inputDrink.setText("");
                }
                sendToast();
            }
        });
    }

    public void sendToast(){
        if(type.equals("drinks")) {
            Toast.makeText(getApplicationContext(), "Saved New Drink", Toast.LENGTH_LONG).show();
        }
        if(type.equals("food")){
            Toast.makeText(getApplicationContext(), "Saved New Meal", Toast.LENGTH_LONG).show();
        }
        if(type.equals("supplies")){
            Toast.makeText(getApplicationContext(), "Saved New Supply", Toast.LENGTH_LONG).show();
        }
    }



}