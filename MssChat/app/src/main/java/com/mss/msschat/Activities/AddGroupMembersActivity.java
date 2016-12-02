package com.mss.msschat.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mss.msschat.Adapters.AddMembersAdapter;
import com.mss.msschat.AppUtils.ApiClient;
import com.mss.msschat.AppUtils.AppPreferences;
import com.mss.msschat.AppUtils.Constants;
import com.mss.msschat.AppUtils.Utils;
import com.mss.msschat.DataBase.Dao.ContactsDao;
import com.mss.msschat.DataBase.Dto.ContactsDto;
import com.mss.msschat.Interfaces.ApiInterface;
import com.mss.msschat.Interfaces.showSelectedMembersImage;
import com.mss.msschat.Models.AddMemberModel;
import com.mss.msschat.Models.AddingMemberResponse.AddMemberResponse;
import com.mss.msschat.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mss on 30/11/16.
 */

public class AddGroupMembersActivity extends AppCompatActivity implements showSelectedMembersImage {


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
    @Bind(R.id.ll_contact_images)
    LinearLayout llContactImages;
    @Bind(R.id.card_view)
    CardView cardView;
    @Bind(R.id.lv_contacts)
    RecyclerView lvContacts;
    public ContactsDao contactsDao;
    AddMembersAdapter adapter;
    List<ContactsDto> allUserFromContactList;
    List<ContactsDto> selectedMembers;
    @Bind(R.id.btn_done)
    FloatingActionButton btnDone;
    private int count;

    String groupId;

    List<Long> selectedUserArray;
    public ViewGroup viewGroup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_member_activity);
        ButterKnife.bind(this);
        initUI();
    }

    private void initUI() {
        btnDone = (FloatingActionButton) findViewById(R.id.btn_done);
        viewGroup = (ViewGroup) ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);
        populateUI();
    }

    private void populateUI() {
        try {

            Intent dataIntent = getIntent();

            groupId = dataIntent.getStringExtra("groupId");


        } catch (Exception e) {
            e.printStackTrace();
        }


        llContactImages.setVisibility(View.GONE);
        cardView.setVisibility(View.GONE);
        contactsDao = new ContactsDao(AddGroupMembersActivity.this);
        allUserFromContactList = contactsDao.getAllAppContacts();
        adapter = new AddMembersAdapter(getApplicationContext(), allUserFromContactList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        lvContacts.setLayoutManager(layoutManager);
        lvContacts.setItemAnimator(new DefaultItemAnimator());
        lvContacts.setAdapter(adapter);
    }


    @Override
    public void getSelectedMembers() {
        selectedMembers = new ArrayList<ContactsDto>();
        llContactImages.removeAllViews();
        selectedMembers = ((AddMembersAdapter) adapter).allSelectedMembers();
        count = selectedMembers.size();
        selectedUserArray = new ArrayList<Long>();
        for (int i = 0; i < selectedMembers.size(); i++) {
            ContactsDto contactsDto = selectedMembers.get(i);
            if (contactsDto.isSelected()) {
                cardView.setVisibility(View.VISIBLE);
                llContactImages.setVisibility(View.VISIBLE);
                LinearLayout.LayoutParams viewOrdersParams;
                viewOrdersParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                View displayOrdersView = getLayoutInflater().inflate(R.layout.add_member_image, llContactImages, false);
                ImageView img = (ImageView) displayOrdersView.findViewById(R.id.img_contact_profile);

                //   Glide.with(getApplicationContext()).load(contactsDto.getUserPicture()).into(img);

                //  img.setImageResource(R.mipmap.ic_launcher);
                if (contactsDto.getUserPicture() != null) {

                    Picasso.with(AddGroupMembersActivity.this).load(contactsDto.getUserPicture()).into(img);
                    // Glide.with(AddGroupMembersActivity.this).load(contactsDto.getUserPicture()).into(img);
                    //  holder.imgContactProfile.setImageResource(R.mipmap.ic_launcher);


                } else {
                    img.setImageResource(R.mipmap.ic_launcher);

                }


                displayOrdersView.setLayoutParams(viewOrdersParams);
                llContactImages.addView(displayOrdersView);
                if (count > 0) {
                    count--;
                }


                selectedUserArray.add(Long.parseLong(contactsDto.getPhoneNumber()));


            }

            if (count == selectedMembers.size()) {
                cardView.setVisibility(View.GONE);
                llContactImages.setVisibility(View.GONE);
            }
        }
    }

    @OnClick(R.id.btn_done)
    public void onClick() {

        addMembersToGroup();


    }

    private void addMembersToGroup() {

        Utils.dialog(AddGroupMembersActivity.this);

        AddMemberModel addMemberModel = new AddMemberModel();

        addMemberModel.setAdminId(new AppPreferences(AddGroupMembersActivity.this).getPrefrenceString(Constants.USER_ID));
        addMemberModel.setGroupId(groupId);
        addMemberModel.setUsers(selectedUserArray);


        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<AddMemberResponse> addSelectedMember = apiService.getAddSelectedMemberResponse(addMemberModel);


        addSelectedMember.enqueue(new Callback<AddMemberResponse>() {
            @Override
            public void onResponse(Call<AddMemberResponse> call, Response<AddMemberResponse> response) {
                try {

                    if (response.isSuccessful()) {

                        Utils.dismissProgressDialog();

                        AddMemberResponse addMemberResponse = response.body();


                        int status = addMemberResponse.getStatus();


                        if (status == 200) {

                            Utils.showErrorOnTop(viewGroup, "Members Added Successfully !!");

                            finish();
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
            public void onFailure(Call<AddMemberResponse> call, Throwable t) {

                Utils.dismissProgressDialog();
            }
        });


    }
}
