package com.mss.msschat.NotificationUtils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.ProgressBar;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mss.msschat.Activities.ChatMessageActivity;
import com.mss.msschat.Activities.ConfirmPatternActivity;
import com.mss.msschat.Activities.MainActivity;
import com.mss.msschat.AppUtils.AppPreferences;
import com.mss.msschat.AppUtils.Constants;
import com.mss.msschat.AppUtils.Session;
import com.mss.msschat.AppUtils.Utils;
import com.mss.msschat.DataBase.Dao.MessageDao;
import com.mss.msschat.DataBase.Dao.RecentChatDao;
import com.mss.msschat.DataBase.Dto.MessageDto;
import com.mss.msschat.DataBase.Dto.RecentChatDto;
import com.mss.msschat.Models.ChatMessageModel;
import com.mss.msschat.R;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

public class PushNotificationService extends FirebaseMessagingService {

    private int mId;
    int count;
    int messageCount = 0;
    private int notificationCount;

    String profilePic;
    String userMessage;
    String userName;
    String senderId;
    String messageType;
    String gFromUser;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        try {
            String notificationDataString = remoteMessage.getData().get("message");
            JSONObject notificationDataObj = new JSONObject(notificationDataString);

            messageType = notificationDataObj.getString("typeMessage");
            if (messageType.equals(Constants.PRIVATE_TYPE)) {


                profilePic = notificationDataObj.getString("profilePic");
                userMessage = notificationDataObj.getString("message");
                userName = notificationDataObj.getString("userName");
                senderId = notificationDataObj.getString("userId");

                createNotification(userMessage);
                addMessageToLocal(userName, userMessage, "false", senderId, messageType, profilePic);


            } else if (messageType.equals(Constants.GROUP_TYPE)) {


                profilePic = notificationDataObj.getString("profilePic");
                userMessage = notificationDataObj.getString("message");
                userName = notificationDataObj.getString("userName");
                senderId = notificationDataObj.getString("groupId");
                gFromUser = notificationDataObj.getString("fromuser");
                createNotification(userMessage);
                if (userMessage.contains("http://mastersoftwaretechnologies")) {
                    addMessageToLocal(userName, userMessage, "false", senderId, messageType, profilePic);
                } else {
                    addMessageToLocal(userName, gFromUser + " says:- " + userMessage, "false", senderId, messageType, profilePic);

                }
            } else {


            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    // Creates com.mss.firebasenotificationdemo.notification based on title and body received
    private void createNotification(String notification) {

        Bitmap bmp = BitmapFactory.decodeResource((PushNotificationService.this).getResources(), R.mipmap.notification);
       /* Context context = getBaseContext();
        NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(context).setLargeIcon(bmp).setContentTitle("MssChat").setContentText(notification);

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());*/


        NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(PushNotificationService.this).setLargeIcon(bmp).setSmallIcon(getNotificationIcon()).setContentTitle(userName).setContentText(notification).setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));


        Intent resultIntent = new Intent(this, ConfirmPatternActivity.class);


        resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(PushNotificationService.this);

        stackBuilder.addParentStack(ConfirmPatternActivity.class);

        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mBuilder.setAutoCancel(true);
        mNotificationManager.notify(mId, mBuilder.build());


    }


    private void addMessageToLocal(final String username, final String message, final String value, final String friendid, final String messageType, final String contactImage) {


        if (message.contains("http://mastersoftwaretechnologies")) {


            new DownloadImageFromURl(new ChatMessageActivity.TaskListener() {
                @Override
                public void onFinished(String result) {


                    MessageDao messageDao = new MessageDao(PushNotificationService.this);

                    MessageDto messageDto = new MessageDto();
                    messageDto.setMessage("@picPath>" + result);
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
                    recentChatDto.setMessage("@picPath>" + result);
                    recentChatDto.setTypeMessage(messageType);
                    recentChatDto.setFrom(value);
                    recentChatDto.setUserName(username);


                    recentChatDao.insert(recentChatDto);

                    Session.getUpdateRecentChat();


                }
            }).execute(message);


        } else {


            MessageDao messageDao = new MessageDao(PushNotificationService.this);
            RecentChatDao recentChatDao = new RecentChatDao(PushNotificationService.this);
            MessageDto messageDto = new MessageDto();
            messageDto.setMessage(message);
            messageDto.setUserName(username);
            messageDto.setSenderId(friendid);
            messageDto.setDateTime("" + System.currentTimeMillis());
            messageDto.setFrom(value);
            messageDto.setTypeMessage(messageType);
            messageDao.insert(messageDto);


            List<RecentChatDto> list1 = recentChatDao.getRecentListFriendId(friendid);
            if (list1.size() > 0) {
                notificationCount = Integer.parseInt(list1.get(0).getMessageCount());
                notificationCount = notificationCount + 1;
            } else {
                notificationCount = 0;
            }


            RecentChatDto recentChatDto = new RecentChatDto();
            recentChatDto.setSenderId(friendid);
            recentChatDto.setProfileImage(contactImage);
            recentChatDto.setDateTime("" + System.currentTimeMillis());
            recentChatDto.setMessage(message);
            recentChatDto.setTypeMessage(messageType);
            recentChatDto.setFrom(value);
            recentChatDto.setUserName(username);
            recentChatDto.setMessageCount("" + notificationCount);

            recentChatDao.insert(recentChatDto);

            Session.getUpdateRecentChat();


        }

    }


    public interface TaskListener {
        public void onFinished(String result);
    }

    public class DownloadImageFromURl extends AsyncTask<String, String, String> {
        private final ChatMessageActivity.TaskListener taskListener;
        ProgressBar mProgressBar;

        public DownloadImageFromURl(ChatMessageActivity.TaskListener listner) {


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
                new AppPreferences(PushNotificationService.this).setPrefrenceLong("countDownload", System.currentTimeMillis());
                OutputStream output = new FileOutputStream("/sdcard/.MssChat/attachments" + new AppPreferences(PushNotificationService.this).getPrefrenceLong("countDownload") + ".jpg");
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

            String result = Environment.getExternalStorageDirectory().toString() + "/.MssChat/attachments" + new AppPreferences(PushNotificationService.this).getPrefrenceLong("countDownload") + ".jpg";

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

    private int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.mipmap.notification : R.mipmap.notification;
    }

}