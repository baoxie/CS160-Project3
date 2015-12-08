package com.example.baoxie.tips;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.List;

public class SuppliesActivity extends WearableActivity implements View.OnClickListener {

    private ViewFlipper submenuFlipper;
    private Button prevButton;
    private Button nextButton;
    private Button mainButton;
    private Button helpButton;
    private ListView mainView;
    private List<String> supplyItems;
    private ArrayAdapter<String> listAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submenu);

        Intent intent = getIntent();
        String [] supplies = intent.getStringArrayExtra("supplies");

        //submenuFlipper = (ViewFlipper) findViewById(R.id.submenuflipper);
        //prevButton = (Button) findViewById(R.id.prev_button);
        //nextButton = (Button) findViewById(R.id.next_button);
        mainView = (ListView) findViewById((android.R.id.list));
        supplyItems = new ArrayList<String>();
        mainButton = (Button) findViewById(R.id.main_button);
        helpButton = (Button) findViewById(R.id.help_button);
        listAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,supplyItems);
        mainView.setAdapter(listAdapter);

        // Font path
        String fontPath = "fonts/proximanova_extrabold.ttf";
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);

        // Populate ViewFlipper with list of drink-recipes/cleaning/supplies
        for (String item : supplies) {
//            TextView tv = new TextView(SuppliesActivity.this);
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
            supplyItems.add(item);
        }
        listAdapter.notifyDataSetChanged();
//        }

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
                Intent intent = new Intent(SuppliesActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SuppliesActivity.this, QuickHelpActivity.class);
                startActivity(intent);
            }
        });
        //submenuFlipper.setOnClickListener(this);
        mainView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected = (String) mainView.getItemAtPosition(position);
                Intent intent = new Intent(SuppliesActivity.this, SupplyOptionsActivity.class);
                startActivity(intent);
            }

        });

    }

    public void onClick(View v) {
        //Intent intent = new Intent(SuppliesActivity.this, SupplyOptionsActivity.class);
        //startActivity(intent);
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

