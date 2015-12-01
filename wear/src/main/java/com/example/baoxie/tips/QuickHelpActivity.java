package com.example.baoxie.tips;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class QuickHelpActivity extends WearableActivity {

    private Button mainButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_help);

        // Font path
        String fontPath = "fonts/proximanova_extrabold.ttf";
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);

        TextView helpMsg = (TextView) findViewById(R.id.help_view);
        helpMsg.setTypeface(tf);
        mainButton = (Button) findViewById(R.id.main_button);
        mainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuickHelpActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

}