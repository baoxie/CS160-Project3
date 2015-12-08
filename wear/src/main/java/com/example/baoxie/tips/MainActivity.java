package com.example.baoxie.tips;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class MainActivity extends WearableActivity implements View.OnClickListener {
    private ViewFlipper mainMenuFlipper;
    private Button prevButton;
    private Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainMenuFlipper = (ViewFlipper) findViewById(R.id.mainmenuflipper);

        // Font path
        String fontPath = "fonts/proximanova_extrabold.ttf";
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);

        TextView m1 = (TextView) findViewById(R.id.main_menu_view1);
        TextView m2 = (TextView) findViewById(R.id.main_menu_view2);
        TextView m3 = (TextView) findViewById(R.id.main_menu_view3);
        m1.setTypeface(tf);
        m2.setTypeface(tf);
        m3.setTypeface(tf);

        mainMenuFlipper = (ViewFlipper) findViewById(R.id.mainmenuflipper);
        prevButton = (Button) findViewById(R.id.prev_button);
        nextButton = (Button) findViewById(R.id.next_button);

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainMenuFlipper.setInAnimation(inFromLeftAnimation());
                mainMenuFlipper.setOutAnimation(outToRightAnimation());
                mainMenuFlipper.showPrevious();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainMenuFlipper.setInAnimation(inFromRightAnimation());
                mainMenuFlipper.setOutAnimation(outToLeftAnimation());
                mainMenuFlipper.showNext();
            }
        });

        //setAmbientEnabled();

        mainMenuFlipper.setOnClickListener(this);

    }

    public void onClick(View v) {
        TextView tv = (TextView) mainMenuFlipper.getCurrentView();
        String selected = tv.getText().toString();
        // hardcoding for now
        if (selected.equals("drinks")) {
            String[] drinks = {
                    "Espresso",
                    "Coffee",
                    "Tea",
            };
            Intent intent = new Intent(MainActivity.this, DrinksActivity.class);
            intent.putExtra("drinks", drinks);
            startActivity(intent);
            // get http for list of drinks + new intent for submenu
        } else if (selected.equals("cleaning")) {
            // get http for list of cleaning + new intent for submenu
        } else if (selected.equals("supplies")) {
            String[] supplies = {
                    "Cups",
                    "Coffee Beans",
                    "Straws",
            };
            Intent intent = new Intent(MainActivity.this, SuppliesActivity.class);
            intent.putExtra("supplies", supplies);
            startActivity(intent);
            // get http for list of cleaning + new intent for submenu
        }
        //Toast.makeText(getApplicationContext(), selected,
        //        Toast.LENGTH_LONG).show();
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



//    implement if want the always on feature for app; may add/edit in later
//    @Override
//    public void onEnterAmbient(Bundle ambientDetails) {
//        super.onEnterAmbient(ambientDetails);
//        updateDisplay();
//    }
//
//    @Override
//    public void onUpdateAmbient() {
//        super.onUpdateAmbient();
//        updateDisplay();
//    }
//
//    @Override
//    public void onExitAmbient() {
//        updateDisplay();
//        super.onExitAmbient();
//    }
//
//    private void updateDisplay() {
////        if (isAmbient()) {
////            mContainerView.setBackgroundColor(getResources().getColor(android.R.color.black));
////            mTextView.setTextColor(getResources().getColor(android.R.color.white));
////            mClockView.setVisibility(View.VISIBLE);
////
////            mClockView.setText(AMBIENT_DATE_FORMAT.format(new Date()));
////        } else {
////            mContainerView.setBackground(null);
////            mTextView.setTextColor(getResources().getColor(android.R.color.black));
////            mClockView.setVisibility(View.GONE);
////        }
//    }
}
