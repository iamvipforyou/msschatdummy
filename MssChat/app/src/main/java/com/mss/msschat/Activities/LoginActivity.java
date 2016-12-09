package com.mss.msschat.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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
import com.mss.msschat.AppUtils.UserPicture;
import com.mss.msschat.AppUtils.Utils;
import com.mss.msschat.Interfaces.ApiInterface;
import com.mss.msschat.Models.RegistrationModel;
import com.mss.msschat.Models.RegistrationResponse;
import com.mss.msschat.Models.RegistrationResponseData;
import com.mss.msschat.NotificationUtils.RegistrationIntentService;
import com.mss.msschat.R;
import com.mss.msschat.Services.ContactFirstSyncIntentService;

import java.io.ByteArrayOutputStream;
import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
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
    EditText editName;
    @Bind(R.id.input_layout_name)
    TextInputLayout inputLayoutName;
    @Bind(R.id.input_password)
    EditText editPassword;
    @Bind(R.id.input_layout_password)
    TextInputLayout inputLayoutPassword;

    @Bind(R.id.card_view)
    CardView cardView;
    AppPreferences mSession;
    @Bind(R.id.btn_signup)
    FloatingActionButton btnSignup;
    @Bind(R.id.img_profile)
    CircleImageView imgProfile;
    @Bind(R.id.ll_profile)
    LinearLayout llProfile;
    @Bind(R.id.ll_pic)
    ImageView llPic;
    @Bind(R.id.ll_sel_pic)
    LinearLayout llSelPic;
    private LinearLayout llTakePic;
    private LinearLayout llChoosePic;
    private Bitmap imgBitmap;
    private ViewGroup viewGroup;
    public String userImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        intUI();
    }

    private void intUI() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        Utils.setStatusBarColor(LoginActivity.this);
        if (checkPlayServices()) {
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
        viewGroup = (ViewGroup) ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);
        populateUI();
    }

    private void populateUI() {
        mSession = new AppPreferences(LoginActivity.this);
        toolbarTitle.setText("Sign Up");


        try {

                String extStorageDirectory = Environment.getExternalStorageDirectory()
                        .toString();
                File folder = new File(extStorageDirectory, ".MssChat");
                folder.mkdir();
            new AppPreferences(LoginActivity.this).setPrefrenceLong("countDownload", System.currentTimeMillis());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void signUpToServer() {

        String numberString = editPassword.getText().toString();
        //    long numberlong = Long.parseLong(numberString);
        //   int numberValue = (int) numberlong;

        RegistrationModel registrationData = new RegistrationModel();
        registrationData.setName(editName.getText().toString());
        registrationData.setPhoneNumber(numberString);
        registrationData.setDeviceType("android");
        registrationData.setProfilePic(userImage);
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

                         //   Toast.makeText(LoginActivity.this, "" + userData.getId(), Toast.LENGTH_SHORT).show();

                            startService(new Intent(LoginActivity.this, ContactFirstSyncIntentService.class));
                            Intent mainIntent = new Intent(LoginActivity.this, SetPatternActivity.class);
                            startActivity(mainIntent);
                            finish();


                        } else if (responseStatus == 400) {
                            String errorMessage = finalResponse.getResponseMessage();
                            Toast.makeText(LoginActivity.this, "" + errorMessage, Toast.LENGTH_SHORT).show();
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

    boolean validationSignUp() {
        if (TextUtils.isEmpty(editName.getText().toString().trim())) {
            Utils.showErrorOnTop(viewGroup, "Please Enter Name");
            return false;
        } else if (TextUtils.isEmpty(editPassword.getText().toString().trim())) {
            Utils.showErrorOnTop(viewGroup, "Please Enter Mobile No.");
            return false;
        } else if (editPassword.getText().toString().trim().length() < 10) {
            Utils.showErrorOnTop(viewGroup, "Please Enter valid Number");
            return false;
        } else {
            return true;
        }
    }


    @OnClick({R.id.btn_signup, R.id.img_profile, R.id.ll_sel_pic})
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btn_signup:
                if (validationSignUp()) {
                    mSession.setPrefrenceString(Constants.USERNAME, editName.getText().toString());
                    mSession.setPrefrenceString(Constants.USER_MOBILE_NUMBER, editPassword.getText().toString());
                    signUpToServer();
                }
                break;

            case R.id.img_profile:
                selectProfile();
                break;
            case R.id.ll_sel_pic:
                selectProfile();
                break;
        }
    }

    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = null;
        try {
            System.gc();
            temp = Base64.encodeToString(b, Base64.NO_WRAP);
        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            b = baos.toByteArray();
            temp = Base64.encodeToString(b, Base64.NO_WRAP);
            Log.e("EWN", "Out of memory error catched");
        }
        return temp;
    }

    private void selectProfile() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_select_picture);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER;
        wmlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wmlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        llTakePic = (LinearLayout) dialog.findViewById(R.id.ll_take_pic);
        llChoosePic = (LinearLayout) dialog.findViewById(R.id.ll_choose_pic);
        llTakePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 2);
                dialog.dismiss();
            }
        });
        llChoosePic.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent galleryImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryImageIntent, 3);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 2) {
                try {
                    imgBitmap = (Bitmap) data.getExtras().get("data");
                    Uri temUri = getImageUri(LoginActivity.this, imgBitmap);
                    Intent updateIntent = new Intent(this, ProfileCropViewActivity.class);
                    updateIntent.setData(temUri);
                    startActivityForResult(updateIntent, 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 3) {
                try {
                    Intent updateIntent = new Intent(this, ProfileCropViewActivity.class);
                    updateIntent.setData(data.getData());
                    startActivityForResult(updateIntent, 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 1) {
                try {
                    if (resultCode == RESULT_OK) {
                        Uri selectedImageUri = data.getData();
                        imgBitmap = new UserPicture(selectedImageUri, getContentResolver()).getBitmap();
                        imgProfile.setImageBitmap(new UserPicture(selectedImageUri, getContentResolver()).getBitmap());
                        userImage = "data:image/png;base64," + BitMapToString(new UserPicture(selectedImageUri, getContentResolver()).getBitmap());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}
