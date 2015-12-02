package com.example.baoxie.tips;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class StaticAddRecipeActivity extends Activity {
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
        setContentView(R.layout.activity_addrecipe);
    }
}