package com.mss.msschat.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mss.msschat.Adapters.AddMembersAdapter;
import com.mss.msschat.Adapters.ContactsAdapter;
import com.mss.msschat.DataBase.Dao.ContactsDao;
import com.mss.msschat.DataBase.Dto.ContactsDto;
import com.mss.msschat.Interfaces.showSelectedMembersImage;
import com.mss.msschat.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

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
    private int count;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_member_activity);
        ButterKnife.bind(this);

        initUI();

    }

    private void initUI() {

        populateUI();
    }

    private void populateUI() {
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

        for (int i = 0; i < selectedMembers.size(); i++) {

            ContactsDto contactsDto = selectedMembers.get(i);


            if (contactsDto.isSelected()) {
                cardView.setVisibility(View.VISIBLE);
                llContactImages.setVisibility(View.VISIBLE);

                LinearLayout.LayoutParams viewOrdersParams;
                viewOrdersParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                View displayOrdersView = getLayoutInflater().inflate(R.layout.add_member_image, llContactImages, false);

                ImageView img = (ImageView) displayOrdersView.findViewById(R.id.img_contact_profile);

                img.setImageResource(R.mipmap.ic_launcher);

                displayOrdersView.setLayoutParams(viewOrdersParams);

                llContactImages.addView(displayOrdersView);
                if (count > 0) {
                    count--;
                }


            }

            if (count == selectedMembers.size()) {
                cardView.setVisibility(View.GONE);
                llContactImages.setVisibility(View.GONE);

            }


        }


    }
}
