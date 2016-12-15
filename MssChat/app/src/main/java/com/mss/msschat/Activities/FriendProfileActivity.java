package com.mss.msschat.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
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

import com.bumptech.glide.Glide;
import com.mss.msschat.AppUtils.ApiClient;
import com.mss.msschat.AppUtils.Connectivity;
import com.mss.msschat.AppUtils.UserPicture;
import com.mss.msschat.AppUtils.Utils;
import com.mss.msschat.Interfaces.ApiInterface;
import com.mss.msschat.Models.UpdateProfileData;
import com.mss.msschat.Models.UpdateProfileResponse;
import com.mss.msschat.Models.UserDetailsRes.UserDetailsResponse;
import com.mss.msschat.Models.UserDetailsRes.UserDetailsResponseData;
import com.mss.msschat.R;

import java.io.ByteArrayOutputStream;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mss on 8/12/16.
 */

public class FriendProfileActivity extends AppCompatActivity {

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
    @Bind(R.id.img_profile)
    CircleImageView imgProfile;
    @Bind(R.id.ll_profile)
    LinearLayout llProfile;
    @Bind(R.id.input_name)
    EditText inputName;
    @Bind(R.id.input_layout_name)
    TextInputLayout inputLayoutName;
    @Bind(R.id.input_password)
    EditText inputPassword;
    @Bind(R.id.input_layout_password)
    TextInputLayout inputLayoutPassword;
    @Bind(R.id.btn_signup)
    FloatingActionButton btnSignup;
    @Bind(R.id.ll_pic)
    ImageView llPic;
    @Bind(R.id.ll_sel_pic)
    LinearLayout llSelPic;
    @Bind(R.id.card_view)
    CardView cardView;
    String userId;
    String updateUser;
    private LinearLayout llTakePic;
    private LinearLayout llChoosePic;
    private Bitmap imgBitmap;
    private ViewGroup viewGroup;
    public String userImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);
        ButterKnife.bind(this);

        initUI();
    }

    private void initUI() {

        viewGroup = (ViewGroup) ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);
        Utils.setStatusBarColor(FriendProfileActivity.this);
        llBack.setVisibility(View.VISIBLE);
        ibtnBack.setImageResource(R.mipmap.ic_keyboard_backspace_white_24dp);
        ibtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });


        try {
            inputName.setFocusable(false);
            inputPassword.setFocusable(false);
            Intent dataIntent = getIntent();
            userId = dataIntent.getStringExtra("userId");

            updateUser = dataIntent.getStringExtra("UserProfile");

            if (updateUser.equals("update")) {

                btnSignup.setVisibility(View.VISIBLE);
                inputName.setFocusableInTouchMode(true);
                llPic.setVisibility(View.VISIBLE);


                Bitmap bitmap = ((BitmapDrawable) imgProfile.getDrawable()).getBitmap();
                userImage = "data:image/png;base64," + BitMapToString(bitmap);

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        populateUI();
    }

    private void populateUI() {
        Utils.setClassTitle(FriendProfileActivity.this, "Profile", toolbar);


        Utils.dialog(FriendProfileActivity.this);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);


        Call<UserDetailsResponse> getUserDetails = apiService.getUserDetails(userId);


        getUserDetails.enqueue(new Callback<UserDetailsResponse>() {
            @Override
            public void onResponse(Call<UserDetailsResponse> call, Response<UserDetailsResponse> response) {

                try {
                    Utils.dismissProgressDialog();


                    if (response.isSuccessful()) {

                        UserDetailsResponse userDetailsResponse = response.body();

                        int status = userDetailsResponse.getStatus();

                        if (status == 200) {


                            final UserDetailsResponseData userDetailsResponseData = userDetailsResponse.getData();


                            if (userDetailsResponseData.getProfilePic() != null) {
                                Glide.with(getApplicationContext()).load(userDetailsResponseData.getProfilePic()).into(imgProfile);
                            } else {

                                Glide.with(getApplicationContext()).load(R.mipmap.ic_launcher).into(imgProfile);
                            }


                            inputName.setText(userDetailsResponseData.getName());
                            inputPassword.setText("" + userDetailsResponseData.getPhoneNumber());


                            imgProfile.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {


                                    Utils.showImageDialog(FriendProfileActivity.this, userDetailsResponseData.getProfilePic(), userDetailsResponseData.getName());


                                }
                            });


                        }


                    } else {


                    }


                } catch (Exception e) {
                    e.printStackTrace();

                    Utils.dismissProgressDialog();
                }


            }

            @Override
            public void onFailure(Call<UserDetailsResponse> call, Throwable t) {
                Utils.dismissProgressDialog();
            }
        });


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
                    Uri temUri = getImageUri(FriendProfileActivity.this, imgBitmap);
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

    @OnClick({R.id.btn_signup, R.id.ll_sel_pic})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_signup:

                if (validation()) {
                    updateProfile();

                }
                break;
            case R.id.ll_sel_pic:

                selectProfile();
                break;
        }
    }

    private void updateProfile() {


        UpdateProfileData updateProfileData = new UpdateProfileData();

        updateProfileData.setName(inputName.getText().toString());
        updateProfileData.setPhoneNumber(inputPassword.getText().toString());
        updateProfileData.setProfilePic(userImage);


        Utils.dialog(FriendProfileActivity.this);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<UpdateProfileResponse> getUpdateProfileData = apiService.getupdateProfileResponse(updateProfileData);


        getUpdateProfileData.enqueue(new Callback<UpdateProfileResponse>() {
            @Override
            public void onResponse(Call<UpdateProfileResponse> call, Response<UpdateProfileResponse> response) {

                Utils.dismissProgressDialog();


                try {
                    if (response.isSuccessful()) {
                        UpdateProfileResponse updateProfileResponse = response.body();

                        int status = updateProfileResponse.getStatus();


                        if (status == 200) {

                            Utils.showErrorOnTop(viewGroup, "Profile updated Successfully");


                            initUI();

                        } else {

                            Utils.showErrorOnTop(viewGroup, updateProfileResponse.getResponseMessage());

                        }
                    }else{

                        Utils.dismissProgressDialog();
                    }

                } catch (Exception e) {

                    e.printStackTrace();

                }


            }

            @Override
            public void onFailure(Call<UpdateProfileResponse> call, Throwable t) {
                Utils.dismissProgressDialog();
            }
        });


    }

    private boolean validation() {
        if (TextUtils.isEmpty(inputName.getText().toString().trim())) {
            Utils.showErrorOnTop(viewGroup, "Please Enter Name");
            return false;
        } else if (!Connectivity.isConnected(FriendProfileActivity.this)) {

            Utils.showErrorOnTop(viewGroup, "No Internet connection !!");
            return false;


        } else {
            return true;

        }
    }


}