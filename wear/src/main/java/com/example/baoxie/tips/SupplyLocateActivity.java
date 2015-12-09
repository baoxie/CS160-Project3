package com.example.baoxie.tips;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class SupplyLocateActivity extends WearableActivity {

    private static final String DEBUG_TAG = "Gestures";

    private ViewFlipper locateFlipper;
    private Button prevButton;
    private Button nextButton;
    private Button mainButton;

    static TextView mDotsText[];
    private int mDotsCount;
    private LinearLayout mDotsLayout;
    private int mDotsCurr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supply_locate);

        Intent intent = getIntent();
        //hardcoded for now
//        String [] steps = intent.getStringArrayExtra("steps");
        String [] steps = {
                "Go to stock room",
                "Section A",
                "Shelf 4",
                "Center"
        };

        locateFlipper = (ViewFlipper) findViewById(R.id.locateflipper);
        prevButton = (Button) findViewById(R.id.prev_button);
        nextButton = (Button) findViewById(R.id.next_button);
        mainButton = (Button) findViewById(R.id.main_button);

        // Font path
        String fontPath = "fonts/proximanova_extrabold.ttf";
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);

        // Populate ViewFlipper with list of drink-recipes/cleaning/supplies
        for (String item : steps) {
            TextView tv = new TextView(SupplyLocateActivity.this);
            tv.setText(item);
            tv.setTypeface(tf);
            tv.setTextSize(25);
            tv.setTextColor(Color.parseColor("#FFFFFF"));
            if (item.length() > 9) {
                tv.setPadding(11, 10, 0, 0);
            } else {
                tv.setPadding(31, 10, 0, 0);
            }
            locateFlipper.addView(tv,
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT));
        }

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locateFlipper.setInAnimation(inFromLeftAnimation());
                locateFlipper.setOutAnimation(outToRightAnimation());
                locateFlipper.showPrevious();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locateFlipper.setInAnimation(inFromRightAnimation());
                locateFlipper.setOutAnimation(outToLeftAnimation());
                locateFlipper.showNext();
            }
        });

        mainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SupplyLocateActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        mDotsLayout = (LinearLayout)findViewById(R.id.image_count);

        mDotsCount = steps.length;
        mDotsCurr = 0;
        mDotsText = new TextView[mDotsCount];

        //here we set the dots
        for (int i = 0; i < mDotsCount; i++) {
            mDotsText[i] = new TextView(this);
            mDotsText[i].setText(".");
            mDotsText[i].setTextSize(50);
            mDotsText[i].setTypeface(null, Typeface.BOLD);
            mDotsText[i].setTextColor(android.graphics.Color.GRAY);
            //Log.i("dots", "creating dot scrolling");
            mDotsLayout.addView(mDotsText[i]);
//            mDotsLayout.getChildAt(i).setVisibility(View.VISIBLE);
        }

        for (int i = 0; i < mDotsCount; i++) {
            mDotsText[i]
                    .setTextColor(Color.GRAY);
            //Log.i("dots", "coloring dots grey");
        }

        mDotsText[mDotsCurr]
                .setTextColor(Color.WHITE);
        //Log.i("dots", "coloring dots white");

    }

    // animation effect: http://www.inter-fuser.com/2009/07/android-transistions-slide-in-and-slide.html
    private Animation inFromRightAnimation() {
        Animation inFromRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, +1.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromRight.setDuration(500);
        inFromRight.setInterpolator(new AccelerateInterpolator());
        if (mDotsCurr == mDotsCount - 1) {
            mDotsCurr = 0;
        } else {
            mDotsCurr++;
        }
        for (int i = 0; i < mDotsCount; i++) {
            mDotsText[i]
                    .setTextColor(Color.GRAY);
            //Log.i("dots", "coloring dots grey");
        }

        mDotsText[mDotsCurr]
                .setTextColor(Color.WHITE);
        //Log.i("dots", "coloring dots white");
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
        if (mDotsCurr == 0) {
            mDotsCurr = mDotsCount - 1;
        } else {
            mDotsCurr--;
        }
        for (int i = 0; i < mDotsCount; i++) {
            mDotsText[i]
                    .setTextColor(Color.GRAY);
            //Log.i("dots", "coloring dots grey");
        }

        mDotsText[mDotsCurr]
                .setTextColor(Color.WHITE);
        //Log.i("dots", "coloring dots white");
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
