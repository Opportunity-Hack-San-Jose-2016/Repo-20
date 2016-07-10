package com.opportunityhack.teamhacks.helpbot_android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class NotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);


        Intent intent = new Intent(this, GCMRegistrationIntentService.class);
        startService(intent);
    }
}
