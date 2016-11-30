package com.mss.msschat.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.mss.msschat.AppUtils.AppPreferences;
import com.mss.msschat.AppUtils.Constants;
import com.mss.msschat.R;


public class SplashActivity extends Activity {

    AppPreferences mSession;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        mSession = new AppPreferences(this);
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
        }, 2000);


    }
}
