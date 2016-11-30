package com.mss.msschat.NotificationUtils;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.mss.msschat.AppUtils.AppPreferences;
import com.mss.msschat.AppUtils.Constants;
import com.mss.msschat.R;

/**
 * Created by ${Gaurav} on 25/8/16.
 */

public class RegistrationIntentService extends IntentService {

    AppPreferences mSession;

    // abbreviated tag name
    private static final String TAG = "RegIntentService";
    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final String FCM_TOKEN = "FCMToken";

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {


        mSession = new AppPreferences(RegistrationIntentService.this);

        // Make a call to Instance API
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        FirebaseInstanceId instanceID = FirebaseInstanceId.getInstance();
        String senderId = getResources().getString(R.string.gcm_defaultSenderId);
        try {
            // request token that will be used by the server to send push notifications
            String token = instanceID.getToken();

            mSession.setPrefrenceString(Constants.FCM_TOKEN, token);


            Log.d(TAG, "FCM Registration Token: " + token);

            sharedPreferences.edit().putString(FCM_TOKEN, token).apply();

            // pass along this data
            sendRegistrationToServer(token);
        } catch (Exception e) {
            e.printStackTrace();
            sharedPreferences.edit().putBoolean(SENT_TOKEN_TO_SERVER, false).apply();
        }
    }

    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.edit().putBoolean(SENT_TOKEN_TO_SERVER, true).apply();
    }


}