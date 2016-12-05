package com.mss.msschat.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mss.msschat.Adapters.ChatMessageAdapter;
import com.mss.msschat.AppUtils.AppController;
import com.mss.msschat.AppUtils.AppPreferences;
import com.mss.msschat.AppUtils.Constants;
import com.mss.msschat.AppUtils.Session;
import com.mss.msschat.AppUtils.Utils;
import com.mss.msschat.DataBase.Dao.MessageDao;
import com.mss.msschat.DataBase.Dao.RecentChatDao;
import com.mss.msschat.DataBase.Dto.MessageDto;
import com.mss.msschat.DataBase.Dto.RecentChatDto;
import com.mss.msschat.Models.ChatMessageModel;
import com.mss.msschat.Models.Message;
import com.mss.msschat.R;
import com.mss.msschat.SocketUtils.Chat;
import com.mss.msschat.SocketUtils.Emitters;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


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
    AppController appController;
    private Socket mSocket;
    String mIntentFrom;
    Emitters emitters;
    Chat chat;
    String friendName = "", friendid = "", TypeMessage = "";
    MessageDao messageDao;

    private AppPreferences mSession;
    String friendsId;
    private String contactImage;
    private String typeMessageFrom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_message_activity);
        ButterKnife.bind(this);
        initUI();
    }

    private void initUI() {


        ll_txtTitles = (LinearLayout) findViewById(R.id.ll_title);
        mSession = new AppPreferences(ChatMessageActivity.this);
        messageDao = new MessageDao(ChatMessageActivity.this);
        openSocket();
        populateUI();
    }

    private void populateUI() {


        Utils.setClassTitle(ChatMessageActivity.this, "User", toolbar);
        Utils.setStatusBarColor(ChatMessageActivity.this);
        recentChatDao = new RecentChatDao(ChatMessageActivity.this);
        try {

            Intent dataIntent = getIntent();

            mIntentFrom = dataIntent.getStringExtra("intentFrom");
            senderOrGrpId = dataIntent.getStringExtra("id");
            typeMessageFrom = dataIntent.getStringExtra("typeMessage");

            if (mIntentFrom.equals("recent")) {


                recentChatListById = recentChatDao.getRecentListFriendId(senderOrGrpId);
                friendName = recentChatListById.get(0).getUserName();
                toolbarTitle.setText(friendName);

                contactImage = recentChatListById.get(0).getProfileImage();
                if (recentChatListById.get(0).getProfileImage() != null) {
                    Picasso.with(ChatMessageActivity.this).load(recentChatListById.get(0).getProfileImage()).into(imgProfile);

                } else {

                    Picasso.with(ChatMessageActivity.this).load(R.mipmap.ic_launcher).into(imgProfile);
                }


            } else if (mIntentFrom.equals("contacts")) {

                friendName = dataIntent.getStringExtra("user_name");
                contactImage = dataIntent.getStringExtra("user_image");


                toolbarTitle.setText(friendName);
                if (contactImage != null) {

                    Picasso.with(ChatMessageActivity.this).load(contactImage).into(imgProfile);
                } else {

                    Picasso.with(ChatMessageActivity.this).load(R.mipmap.ic_launcher).into(imgProfile);
                }


            }


            //


        } catch (Exception e) {
            e.printStackTrace();
        }


        allMessageList = new ArrayList<ChatMessageModel>();

        ArrayList<MessageDto> messageDtoArrayList = messageDao.getAllMessagesUser(senderOrGrpId);
        ChatMessageModel messageModel;
        for (int i = 0; i < messageDtoArrayList.size(); i++) {
            messageModel = new ChatMessageModel();
            messageModel.setMessage(messageDtoArrayList.get(i).getMessage());
            messageModel.setFrom(messageDtoArrayList.get(i).getFrom());
            messageModel.setUserName(messageDtoArrayList.get(i).getUserName());
            messageModel.setTypeMessage(messageDtoArrayList.get(i).getTypeMessage());
            messageModel.setDateTime(messageDtoArrayList.get(i).getDateTime());
            messageModel.setSenderId(messageDtoArrayList.get(i).getSenderId());

            allMessageList.add(messageModel);
        }


        adapter = new ChatMessageAdapter(getApplicationContext(), allMessageList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        lvChatMessages.setLayoutManager(layoutManager);
        lvChatMessages.setItemAnimator(new DefaultItemAnimator());
        lvChatMessages.setAdapter(adapter);
        if (allMessageList.size() != 0) {

            scrollToBottom();
        }


    }


    private void openSocket() {
        try {
            appController = new AppController();
            mSocket = appController.getSocket();
            mSocket.on("init", onInit);
            mSocket.on("message", onNewMessage);
            mSocket.connect();
            mSocket.emit("init", mSession.getPrefrenceString(Constants.USER_ID));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.ll_title, R.id.ll_btn_send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_title:

                Intent detailsIntent = new Intent(ChatMessageActivity.this, GroupDetailsActivity.class);
                detailsIntent.putExtra("id", senderOrGrpId);
                startActivity(detailsIntent);
                finish();

                break;

            case R.id.ll_btn_send:
                attemptSend();
                break;
        }


    }


    private Emitter.Listener onInit = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                }
            });
        }
    };

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject data = (JSONObject) args[0];

                        Log.e("data", data + "");
                        String userName = null;
                        String id = null;
                        String senderMessage = null;
                        String value = null;

                        friendsId = null;
                        userName = data.getString("userName");
                        senderMessage = data.getString("message");

                        id = data.getString("userId");
                    //    contactImage = data.getString("userImage");
                        friendsId = data.getString("userTo");

                        if (id.equals(senderOrGrpId)) {

                            addMessageToLocal(userName, senderMessage, "false", senderOrGrpId, "private");

                        } else {

                         //   addMessageOfOther(userName, senderMessage, "false", friendsId, "private");

                        }


                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }
            });
        }
    };


    private void attemptSend() {
        String message = editMsg.getText().toString();
        if (message.length() == 0) {
            Toast.makeText(ChatMessageActivity.this, "Please Enter Some message", Toast.LENGTH_SHORT).show();
        } else {
            try {
                if (typeMessageFrom.equals("private")) {
                    JSONObject jMessage = new JSONObject();
                    jMessage.put("userTo", senderOrGrpId);
                    jMessage.put("userId", mSession.getPrefrenceString(Constants.USER_ID));
                    jMessage.put("message", message);
                    jMessage.put("isMedia", false);
                    jMessage.put("isPrivate", true);
                    jMessage.put("userName", mSession.getPrefrenceString(Constants.USERNAME));

                  //  jMessage.put("userImage", contactImage);

                    if (mSocket.connected()) {
                        mSocket.emit("message", jMessage);
                        addMessageToLocal(friendName, message, "true", senderOrGrpId, "private");
                        editMsg.setText("");
                        Session.getUpdateRecentChat();
                    } else {
                        Toast.makeText(getApplicationContext(), "Connection Error !! ", Toast.LENGTH_SHORT).show();
                    }
                } else {

                    JSONObject jMessage = new JSONObject();
                    jMessage.put("userTo", senderOrGrpId);
                    jMessage.put("userId", mSession.getPrefrenceString(Constants.USER_ID));
                    jMessage.put("message", message);
                    jMessage.put("isMedia", false);
                    jMessage.put("isPrivate", false);
                    jMessage.put("userName", friendName);
                    if (mSocket.connected()) {
                        mSocket.emit("message", jMessage);
                        addMessageToLocal(friendName, message, "true", senderOrGrpId, "private");
                        editMsg.setText("");
                        Session.getUpdateRecentChat();
                    } else {
                        Toast.makeText(getApplicationContext(), "Connection Error !! ", Toast.LENGTH_SHORT).show();
                    }


                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    private void addMessageToLocal(String username, String message, String value, String friendid, String messageType) {

        MessageDto messageDto = new MessageDto();
        messageDto.setMessage(message);
        messageDto.setUserName(username);
        messageDto.setSenderId(friendid);
        messageDto.setDateTime("" + System.currentTimeMillis());
        messageDto.setFrom(value);
        messageDto.setTypeMessage(messageType);
        messageDao.insert(messageDto);

        ChatMessageModel chatMessageModel = new ChatMessageModel();
        chatMessageModel.setMessage(message);
        chatMessageModel.setFrom(value);
        chatMessageModel.setSenderId(friendid);
        chatMessageModel.setUserName(username);
        chatMessageModel.setDateTime("" + System.currentTimeMillis());
        chatMessageModel.setTypeMessage(messageType);
        allMessageList.add(chatMessageModel);


        RecentChatDto recentChatDto = new RecentChatDto();
        recentChatDto.setSenderId(friendid);
        recentChatDto.setProfileImage(contactImage);
        recentChatDto.setDateTime("" + System.currentTimeMillis());
        recentChatDto.setMessage(message);
        recentChatDto.setTypeMessage(messageType);
        recentChatDto.setFrom(value);
        recentChatDto.setUserName(username);


        recentChatDao.insert(recentChatDto);

        adapter.notifyItemInserted(allMessageList.size() - 1);
        scrollToBottom();

    }

    private void scrollToBottom() {
        lvChatMessages.scrollToPosition(adapter.getItemCount() - 1);
    }


    private void addMessageOfOther(String username, String message, String value, String friendid, String messageType) {

        MessageDto messageDto = new MessageDto();
        messageDto.setMessage(message);
        messageDto.setUserName(username);
        messageDto.setSenderId(friendid);
        messageDto.setDateTime("" + System.currentTimeMillis());
        messageDto.setFrom(value);
        messageDto.setTypeMessage(messageType);
        messageDao.insert(messageDto);


        RecentChatDto recentChatDto = new RecentChatDto();
        recentChatDto.setSenderId(friendid);
        recentChatDto.setProfileImage(contactImage);
        recentChatDto.setDateTime("" + System.currentTimeMillis());
        recentChatDto.setMessage(message);
        recentChatDto.setTypeMessage(messageType);
        recentChatDto.setFrom(value);
        recentChatDto.setUserName(username);


        recentChatDao.insert(recentChatDto);

        Session.getUpdateRecentChat();

    }
}
