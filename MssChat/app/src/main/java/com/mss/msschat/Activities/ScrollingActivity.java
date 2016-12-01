package com.mss.msschat.Activities;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mss.msschat.DataBase.Dto.ContactsDto;
import com.mss.msschat.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ScrollingActivity extends AppCompatActivity {

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
    }

    private void populateUi() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
        getParticipants();
    }

    public void getParticipants() {
        for (int count = 0; count < getParticipantList.size(); count++) {
            LinearLayout.LayoutParams viewOrdersParams;
            viewOrdersParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            View displayParticipants = getLayoutInflater().inflate(R.layout.adapter_group_participant, llParticipants, false);
            displayParticipants.setLayoutParams(viewOrdersParams);
            TextView txtMemberName = (TextView) displayParticipants.findViewById(R.id.txt_contact_name);
            txtMemberName.setText(getParticipantList.get(count).getUserName());
            TextView txtMemberContact = (TextView) displayParticipants.findViewById(R.id.txt_contact_number);
            txtMemberContact.setText(getParticipantList.get(count).getPhoneNumber());
            llParticipants.addView(displayParticipants);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @OnClick(R.id.ll_participants)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_participants:

                break;
        }
    }
}
