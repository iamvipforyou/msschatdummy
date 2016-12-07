package com.mss.msschat.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mss.msschat.Adapters.ChatMessageAdapter;
import com.mss.msschat.AppUtils.AppController;
import com.mss.msschat.AppUtils.AppPreferences;
import com.mss.msschat.AppUtils.Constants;
import com.mss.msschat.AppUtils.Session;
import com.mss.msschat.AppUtils.UserPicture;
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

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
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
    private LinearLayout llTakePic;
    private LinearLayout llChoosePic;
    private AppPreferences mSession;
    String friendsId;
    private String contactImage;
    private String typeMessageFrom;
    private Bitmap imgBitmap;
    private String userImage;
    private String imagePath;
    /////////////////////////////
    String userName = null;
    String id = null;
    String senderMessage = null;
    String value = null;
    String typeMessage = null;

    ///////////////////////

    String gUserName = null;
    String gId = null;
    String gSenderMessage = null;
    String gTypeMessage = null;


    ////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_message_activity);
        ButterKnife.bind(this);
        initUI();
    }

    private void initUI() {

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ll_txtTitles = (LinearLayout) findViewById(R.id.ll_title);
        mSession = new AppPreferences(ChatMessageActivity.this);
        messageDao = new MessageDao(ChatMessageActivity.this);
        openSocket();
        populateUI();
    }

    private void populateUI() {

      /*  toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/
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
            mSocket.on("groupMsg", onGroupMessage);
            mSocket.connect();
            mSocket.emit("init", mSession.getPrefrenceString(Constants.USER_ID));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.ll_title, R.id.ll_btn_send, R.id.ibtn_back, R.id.ll_back, R.id.ll_camera})
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

            case R.id.ibtn_back:

                finish();

                break;
            case R.id.ll_back:
                finish();
                break;
            case R.id.ll_camera:

                changeProfile();
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
                       /* String userName = null;
                        String id = null;
                        String senderMessage = null;
                        String value = null;
                        String typeMessage = null;*/
                        friendsId = null;
                        userName = data.getString("userName");
                        senderMessage = data.getString("message");

                        id = data.getString("userId");
                        contactImage = data.getString("profilePic");
                        friendsId = data.getString("userTo");
                        typeMessage = data.getString("typeMessage");


                        if (id.equals(senderOrGrpId)) {

                            if (senderMessage.contains("http://mastersoftwaretechnologies")) {

                                new DownloadImageFromURl(new TaskListener() {
                                    @Override
                                    public void onFinished(String result) {


                                        addMessageToLocal(userName, "@picPath>" + result, "false", senderOrGrpId, typeMessage);


                                    }
                                }).execute(senderMessage);


                            } else {


                                addMessageToLocal(userName, senderMessage, "false", senderOrGrpId, typeMessage);
                            }


                        } else {

                            //   addMessageOfOther(userName, senderMessage, "false", friendsId, "private");


                            if (senderMessage.contains("http://mastersoftwaretechnologies")) {

                                new DownloadImageFromURl(new TaskListener() {
                                    @Override
                                    public void onFinished(String result) {


                                        addMessageOfOther(userName, "@picPath>" + result, "false", senderOrGrpId, typeMessage);


                                    }
                                }).execute(senderMessage);


                            } else {

                                addMessageOfOther(userName, senderMessage, "false", friendsId, typeMessage);

                            }

                        }


                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }
            });
        }
    };


    private Emitter.Listener onGroupMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    try {
                        JSONObject data = (JSONObject) args[0];

                        Log.e("data", data + "");

                        friendsId = null;
                        gUserName = data.getString("userName");
                        gSenderMessage = data.getString("message");

                        gId = data.getString("groupId");
                        contactImage = data.getString("profilePic");
                        gTypeMessage = data.getString("typeMessage");


                        if (gId.equals(senderOrGrpId)) {


                            if (gSenderMessage.contains("http://mastersoftwaretechnologies")) {

                                new DownloadImageFromURl(new TaskListener() {
                                    @Override
                                    public void onFinished(String result) {


                                        addMessageToLocal(gUserName, "@picPath>" + result, "false", senderOrGrpId, gTypeMessage);

                                    }
                                }).execute(senderMessage);


                            } else {
                                addMessageToLocal(gUserName, gSenderMessage, "false", senderOrGrpId, gTypeMessage);

                            }


                        } else {


                            if (gSenderMessage.contains("http://mastersoftwaretechnologies")) {

                                new DownloadImageFromURl(new TaskListener() {
                                    @Override
                                    public void onFinished(String result) {


                                        addMessageOfOther(gUserName, "@picPath>" + result, "false", senderOrGrpId, gTypeMessage);

                                    }
                                }).execute(senderMessage);


                            } else {
                                addMessageOfOther(gUserName, gSenderMessage, "false", senderOrGrpId, gTypeMessage);

                            }


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
                        addMessageToLocal(friendName, message, "true", senderOrGrpId, "group");
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

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mSocket.disconnect();
        mSocket.off("init", onInit);
        mSocket.off("message", onNewMessage);
        mSocket.off("groupMsg", onGroupMessage);


    }


    public void changeProfile() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_change_picture);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER;
        wmlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wmlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        llTakePic = (LinearLayout) dialog.findViewById(R.id.ll_take_pic);
        llChoosePic = (LinearLayout) dialog.findViewById(R.id.ll_choose_pic);

        llTakePic.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 2);
                dialog.dismiss();
            }
        });
        llChoosePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryImageIntent, 3);
                dialog.dismiss();

            }
        });
        dialog.show();
    }

    //////////////////////////////////////////
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            if (requestCode == 2) {


                try {
                    imgBitmap = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    imgBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    Uri temUri = getImageUri(ChatMessageActivity.this, imgBitmap);
                    //   ivGrpImage.setImageBitmap(new UserPicture(temUri, getContentResolver()).getBitmap());
                    imagePath = Utils.getAllImageAndVideoFilePath(this, temUri);
                    userImage = "data:image/png;base64," + BitMapToString(new UserPicture(temUri, getContentResolver()).getBitmap());
                    openSocket();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            attemptPicSend();
                        }
                    }, 1000);

                } catch (Exception e) {
                    e.printStackTrace();
                }


            } else if (requestCode == 3) {

                try {
                    Uri selectedImageUri = data.getData();
                    imgBitmap = new UserPicture(selectedImageUri, getContentResolver()).getBitmap();
                    //     ivGrpImage.setImageBitmap(new UserPicture(selectedImageUri, getContentResolver()).getBitmap());

                    imagePath = Utils.getAllImageAndVideoFilePath(this, selectedImageUri);
                    userImage = "data:image/png;base64," + BitMapToString(new UserPicture(selectedImageUri, getContentResolver()).getBitmap());
                    openSocket();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            attemptPicSend();
                        }
                    }, 1000);


                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        }
    }

    private void attemptPicSend() {

        try {
            if (typeMessageFrom.equals("private")) {
                JSONObject jMessage = new JSONObject();
                jMessage.put("userTo", senderOrGrpId);
                jMessage.put("userId", mSession.getPrefrenceString(Constants.USER_ID));
                jMessage.put("message", userImage);
                jMessage.put("isMedia", true);
                jMessage.put("isPrivate", true);
                jMessage.put("userName", mSession.getPrefrenceString(Constants.USERNAME));

                //  jMessage.put("userImage", contactImage);

                if (mSocket.connected()) {
                    mSocket.emit("message", jMessage);
                    addMessageToLocal(friendName, "@picPath>" + imagePath, "true", senderOrGrpId, "private");
                    editMsg.setText("");
                    Session.getUpdateRecentChat();
                } else {
                    Toast.makeText(getApplicationContext(), "Connection Error !! ", Toast.LENGTH_SHORT).show();
                }
            } else {

                JSONObject jMessage = new JSONObject();
                jMessage.put("userTo", senderOrGrpId);
                jMessage.put("userId", mSession.getPrefrenceString(Constants.USER_ID));
                jMessage.put("message", userImage);
                jMessage.put("isMedia", true);
                jMessage.put("isPrivate", false);
                jMessage.put("userName", friendName);
                if (mSocket.connected()) {
                    mSocket.emit("message", jMessage);
                    addMessageToLocal(friendName, "@picPath>" + imagePath, "true", senderOrGrpId, "group");
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

    ////////////////////////////////////////

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = null;
        try {
            System.gc();
            temp = Base64.encodeToString(b, Base64.NO_WRAP);
        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            b = baos.toByteArray();
            temp = Base64.encodeToString(b, Base64.NO_WRAP);
            Log.e("EWN", "Out of memory error catched");
        }
        return temp;
    }


    @Override
    protected void onStop() {
        super.onStop();

        mSocket.disconnect();
        mSocket.off("init", onInit);
        mSocket.off("message", onNewMessage);
        mSocket.off("groupMsg", onGroupMessage);

    }


    @Override
    protected void onPause() {
        super.onPause();
        mSocket.disconnect();
        mSocket.off("init", onInit);
        mSocket.off("message", onNewMessage);
        mSocket.off("groupMsg", onGroupMessage);


    }


    public interface TaskListener {
        public void onFinished(String result);
    }

    public class DownloadImageFromURl extends AsyncTask<String, String, String> {
        private final TaskListener taskListener;
        ProgressBar mProgressBar;

        public DownloadImageFromURl(TaskListener listner) {


            taskListener = listner;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


//            showDialog(progress_bar_type);

        }

        @Override
        protected String doInBackground(final String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0].toString());
                URLConnection conection = url.openConnection();
                conection.connect();
                int lenghtOfFile = conection.getContentLength();
                InputStream input = new BufferedInputStream(url.openStream(), 8192);
                //    long TotalCount = new AppPreferences(ChatMessageActivity.this).getPrefrenceLong("countDownload");
                new AppPreferences(ChatMessageActivity.this).setPrefrenceLong("countDownload", System.currentTimeMillis());
                OutputStream output = new FileOutputStream("/sdcard/.MssChat/attachments" + new AppPreferences(ChatMessageActivity.this).getPrefrenceLong("countDownload") + ".jpg");
                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                    // writing data to file
                    output.write(data, 0, count);
                }
                // flushing output
                output.flush();
                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                e.printStackTrace();
//                Log.e("Error: ", e.getMessage());
            }

            String result = Environment.getExternalStorageDirectory().toString() + "/.MssChat/attachments" + new AppPreferences(ChatMessageActivity.this).getPrefrenceLong("countDownload") + ".jpg";

            return result;
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
//            pDialog.setProgress(Integer.parseInt(progress[0]));
        }


        @Override
        protected void onPostExecute(String file_url) {


            if (this.taskListener != null) {

                // And if it is we call the callback function on it.
                this.taskListener.onFinished(file_url);
            }
            //     pdfView.fromFile(new File(Environment.getExternalStorageDirectory().toString() + "/pdf/hhhh.pdf")).defaultPage(1).onPageChange(PresonalizeMedicalProfileActivity.this).load();


        }


    }


}
