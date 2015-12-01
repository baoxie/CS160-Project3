package com.example.baoxie.tips;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

public class SupplyOptionsActivity extends WearableActivity {
    private Button restockButton;
    private Button locateButton;
    private GoogleApiClient mApiClient;
    private static final String RESTOCK = "/restock";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supply_options);

        restockButton = (Button) findViewById(R.id.restock_button);
        locateButton = (Button) findViewById(R.id.locate_button);

        mApiClient = new GoogleApiClient.Builder( this )
                .addApi( Wearable.API )
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle connectionHint) {
                        /* Successfully connected */
                    }

                    @Override
                    public void onConnectionSuspended(int cause) {
                        /* Connection was interrupted */
                    }
                })
                .build();
        mApiClient.connect();

        restockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Product will be restocked.",
                        Toast.LENGTH_LONG).show();
                sendRestockMessage();
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

    public void sendRestockMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String temp = "Product needs to be restocked.";
                NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(mApiClient).await();
                for (Node node : nodes.getNodes()) {
                    MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(
                            mApiClient, node.getId(), RESTOCK, temp.getBytes()).await();
                }
            }
        }).start();
    }
}