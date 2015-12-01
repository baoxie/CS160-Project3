package com.example.baoxie.tips;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */


    private ListView mainMenu;
    /* Main Menu options (as of now): Drinks, Cleaning */
    private ArrayList<String> mainOptions = new ArrayList<>();
    private ArrayAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // coded dynamic list just in case we add more options in future
        // can change it later if we stick to only two categories
        mainOptions.add("Drinks");
        mainOptions.add("Cleaning");
        mainMenu = (ListView) findViewById(R.id.main_menu_list);

        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, mainOptions) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tV = (TextView) view.findViewById(android.R.id.text1);
                // edit listView design/colors later!
                tV.setHeight(300);
                tV.setTextSize(40);
                tV.setTextColor(Color.parseColor("#FFFFFF"));
                if (position % 2 == 1) {
                    view.setBackgroundResource(R.drawable.selector1);
                } else
                {
                    view.setBackgroundResource(R.drawable.selector2);
                }
                return view;
            }
        };
        mainMenu.setAdapter(adapter);

        // SAM'S TEST CODE TO CREATE AN OBJECT
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//            new PostToServer().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//        } else {
//            new PostToServer().execute();
//        }
    }
}
