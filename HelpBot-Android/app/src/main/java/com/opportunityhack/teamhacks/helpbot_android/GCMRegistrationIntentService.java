package com.opportunityhack.teamhacks.helpbot_android;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

/**
 * Created by hjagtap on 7/9/16.
 */
public class GCMRegistrationIntentService extends IntentService {

    public static final String REGISTRATION_SUCCESS = "registration_success";
    public static final String REGISTRATION_FAILED = "registration_failed";

    public GCMRegistrationIntentService() {
        super("");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        InstanceID instanceID = InstanceID.getInstance(getApplicationContext());
        Intent registrationDone = null;

        try {
            String registrationToken = instanceID.getToken(getString(R.string.gcm_sender_id), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Log.i("token:", registrationToken);
            registrationDone = new Intent(REGISTRATION_SUCCESS);
            registrationDone.putExtra("registrationToken", registrationToken);
        } catch (IOException e) {
            e.printStackTrace();
            registrationDone = new Intent(REGISTRATION_FAILED);
        }

        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationDone);
    }
}
