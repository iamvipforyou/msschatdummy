package com.mss.msschat.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import com.mss.msschat.AppUtils.ApiClient;
import com.mss.msschat.AppUtils.AppPreferences;
import com.mss.msschat.AppUtils.Constants;
import com.mss.msschat.AppUtils.Utils;
import com.mss.msschat.Interfaces.ApiInterface;
import com.mss.msschat.Models.CreateGroupModel;
import com.mss.msschat.Models.CreateGroupResponse;
import com.mss.msschat.Models.GroupResponseData;
import com.mss.msschat.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mss on 30/11/16.
 */

public class CreateGroupActivity extends AppCompatActivity {


    public ViewGroup viewGroup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_group);


        initUI();


    }

    private void initUI() {


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
}
