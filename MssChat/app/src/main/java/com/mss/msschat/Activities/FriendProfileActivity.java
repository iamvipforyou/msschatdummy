package com.mss.msschat.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mss.msschat.AppUtils.ApiClient;
import com.mss.msschat.AppUtils.Utils;
import com.mss.msschat.Interfaces.ApiInterface;
import com.mss.msschat.Models.GroupUserResponse.GroupUsersResponse;
import com.mss.msschat.Models.UserDetailsRes.UserDetailsResponse;
import com.mss.msschat.Models.UserDetailsRes.UserDetailsResponseData;
import com.mss.msschat.R;

import butterknife.Bind;
import butterknife.ButterKnife;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);
        ButterKnife.bind(this);

        initUI();
    }

    private void initUI() {
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
            Intent dataIntent = getIntent();
            userId = dataIntent.getStringExtra("userId");
        } catch (Exception e) {
            e.printStackTrace();
        }
        populateUI();
    }

    private void populateUI() {
        Utils.setClassTitle(FriendProfileActivity.this, "Profile", toolbar);
        inputName.setFocusable(false);
        inputPassword.setFocusable(false);


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
}
