package com.example.baoxie.tips;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SupplyOptionsActivity extends WearableActivity {
    private Button restockButton;
    private Button locateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supply_options);

        restockButton = (Button) findViewById(R.id.restock_button);
        locateButton = (Button) findViewById(R.id.locate_button);

        restockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Product will be restocked.",
                        Toast.LENGTH_LONG).show();
            }
        });

        locateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Start new intent to locate product.",
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}
