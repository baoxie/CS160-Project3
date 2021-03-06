package com.example.baoxie.tips;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class DrinksActivity extends WearableActivity implements View.OnClickListener {

    private ViewFlipper submenuFlipper;
    private Button prevButton;
    private Button nextButton;
    private Button mainButton;
    private Button helpButton;
    private ListView mainView;
    private List<String> drinkItems;
    private ArrayAdapter<String> listAdapter;

    private HashMap<String, String> drinksFromServer;
    private JSONArray responseJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submenu);

        Intent intent = getIntent();
        final String [] drinks = intent.getStringArrayExtra("drinks");
        drinksFromServer = (HashMap <String, String>) intent.getSerializableExtra("drinksFromServer");

        //submenuFlipper = (ViewFlipper) findViewById(R.id.submenuflipper);
        mainView = (ListView) findViewById((android.R.id.list));
        drinkItems = new ArrayList<String>();
        //prevButton = (Button) findViewById(R.id.prev_button);
        //nextButton = (Button) findViewById(R.id.next_button);
        mainButton = (Button) findViewById(R.id.main_button);
        helpButton = (Button) findViewById(R.id.help_button);
        listAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,drinkItems) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tV = (TextView) view.findViewById(android.R.id.text1);
                tV.setTextColor(Color.parseColor("#FFFFFF"));
                tV.setTextSize(28);
//                if (position % 2 == 1) {
//                    view.setBackgroundResource(R.drawable.selector1);
//                } else
//                {
//                    view.setBackgroundResource(R.drawable.selector2);
//                }
                return view;
            }
        };
        mainView.setAdapter(listAdapter);

        // Font path
        String fontPath = "fonts/proximanova_extrabold.ttf";
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);

        // Populate ViewFlipper with list of drink-recipes/cleaning/supplies
        for (String item : drinks) {
//            TextView tv = new TextView(DrinksActivity.this);
//            tv.setText(item);
//            tv.setTypeface(tf);
//            tv.setTextSize(25);
//            if (item.length() > 9) {
//                tv.setPadding(11, 10, 0, 0);
//            } else {
//                tv.setPadding(31, 10, 0, 0);
//            }
//            submenuFlipper.addView(tv,
//                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                            ViewGroup.LayoutParams.MATCH_PARENT));
            drinkItems.add(item);

        }
        listAdapter.notifyDataSetChanged();
//        }
//
//        prevButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                submenuFlipper.setInAnimation(inFromLeftAnimation());
//                submenuFlipper.setOutAnimation(outToRightAnimation());
//                submenuFlipper.showPrevious();
//            }
//        });

//        nextButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                submenuFlipper.setInAnimation(inFromRightAnimation());
//                submenuFlipper.setOutAnimation(outToLeftAnimation());
//                submenuFlipper.showNext();
//            }
//        });

        mainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DrinksActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DrinksActivity.this, QuickHelpActivity.class);
                startActivity(intent);
            }
        });
        mainView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected = (String) mainView.getItemAtPosition(position);
                Log.d("Sdf*&*&*&*&*&*&&*&*&*&", selected);
                Log.d("Sdf*&*&*&*&*&*&&*&*&*&",drinksFromServer.keySet().toString());
                if (drinksFromServer.containsKey(selected)) {
                    Log.d("asdadasd", "are you in here?");
                    String[] steps = drinksFromServer.get(selected).split(",");
                    Intent intent = new Intent(DrinksActivity.this, RecipeActivity.class);
                    intent.putExtra("steps", steps);
                    startActivity(intent);
                }
//                if (selected.equals("   Mocha  ")) {
//                    //String[] steps = {"whip milk", "whip milk", "pour coffee", "mix in milk"};
//                    // steps for testing
//                    String[] steps = {"stir milk", "pour stuff", "whip it!", "mix that", "steam that"};
//                    Intent intent = new Intent(DrinksActivity.this, RecipeActivity.class);
//                    intent.putExtra("steps", steps);
//                    startActivity(intent);
//                }
                if(selected.equals("Espresso")){

                    Thread thread = new Thread() {
                        @Override
                        public void run() {
                            try {
                                String request = "http://107.170.212.70/drinks.json";
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
                                responseJson = new JSONArray(response.toString());

                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.i("SERVER", "ERROR 2");
                            }
                        }
                    };
                    thread.start();
                    try {
                        thread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    for (int i = 0; i < responseJson.length(); i++) {
                        JSONObject currDrink = null;
                        try {
                            currDrink = responseJson.getJSONObject(i);
                            drinksFromServer.put(currDrink.getString("name"), currDrink.getString("instructions"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    Log.d("fdsfsdfs", drinksFromServer.keySet().toString());
                    Set<String> keys = drinksFromServer.keySet();
                    for(String i: keys) {
                        Log.d("Sdfsdfsdf", drinksFromServer.get(i));
                    }


                    //String[] drinks = {"   Mocha  ", " Americano", "Cappuccino"};
                    String[] drinks = drinksFromServer.keySet().toArray(new String[drinksFromServer.keySet().size()]);
                    Intent intent = new Intent(DrinksActivity.this, DrinksActivity.class);
                    intent.putExtra("drinks", drinks);
                    intent.putExtra("drinksFromServer", drinksFromServer);
                    startActivity(intent);
                }
            }
        });
        //submenuFlipper.setOnClickListener(this);
    }

    public void onClick(View v) {
        //TextView tv = (TextView) submenuFlipper.getCurrentView();
        //String selectedDrink = tv.getText().toString();
        //String selectedDrink =(String)mainView.getItemAtPosition(position);
        // hardcoding for now ; get recipe for respective drink
//        if (selectedDrink.equals("Cappuccino")) { // probably want convert cases here (so text is case insensitive)
//
//            String[] steps = {
//                    "steam milk",
//                    "whip milk",
//                    "pour coffee",
//                    "mix in milk"
//            };
//            Intent intent = new Intent(DrinksActivity.this, RecipeActivity.class);
//            intent.putExtra("steps", steps);
//            startActivity(intent);
//            // get http for list of drinks + new intent for submenu
//        }
//        Toast.makeText(getApplicationContext(), selectedDrink,
//                Toast.LENGTH_LONG).show();
    }

    // animation effect: http://www.inter-fuser.com/2009/07/android-transistions-slide-in-and-slide.html
    private Animation inFromRightAnimation() {
        Animation inFromRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, +1.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromRight.setDuration(500);
        inFromRight.setInterpolator(new AccelerateInterpolator());
        return inFromRight;
    }

    private Animation outToLeftAnimation() {
        Animation outtoLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, -1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
        outtoLeft.setDuration(500);
        outtoLeft.setInterpolator(new AccelerateInterpolator());
        return outtoLeft;
    }

    private Animation inFromLeftAnimation() {
        Animation inFromLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, -1.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromLeft.setDuration(500);
        inFromLeft.setInterpolator(new AccelerateInterpolator());
        return inFromLeft;
    }

    private Animation outToRightAnimation(){
        Animation outtoRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, +1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
        outtoRight.setDuration(500);
        outtoRight.setInterpolator(new AccelerateInterpolator());
        return outtoRight;
    }
}

