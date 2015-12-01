package com.example.baoxie.tips;

import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.nio.charset.StandardCharsets;

public class MobileListenerService extends WearableListenerService {
    private static final String TAG = "MobileListenerService";
    private static final String RESTOCK = "/restock";

    public void restockNotify(String message){
        Intent viewIntent = new Intent(this, MainActivity.class);
        PendingIntent viewPendingIntent = PendingIntent.getActivity(this, 0, viewIntent, 0);
        viewIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.sym_def_app_icon)
                .setContentTitle("Restock Requested")
                .setContentText(message)
                .setContentIntent(viewPendingIntent);


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        int notificationId = 001;
        notificationManager.notify(notificationId, notificationBuilder.build());
    }


    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.i(TAG, "Message to change the picture...");
        Toast.makeText(getApplicationContext(), "Received notification from watch; you need to restock [product].",
                Toast.LENGTH_LONG).show();
        if( messageEvent.getPath().equalsIgnoreCase( RESTOCK ) ) {
            String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            restockNotify(value);
        }else{
            super.onMessageReceived(messageEvent);
        }
    }

}