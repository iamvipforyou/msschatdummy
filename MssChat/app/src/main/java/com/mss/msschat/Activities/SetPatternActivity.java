package com.mss.msschat.Activities;

import android.content.Intent;


import com.mss.msschat.AppUtils.AppPreferences;
import com.mss.msschat.AppUtils.Constants;

import java.util.List;

import me.zhanghai.android.patternlock.PatternUtils;
import me.zhanghai.android.patternlock.PatternView;


public class SetPatternActivity extends me.zhanghai.android.patternlock.SetPatternActivity {

    AppPreferences mSession;

    @Override
    protected void onSetPattern(List<PatternView.Cell> pattern) {
        super.onSetPattern(pattern);
        mSession = new AppPreferences(this);
        //SHA value of Pattern
        String patternSha1 = PatternUtils.patternToSha1String(pattern);
        // Save SHA of Pattern to Preferences
        mSession.setPrefrenceString(Constants.PATTERN_SHA_VALUE, patternSha1);
        //Starts The MainApp
        Intent main = new Intent(SetPatternActivity.this, MainActivity.class);
        startActivity(main);

    }
}
