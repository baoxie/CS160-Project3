package com.example.baoxie.tips;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class QuickHelpActivity extends WearableActivity {

    private Button noButton;
    private Button yesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_help);

        // Font path
        String fontPath = "fonts/proximanova_extrabold.ttf";
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);

        TextView helpMsg = (TextView) findViewById(R.id.help_view);
        helpMsg.setTypeface(tf);
        noButton = (Button) findViewById(R.id.no_button);
        noButton.setTypeface(tf);
        noButton.setTextSize(25);
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuickHelpActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        yesButton = (Button) findViewById(R.id.yes_button);
        yesButton.setTypeface(tf);
        yesButton.setTextSize(25);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "currently asking manager for help!",
                    Toast.LENGTH_LONG).show();
                Intent intent = new Intent(QuickHelpActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

}