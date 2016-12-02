package com.mss.msschat.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mss.msschat.Adapters.ChatMessageAdapter;
import com.mss.msschat.AppUtils.Utils;
import com.mss.msschat.DataBase.Dao.RecentChatDao;
import com.mss.msschat.DataBase.Dto.RecentChatDto;
import com.mss.msschat.Models.ChatMessageModel;
import com.mss.msschat.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by mss on 24/11/16.
 */

public class ChatMessageActivity extends AppCompatActivity {


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
    @Bind(R.id.lv_chat_messages)
    RecyclerView lvChatMessages;
    @Bind(R.id.ll_camera)
    LinearLayout llCamera;
    @Bind(R.id.edit_msg)
    EditText editMsg;
    @Bind(R.id.ll_btn_send)
    LinearLayout llBtnSend;
    RecentChatDao recentChatDao;
    @Bind(R.id.m_table_menu)
    RelativeLayout mTableMenu;
    ChatMessageAdapter adapter;
    ArrayList<ChatMessageModel> allMessageList;
    List<RecentChatDto> recentChatListById;
    @Bind(R.id.img_profile)
    CircleImageView imgProfile;
    @Bind(R.id.ll_pic)
    LinearLayout llPic;
    @Bind(R.id.ll_title)
    LinearLayout llTitle;
    private String senderOrGrpId;
    LinearLayout ll_txtTitles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_message_activity);
        ButterKnife.bind(this);
        initUI();
    }

    private void initUI() {

        ll_txtTitles = (LinearLayout) findViewById(R.id.ll_title);
        populateUI();
    }

    private void populateUI() {


        Utils.setClassTitle(ChatMessageActivity.this, "User", toolbar);
        Utils.setStatusBarColor(ChatMessageActivity.this);
        recentChatDao = new RecentChatDao(ChatMessageActivity.this);
        try {

            Intent dataIntent = getIntent();

            senderOrGrpId = dataIntent.getStringExtra("id");
            recentChatListById = recentChatDao.getRecentListFriendId(senderOrGrpId);

            toolbarTitle.setText(recentChatListById.get(0).getUserName());

            Picasso.with(ChatMessageActivity.this).load(recentChatListById.get(0).getProfileImage()).into(imgProfile);

        } catch (Exception e) {
            e.printStackTrace();
        }


        allMessageList = new ArrayList<ChatMessageModel>();


        for (int i = 0; i < 10; i++) {

            ChatMessageModel chatMessageModel = new ChatMessageModel();


            if (i % 2 == 0) {


                chatMessageModel.setMessage("this is user1");
                chatMessageModel.setType("true");


            } else {


                chatMessageModel.setMessage("this is user2");
                chatMessageModel.setType("false");


            }


            allMessageList.add(chatMessageModel);


        }


        adapter = new ChatMessageAdapter(getApplicationContext(), allMessageList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        lvChatMessages.setLayoutManager(layoutManager);
        lvChatMessages.setItemAnimator(new DefaultItemAnimator());
        lvChatMessages.setAdapter(adapter);


    }

    @OnClick(R.id.ll_title)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_title:

                Intent detailsIntent = new Intent(ChatMessageActivity.this, GroupDetailsActivity.class);
                detailsIntent.putExtra("id", senderOrGrpId);
                startActivity(detailsIntent);
                finish();

                break;
        }


    }
}
