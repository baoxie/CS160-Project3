package com.example.baoxie.tips;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class StaticDrinksActivity extends Activity {
    /**
     * Called when the activity is first created.
     */


    private ListView mainMenu;
    /* Main Menu options (as of now): Drinks, Cleaning */
    private ArrayList<String> mainOptions = new ArrayList<>();
    private ArrayAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staticdrinks);

        Button button = (Button) findViewById(R.id.drinks_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StaticDrinksActivity.this, StaticAddRecipeActivity.class);
                startActivity(intent);
            }
        });
    }
}