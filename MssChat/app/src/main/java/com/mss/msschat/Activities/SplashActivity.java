package com.mss.msschat.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.mss.msschat.AppUtils.AppPreferences;
import com.mss.msschat.AppUtils.Constants;
import com.mss.msschat.AppUtils.Utils;
import com.mss.msschat.R;


public class SplashActivity extends Activity {

    AppPreferences mSession;
    private static final int REQUEST_PERMISSION = 1;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        Utils.setStatusBarColor(SplashActivity.this);
        mSession = new AppPreferences(this);
        checkPermission();
    }

    private void startActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Check if Pattern is Already stored in App
                if (mSession.getPrefrenceString(Constants.USER_ID).isEmpty()) {
                    Intent logInIntent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(logInIntent);
                    finish();
                } else if (mSession.getPrefrenceString(Constants.PATTERN_SHA_VALUE).isEmpty()) {
                    //Set New Pattern
                    Intent setPatternIntent = new Intent(SplashActivity.this, SetPatternActivity.class);
                    startActivity(setPatternIntent);
                    finish();
                } else {
                    //Confirm Pattern If Already Stored
                    Intent confirmPatternIntent = new Intent(SplashActivity.this, ConfirmPatternActivity.class);
                    startActivity(confirmPatternIntent);
                    finish();
                }
            }
        }, 3000);
    }


    public void checkPermission() {

        //get all required elements
        int READ_PHONE_PERMISSION = ContextCompat.checkSelfPermission(SplashActivity.this, android.Manifest.permission.READ_PHONE_STATE);
        int READ_EXTERNALSTORAGE_PERMISSION = ContextCompat.checkSelfPermission(SplashActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        int WRITE_EXTERNALSTORAGE_PERMISSION = ContextCompat.checkSelfPermission(SplashActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int ACCESS_READ_CONTACT_PREMISSION = ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.READ_CONTACTS);
        int ACCESS_WIFI_STATE_PREMISSION = ContextCompat.checkSelfPermission(SplashActivity.this, android.Manifest.permission.ACCESS_WIFI_STATE);
        int ACCESS_NETWORK_STATE_PREMISSION = ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.ACCESS_NETWORK_STATE);
        int ACCESS_CAMERA_PREMISSION = ContextCompat.checkSelfPermission(SplashActivity.this, android.Manifest.permission.CAMERA);

        //Check permissions if permission already granted run if blog if not granted then run else blog
        if (READ_PHONE_PERMISSION == PackageManager.PERMISSION_GRANTED && READ_EXTERNALSTORAGE_PERMISSION == PackageManager.PERMISSION_GRANTED && WRITE_EXTERNALSTORAGE_PERMISSION == PackageManager.PERMISSION_GRANTED && ACCESS_NETWORK_STATE_PREMISSION == PackageManager.PERMISSION_GRANTED && ACCESS_WIFI_STATE_PREMISSION == PackageManager.PERMISSION_GRANTED && ACCESS_READ_CONTACT_PREMISSION == PackageManager.PERMISSION_GRANTED && ACCESS_CAMERA_PREMISSION == PackageManager.PERMISSION_GRANTED) {
            startActivity();
        } else {

            // Marshmallow+
            ActivityCompat.requestPermissions(SplashActivity.this, new String[]{android.Manifest.permission.READ_PHONE_STATE, android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.ACCESS_NETWORK_STATE, android.Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.READ_CONTACTS, android.Manifest.permission.CAMERA}, REQUEST_PERMISSION);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {

            case REQUEST_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Proceed to next steps
                    startActivity();

                } else {

                }
                return;
            }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


}
