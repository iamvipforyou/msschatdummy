package com.mss.msschat.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.mss.msschat.AppUtils.ApiClient;
import com.mss.msschat.AppUtils.AppPreferences;
import com.mss.msschat.AppUtils.Constants;
import com.mss.msschat.AppUtils.Utils;
import com.mss.msschat.Interfaces.ApiInterface;
import com.mss.msschat.MainActivity;
import com.mss.msschat.Models.RegistrationModel;
import com.mss.msschat.Models.RegistrationResponse;
import com.mss.msschat.Models.RegistrationResponseData;
import com.mss.msschat.NotificationUtils.RegistrationIntentService;
import com.mss.msschat.R;
import com.mss.msschat.Services.ContactFirstSyncIntentService;

import java.math.BigInteger;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.zhanghai.android.patternlock.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mss on 25/11/16.
 */

public class LoginActivity extends AppCompatActivity {

    long numbeLong;
    @Bind(R.id.ibtn_back)
    ImageView ibtnBack;
    @Bind(R.id.ll_back)
    LinearLayout llBack;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.ibtn_refresh)
    ImageView ibtnRefresh;
    @Bind(R.id.ll_refresh)
    LinearLayout llRefresh;
    @Bind(R.id.ibtn_right)
    ImageView ibtnRight;
    @Bind(R.id.ll_right)
    LinearLayout llRight;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.input_name)
    EditText inputName;
    @Bind(R.id.input_layout_name)
    TextInputLayout inputLayoutName;
    @Bind(R.id.input_password)
    EditText inputPassword;
    @Bind(R.id.input_layout_password)
    TextInputLayout inputLayoutPassword;

    @Bind(R.id.card_view)
    CardView cardView;
    AppPreferences mSession;
    @Bind(R.id.btn_signup)
    Button btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        ButterKnife.bind(this);


        intUI();


    }

    private void intUI() {
        Utils.setStatusBarColor(LoginActivity.this);
        //   Utils.setTitle(LoginActivity.this, "Sign Up");
        if (checkPlayServices()) {
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }

        populateUI();
    }

    private void populateUI() {
        mSession = new AppPreferences(LoginActivity.this);


    }


    public void signUpToServer() {

        String numberString = inputPassword.getText().toString();
        //    long numberlong = Long.parseLong(numberString);

        //   int numberValue = (int) numberlong;

        RegistrationModel registrationData = new RegistrationModel();
        registrationData.setName(inputName.getText().toString());
        registrationData.setPhoneNumber(numberString);
        registrationData.setDeviceType("android");
        registrationData.setDeviceId(mSession.getPrefrenceString(Constants.FCM_TOKEN));

        Utils.dialog(LoginActivity.this);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<RegistrationResponse> registrationResponseData = apiService.getRegistrationResponse(registrationData);

        registrationResponseData.enqueue(new Callback<RegistrationResponse>() {
            @Override
            public void onResponse(Call<RegistrationResponse> call, Response<RegistrationResponse> response) {

                try {
                    if (response.isSuccessful()) {
                        Utils.dismissProgressDialog();


                        RegistrationResponse finalResponse = response.body();

                        int responseStatus = finalResponse.getStatus();

                        if (responseStatus == 200) {

                            RegistrationResponseData userData = finalResponse.getData();

                            mSession.setPrefrenceString(Constants.USER_ID, userData.getId());

                            Toast.makeText(LoginActivity.this, "" + userData.getId(), Toast.LENGTH_SHORT).show();

                            startService(new Intent(LoginActivity.this, ContactFirstSyncIntentService.class));

                            Intent mainIntent = new Intent(LoginActivity.this, SetPatternActivity.class);
                            startActivity(mainIntent);


                        } else {


                            String errorMessage = finalResponse.getResponseMessage();

                            Toast.makeText(LoginActivity.this, "" + errorMessage, Toast.LENGTH_SHORT).show();

                        }


                    } else {
                        Utils.dismissProgressDialog();
                        Toast.makeText(LoginActivity.this, "Something went wrong !!", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Utils.dismissProgressDialog();


                }

            }

            @Override
            public void onFailure(Call<RegistrationResponse> call, Throwable t) {
                Utils.dismissProgressDialog();
            }
        });


    }


    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, 12).show();
            } else {
                Log.i("Error", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    @OnClick(R.id.btn_signup)
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btn_signup:

                signUpToServer();

                break;


        }


    }
}
