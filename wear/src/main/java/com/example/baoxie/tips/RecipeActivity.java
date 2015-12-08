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
import android.os.Debug;
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

public class RecipeActivity extends WearableActivity implements
        SensorEventListener {

    private static final String DEBUG_TAG = "Gestures";

    private ViewFlipper recipeFlipper;
    private Button prevButton;
    private Button nextButton;
    private Button mainButton;
    private Button helpButton;

    private SensorManager mSensorManager;
    private Sensor mSensor;
    double xValue = 0;
    double yValue = 9.77622;
    double zValue = 0.813417;

    private long lastTime;

    private static final int LAST_SHAKE_TIME = 1000;

    static TextView mDotsText[];
    private int mDotsCount;
    private LinearLayout mDotsLayout;
    private int mDotsCurr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        Intent intent = getIntent();
        //hardcoded for now
        String [] steps = intent.getStringArrayExtra("steps");
//        have toast to say like lowfat milk for 'customizable drinks'

        recipeFlipper = (ViewFlipper) findViewById(R.id.submenuflipper);
        prevButton = (Button) findViewById(R.id.prev_button);
        nextButton = (Button) findViewById(R.id.next_button);
        mainButton = (Button) findViewById(R.id.main_button);
        helpButton = (Button) findViewById(R.id.help_button);

        // Font path
        String fontPath = "fonts/proximanova_extrabold.ttf";
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);

        // Populate ViewFlipper with list of drink-recipes/cleaning/supplies
        for (String item : steps) {
            LinearLayout lv = new LinearLayout(RecipeActivity.this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            lv.setOrientation(LinearLayout.VERTICAL);
            TextView tv = new TextView(RecipeActivity.this);
            AnimatedGifView iv;
            //AnimatedGifView iv = new AnimatedGifView(RecipeActivity.this);
            // steam milk", "whip milk", "pour coffee", "mix in milk"
            if (item.equals("steam milk")) {
                iv = new AnimatedGifView(RecipeActivity.this, "steam");
            } else if (item.equals("whip milk")) {
                iv = new AnimatedGifView(RecipeActivity.this, "stir");
            } else if (item.equals("pour coffee")) {
                iv = new AnimatedGifView(RecipeActivity.this, "pour");
            } else {
                iv = new AnimatedGifView(RecipeActivity.this, "stir");
            }
            tv.setText(item);
            tv.setTypeface(tf);
            tv.setTextColor(Color.parseColor("#FFFFFF"));
            tv.setTextSize(25);
            if (item.length() > 9) {
                tv.setPadding(11, 10, 0, 0);
                iv.setPadding(50, 10, 0,0);
            } else {
                tv.setPadding(31, 10, 0, 0);
                iv.setPadding(50,10,0,0);
            }

            tv.setLayoutParams(params);
            iv.setLayoutParams(params);
            lv.addView(tv);
            lv.addView(iv);
            recipeFlipper.addView(lv,
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT));
        }

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recipeFlipper.setInAnimation(inFromLeftAnimation());
                recipeFlipper.setOutAnimation(outToRightAnimation());
                recipeFlipper.showPrevious();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recipeFlipper.setInAnimation(inFromRightAnimation());
                recipeFlipper.setOutAnimation(outToLeftAnimation());
                recipeFlipper.showNext();
            }
        });

        mainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecipeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecipeActivity.this, QuickHelpActivity.class);
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

        if (mDotsCurr == 0) {
            Toast.makeText(getApplicationContext(), "Low Fat Milk",
                    Toast.LENGTH_SHORT).show();
        }

                       /* Initialize the sensor. */
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);

        lastTime = System.currentTimeMillis();
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
        if (mDotsCurr == 0) {
            Toast.makeText(getApplicationContext(), "Low Fat Milk",
                    Toast.LENGTH_SHORT).show();
        }
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
        if (mDotsCurr == 0) {
            Toast.makeText(getApplicationContext(), "Low Fat Milk",
                    Toast.LENGTH_LONG).show();
        }
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

//    http://stackoverflow.com/questions/2317428/android-i-want-to-shake-it
    @Override
    public void onSensorChanged(SensorEvent event) {

        long now = System.currentTimeMillis();
        long diff = now - lastTime;
        double newX = event.values[0];
        double newY = event.values[1];
        double newZ = event.values[2];
        double deltaX = Math.abs(newX - xValue);
        double deltaY = Math.abs(newY - yValue);
        double deltaZ = Math.abs(newZ - zValue);
        //Log.d("String", "x:" + xValue + "; y:" + yValue + "; z:" + zValue);
//        if (deltaX >= 4.0 && deltaY >= 3 && deltaZ >= 1.7 ) {
//            Toast.makeText(getApplicationContext(), "you are shaking the watch!!!",
//                    Toast.LENGTH_LONG).show();
//            //actually send the message to the watch
//        }

        if ( diff >= LAST_SHAKE_TIME && deltaZ >= 5 && deltaX <= 2.2 && deltaY <= 2.2) {
            Log.i(DEBUG_TAG, "Z plane");
            Log.i("String", "x:" + xValue + "; y:" + yValue + "; z:" + zValue);
            recipeFlipper.setInAnimation(inFromLeftAnimation());
            recipeFlipper.setOutAnimation(outToRightAnimation());
            recipeFlipper.showPrevious();
            resetShakeParameters();

        }

        if ( deltaY >= 4.8  && deltaX <= 2.1 && deltaZ <= 2.1) {
            Log.i(DEBUG_TAG, "Y plane");
            Log.i("String", "x:" + xValue + "; y:" + yValue + "; z:" + zValue);
            recipeFlipper.setInAnimation(inFromRightAnimation());
            recipeFlipper.setOutAnimation(outToLeftAnimation());
            recipeFlipper.showNext();
            resetShakeParameters();

        }
        xValue = newX;
        yValue = newY;
        zValue = newZ;
    }

    private void resetShakeParameters() {
        lastTime = System.currentTimeMillis();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(this);
        super.onPause();
    }
}
