package com.mss.msschat.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mss.msschat.AppUtils.ApiClient;
import com.mss.msschat.AppUtils.AppPreferences;
import com.mss.msschat.AppUtils.Constants;
import com.mss.msschat.AppUtils.Utils;
import com.mss.msschat.Interfaces.ApiInterface;
import com.mss.msschat.Models.CreateGroupModel;
import com.mss.msschat.Models.CreateGroupResponse;
import com.mss.msschat.Models.GroupResponseData;
import com.mss.msschat.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mss on 30/11/16.
 */

public class CreateGroupActivity extends AppCompatActivity {


    public ViewGroup viewGroup;
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
    @Bind(R.id.view)
    View view;
    @Bind(R.id.img_profile)
    CircleImageView imgProfile;
    @Bind(R.id.ll_profile)
    LinearLayout llProfile;
    @Bind(R.id.edit_grp_name)
    EditText editGrpName;
    @Bind(R.id.card_view)
    CardView cardView;
    @Bind(R.id.btn_signup)
    FloatingActionButton btnSignup;
    @Bind(R.id.ll_pic)
    ImageView llPic;
    @Bind(R.id.ll_sel_pic)
    LinearLayout llSelPic;
    @Bind(R.id.card_view3)
    CardView cardView3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);
        ButterKnife.bind(this);
        initUI();
    }

    private void initUI() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        populateUI();
    }

    private void populateUI() {
        viewGroup = (ViewGroup) ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);
    }


    public void createGroupOnServer() {

        CreateGroupModel createGroupModel = new CreateGroupModel();
        createGroupModel.setName("myGrp");
        createGroupModel.setAdminId(new AppPreferences(CreateGroupActivity.this).getPrefrenceString(Constants.USER_ID));
        createGroupModel.setGroupPic("");
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<CreateGroupResponse> getGroupDetails = apiService.getCreateGroupDetails(createGroupModel);
        getGroupDetails.enqueue(new Callback<CreateGroupResponse>() {
            @Override
            public void onResponse(Call<CreateGroupResponse> call, Response<CreateGroupResponse> response) {

                try {
                    if (response.isSuccessful()) {
                        CreateGroupResponse createGroupResponse = response.body();
                        int statusCode = createGroupResponse.getStatus();
                        if (statusCode == Constants.SUCCESSFUL) {
                            GroupResponseData responseData = createGroupResponse.getData();
                            Intent addMemberIntent = new Intent(CreateGroupActivity.this, AddGroupMembersActivity.class);
                            startActivity(addMemberIntent);
                        }
                    } else {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Utils.showErrorOnTop(viewGroup, "Server Error !!");
                }

            }

            @Override
            public void onFailure(Call<CreateGroupResponse> call, Throwable t) {

            }
        });


    }

    @OnClick(R.id.btn_signup)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_signup:
                startActivity(new Intent(CreateGroupActivity.this, ScrollingActivity.class));
                break;
        }

    }
}
