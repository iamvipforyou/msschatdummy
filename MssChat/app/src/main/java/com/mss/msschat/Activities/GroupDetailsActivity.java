package com.mss.msschat.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mss.msschat.AppUtils.ApiClient;
import com.mss.msschat.AppUtils.AppPreferences;
import com.mss.msschat.AppUtils.Constants;
import com.mss.msschat.AppUtils.Session;
import com.mss.msschat.AppUtils.Utils;
import com.mss.msschat.DataBase.Dao.RecentChatDao;
import com.mss.msschat.DataBase.Dto.ContactsDto;
import com.mss.msschat.DataBase.Dto.RecentChatDto;
import com.mss.msschat.Interfaces.ApiInterface;
import com.mss.msschat.Models.DeleteGroupModel;
import com.mss.msschat.Models.DeleteGroupResponse;
import com.mss.msschat.Models.GroupUserResponse.GroupUserResponseData;
import com.mss.msschat.Models.GroupUserResponse.GroupUsersResponse;
import com.mss.msschat.Models.GroupUserResponse.Success;
import com.mss.msschat.Models.GroupUserResponse.User;
import com.mss.msschat.Models.RemoveGroupUserResponse.RemoveGroupUserResponse;
import com.mss.msschat.Models.RemoveGrpUserModel;
import com.mss.msschat.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupDetailsActivity extends AppCompatActivity {

    @Bind(R.id.backdrop)
    ImageView backdrop;
    @Bind(R.id.ll_add_participant)
    LinearLayout llAddParticipant;
    @Bind(R.id.ll_participants)
    LinearLayout llParticipants;
    @Bind(R.id.card_view)
    CardView cardView;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    List<ContactsDto> getParticipantList;
    ContactsDto participants;
    Toolbar toolbar;
    public RecentChatDao recentChatDao;
    List<RecentChatDto> userOrGroupDetails;
    List<User> listAllGrpUser;
    String senderOrUser;
    private ViewGroup viewGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        ButterKnife.bind(this);
        initUi();
    }

    private void initUi() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        getParticipantList = new ArrayList<>();
        participants = new ContactsDto();
        populateUi();
        initCollapsingToolbar();
    }

    private void populateUi() {
        Utils.setStatusBarColor(GroupDetailsActivity.this);
        viewGroup = (ViewGroup) ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);
        try {
            Intent dataIntent = getIntent();

            senderOrUser = dataIntent.getStringExtra("id");


            recentChatDao = new RecentChatDao(GroupDetailsActivity.this);
            userOrGroupDetails = recentChatDao.getRecentListFriendId(senderOrUser);

            Picasso.with(GroupDetailsActivity.this).load(userOrGroupDetails.get(0).getProfileImage()).into(backdrop);


            getAllGroupUsers();


        } catch (Exception e) {

            e.printStackTrace();
        }


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDeleteGroupDialog();
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        participants.setPhoneNumber("9123456789");
        participants.setUserName("gaurav");
        getParticipantList.add(participants);

    }

    private void getAllGroupUsers() {
        Utils.dialog(GroupDetailsActivity.this);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);


        Call<GroupUsersResponse> getAllGrpUser = apiService.getAllGroupUsers(userOrGroupDetails.get(0).getSenderId());


        getAllGrpUser.enqueue(new Callback<GroupUsersResponse>() {
            @Override
            public void onResponse(Call<GroupUsersResponse> call, Response<GroupUsersResponse> response) {

                Utils.dismissProgressDialog();
                try {
                    if (response.isSuccessful()) {


                        GroupUsersResponse responseData = response.body();

                        int status = responseData.getStatus();


                        if (status == 200) {


                            GroupUserResponseData userResponseData = responseData.getData();

                            List<Success> successData = userResponseData.getSuccess();


                            listAllGrpUser = new ArrayList<User>();

                            for (int i = 0; i < successData.size(); i++) {

                                Success successModel = successData.get(i);

                                List<User> userData = successModel.getUser();

                                listAllGrpUser.addAll(userData);


                            }

                            getParticipants(listAllGrpUser);

                        } else {


                            Utils.showErrorOnTop(viewGroup, responseData.getResponseMessage());


                        }


                    } else {
                        Utils.dismissProgressDialog();


                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    Utils.dismissProgressDialog();
                }


            }

            @Override
            public void onFailure(Call<GroupUsersResponse> call, Throwable t) {
                Utils.dismissProgressDialog();
            }
        });


    }

    public void getParticipants(final List<User> participentList) {
        View displayParticipants = null;
        for (int count = 0; count < participentList.size(); count++) {

            LinearLayout.LayoutParams viewOrdersParams;
            viewOrdersParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            displayParticipants = getLayoutInflater().inflate(R.layout.adapter_group_participant, llParticipants, false);
            displayParticipants.setLayoutParams(viewOrdersParams);
            TextView txtMemberName = (TextView) displayParticipants.findViewById(R.id.txt_contact_name);
            txtMemberName.setText(participentList.get(count).getName());
            TextView txtMemberContact = (TextView) displayParticipants.findViewById(R.id.txt_contact_number);
            txtMemberContact.setText("" + participentList.get(count).getPhoneNumber());


            llParticipants.addView(displayParticipants);

            final int userPos = llParticipants.indexOfChild(displayParticipants);
            displayParticipants.setTag(userPos);


            displayParticipants.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    int position = (Integer) v.getTag();

                    User userData = participentList.get(position);

                    showLocationDialog("" + userData.getPhoneNumber());

                    return false;
                }
            });

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {

               showDeleteGroupDialog();


        }

        return super.onOptionsItemSelected(item);


    }

    @OnClick({R.id.ll_participants, R.id.ll_add_participant})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_participants:

                break;
            case R.id.ll_add_participant:

                Intent addParticipantsIntents = new Intent(GroupDetailsActivity.this, AddGroupMembersActivity.class);
                addParticipantsIntents.putExtra("groupId", senderOrUser);
                startActivity(addParticipantsIntents);
                finish();


                break;
        }
    }

    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbar.setTitle(userOrGroupDetails.get(0).getUserName());
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle((userOrGroupDetails.get(0).getUserName()));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(userOrGroupDetails.get(0).getUserName());
                    isShow = false;
                }
            }
        });
    }

    private void showLocationDialog(final String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(GroupDetailsActivity.this);
        builder.setTitle("Remove User !!");


        String positiveText = "Yes";
        builder.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                removeUserFromGroup(message);

            }
        });

        String negativeText = "No";
        builder.setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                // negative button logic
            }
        });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }

    private void removeUserFromGroup(String message) {

        Utils.dialog(GroupDetailsActivity.this);

        List<String> member = new ArrayList<>();
        member.add(message);

        final RemoveGrpUserModel removeGrpUserModel = new RemoveGrpUserModel();
        removeGrpUserModel.setAdminId(new AppPreferences(GroupDetailsActivity.this).getPrefrenceString(Constants.USER_ID));
        removeGrpUserModel.setGroupId(senderOrUser);
        removeGrpUserModel.setUsers(member);


        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<RemoveGroupUserResponse> removeUserService = apiService.removeUserFromGrp(removeGrpUserModel);


        removeUserService.enqueue(new Callback<RemoveGroupUserResponse>() {
            @Override
            public void onResponse(Call<RemoveGroupUserResponse> call, Response<RemoveGroupUserResponse> response) {

                try {
                    Utils.dismissProgressDialog();

                    RemoveGroupUserResponse removeGroupUserResponse = response.body();


                    int status = removeGroupUserResponse.getStatus();

                    if (status == 200) {

                        llParticipants.removeAllViews();
                        initUi();


                    } else {

                        Utils.showErrorOnTop(viewGroup, removeGroupUserResponse.getResponseMessage());
                    }


                } catch (Exception e) {
                    Utils.dismissProgressDialog();
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call<RemoveGroupUserResponse> call, Throwable t) {
                Utils.dismissProgressDialog();
            }
        });


    }

    private void showDeleteGroupDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(GroupDetailsActivity.this);
        builder.setTitle("Delete/Exit Group !!");


        String positiveText = "Yes";
        builder.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //   removeUserFromGroup(message);

                deleteGroupFromServer();

            }
        });

        String negativeText = "No";
        builder.setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                // negative button logic
            }
        });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }

    private void deleteGroupFromServer() {
        Utils.dialog(GroupDetailsActivity.this);

        DeleteGroupModel deleteGroupModel = new DeleteGroupModel();

        deleteGroupModel.setGroupId(senderOrUser);
        deleteGroupModel.setAdminId(new AppPreferences(GroupDetailsActivity.this).getPrefrenceString(Constants.USER_ID));


        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);


        Call<DeleteGroupResponse> deleteGroup = apiService.deleteGroupFromServer(deleteGroupModel);


        deleteGroup.enqueue(new Callback<DeleteGroupResponse>() {
            @Override
            public void onResponse(Call<DeleteGroupResponse> call, Response<DeleteGroupResponse> response) {
                try {
                    Utils.dismissProgressDialog();


                    if (response.isSuccessful()) {

                        DeleteGroupResponse deleteGroupResponse = response.body();

                        int status = deleteGroupResponse.getStatus();

                        if (status == 200) {


                            RecentChatDao recentChatDao = new RecentChatDao(GroupDetailsActivity.this);

                            recentChatDao.delete(senderOrUser);

                            Session.getUpdateRecentChat();

                            finish();
                        } else {
                            Utils.showErrorOnTop(viewGroup, deleteGroupResponse.getResponseMessage());
                        }


                    } else {

                        Utils.showErrorOnTop(viewGroup, "Server Error !!");
                    }


                } catch (Exception e) {
                    e.printStackTrace();

                    Utils.dismissProgressDialog();
                    Utils.showErrorOnTop(viewGroup, "Server Error !!");
                }


            }

            @Override
            public void onFailure(Call<DeleteGroupResponse> call, Throwable t) {

                Utils.dismissProgressDialog();
            }
        });


    }

}
