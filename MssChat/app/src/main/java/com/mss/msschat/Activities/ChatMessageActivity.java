package com.mss.msschat.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mss.msschat.Adapters.ChatMessageAdapter;
import com.mss.msschat.Adapters.ContactsAdapter;
import com.mss.msschat.Models.ChatMessageModel;
import com.mss.msschat.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;


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
    @Bind(R.id.card_view)
    CardView cardView;
    @Bind(R.id.m_table_menu)
    RelativeLayout mTableMenu;
    ChatMessageAdapter adapter;
    ArrayList<ChatMessageModel> allMessageList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_message_activity);
        ButterKnife.bind(this);

        initUI();

    }

    private void initUI() {


        populateUI();
    }

    private void populateUI() {

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
}
