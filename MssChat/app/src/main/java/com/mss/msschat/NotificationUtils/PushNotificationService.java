package com.mss.msschat.NotificationUtils;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v7.app.NotificationCompat;
import android.util.Log;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mss.msschat.AppUtils.Session;
import com.mss.msschat.DataBase.Dao.MessageDao;
import com.mss.msschat.DataBase.Dao.RecentChatDao;
import com.mss.msschat.DataBase.Dto.MessageDto;
import com.mss.msschat.DataBase.Dto.RecentChatDto;
import com.mss.msschat.Models.ChatMessageModel;
import com.mss.msschat.R;

import org.json.JSONObject;

import java.util.Map;

public class PushNotificationService extends FirebaseMessagingService {

    private int mId;
    int count;
    int messageCount = 0;
    private int notificationCount;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        try {
            String notificationDataString = remoteMessage.getData().get("message");
            JSONObject notificationDataObj = new JSONObject(notificationDataString);

            String profilePic = notificationDataObj.getString("profilePic");
            String userMessage = notificationDataObj.getString("message");
            String userName = notificationDataObj.getString("userName");
            String senderId = notificationDataObj.getString("userId");
            String messageType = notificationDataObj.getString("typeMessage");

            addMessageToLocal(userName, userMessage, "false", senderId, messageType, profilePic);
            createNotification(userMessage);


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    // Creates com.mss.firebasenotificationdemo.notification based on title and body received
    private void createNotification(String notification) {
        Context context = getBaseContext();
        NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(context).setSmallIcon(R.mipmap.ic_launcher).setContentTitle("MssChat").setContentText(notification);

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());
    }


    private void addMessageToLocal(String username, String message, String value, String friendid, String messageType, String contactImage) {

        MessageDao messageDao = new MessageDao(PushNotificationService.this);

        MessageDto messageDto = new MessageDto();
        messageDto.setMessage(message);
        messageDto.setUserName(username);
        messageDto.setSenderId(friendid);
        messageDto.setDateTime("" + System.currentTimeMillis());
        messageDto.setFrom(value);
        messageDto.setTypeMessage(messageType);
        messageDao.insert(messageDto);


        RecentChatDao recentChatDao = new RecentChatDao(PushNotificationService.this);

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