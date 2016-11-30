package com.mss.msschat.NotificationUtils;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.mss.msschat.AppUtils.AppPreferences;
import com.mss.msschat.AppUtils.Constants;

import static android.content.ContentValues.TAG;

/**
 * Created by ${Gaurav} on 25/8/16.
 */

public class MyInstanceIDListenerService extends FirebaseInstanceIdService {

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is also called
     * when the InstanceID token is initially generated, so this is where
     * you retrieve the token.
     */
    // [START refresh_token]0
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        new AppPreferences(MyInstanceIDListenerService.this).setPrefrenceString(Constants.FCM_TOKEN, refreshedToken);
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        // TODO: Implement this method to send any registration to your app's servers.

        //   sendRegistrationToServer(refreshedToken);


    }
}
