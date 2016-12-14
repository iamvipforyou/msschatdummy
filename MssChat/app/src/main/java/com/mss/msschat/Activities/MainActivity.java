package com.mss.msschat.Activities;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.mss.msschat.AppUtils.AppController;
import com.mss.msschat.AppUtils.AppPreferences;
import com.mss.msschat.AppUtils.Connectivity;
import com.mss.msschat.AppUtils.Constants;
import com.mss.msschat.AppUtils.Session;
import com.mss.msschat.AppUtils.Utils;
import com.mss.msschat.DataBase.Dao.ContactsDao;
import com.mss.msschat.DataBase.Dao.MessageDao;
import com.mss.msschat.DataBase.Dao.RecentChatDao;
import com.mss.msschat.DataBase.Dto.MessageDto;
import com.mss.msschat.DataBase.Dto.RecentChatDto;
import com.mss.msschat.Fragments.ContactChatFragment;
import com.mss.msschat.Fragments.RecentChatFragment;
import com.mss.msschat.NotificationUtils.RegistrationIntentService;
import com.mss.msschat.R;
import com.mss.msschat.Services.ContactFirstSyncIntentService;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    public Toolbar mToolBar;
    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;
    private ViewGroup viewGroup;
    AppController appController;
    private Socket mSocket;
    private int mId;
    ////////////////////////
/////////////////////////////
    String friendsId;
    String userName = null;
    String id = null;
    String senderMessage = null;
    String value = null;
    String typeMessage = null;
    private int notificationCount;
    String onMessagePicture;
    String onGrpMessagePicture;
    ///////////////////////
    String gUserName = null;
    String gId = null;
    String gSenderMessage = null;
    String gTypeMessage = null;
    String gFromUser = null;
    private static final int TYPING_TIMER_LENGTH = 600;
    ////////////

    ///
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Utils.setStatusBarColor(MainActivity.this);
        initUI();
    }

    public void initUI() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        viewGroup = (ViewGroup) ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);
        if (checkPlayServices()) {
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }

        openSocket();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new RecentChatFragment(), "RECENT");
        adapter.addFragment(new ContactChatFragment(), "FRIENDS");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String newText) {
                    Log.i("onQueryTextChange", newText);

                    String userName = newText;

                    Session.getSearchRecent(userName);
                    Session.getSearchContacts(userName);


                    return true;
                }

                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.i("onQueryTextSubmit", query);

                    return true;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
        }

        /* MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Set styles for expanded state here
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                }
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Set styles for collapsed state here
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.BLUE));
                }
                return true;
            }
        });*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:

                return false;
            case R.id.action_group:
                startActivity(new Intent(MainActivity.this, CreateGroupActivity.class));
                break;

            case R.id.action_setting:
                //   Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show();
                if (Connectivity.isConnected(MainActivity.this)) {
                    ContactsDao contactsDao = new ContactsDao(MainActivity.this);
                    contactsDao.clearTable(MainActivity.this);

                    startService(new Intent(MainActivity.this, ContactFirstSyncIntentService.class));
                } else {
                    Utils.showErrorOnTop(viewGroup, "No Internet Connectivity !!");
                }
                break;
            case R.id.action_profile:

                Intent detailsIntent = new Intent(MainActivity.this, FriendProfileActivity.class);
                detailsIntent.putExtra("userId", new AppPreferences(MainActivity.this).getPrefrenceString(Constants.USER_ID));
                startActivity(detailsIntent);


                break;

            default:
                break;
        }
        searchView.setOnQueryTextListener(queryTextListener);
        return super.onOptionsItemSelected(item);
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, 12).show();
            } else {
                Log.i("Error", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    private void openSocket() {
        try {
            //   appController = new AppController();
            //   mSocket = appController.getSocket();


            mSocket = IO.socket(Constants.SOCKET_URL);


            mSocket.on("message", onNewMessage);
            mSocket.on("groupMsg", onGroupMessage);
            mSocket.connect();
            mSocket.emit("init", new AppPreferences(MainActivity.this).getPrefrenceString(Constants.USER_ID));

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();

        mSocket.off("message", onNewMessage);
        mSocket.off("groupMsg", onGroupMessage);
        //  mSocket.disconnect();
    }

    @Override
    protected void onStop() {
        super.onStop();

        //   mSocket.off("message", onNewMessage);
        //   mSocket.off("groupMsg", onGroupMessage);
        //    mSocket.disconnect();
    }


    @Override
    protected void onResume() {
        super.onResume();


        //   mSocket.on("message", onNewMessage);
        //    mSocket.on("groupMsg", onGroupMessage);


    }


    @Override
    protected void onRestart() {
        super.onRestart();

        mSocket.on("message", onNewMessage);
        mSocket.on("groupMsg", onGroupMessage);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSocket.emit("offline", new AppPreferences(MainActivity.this).getPrefrenceString(Constants.USER_ID));
        mSocket.off("message", onNewMessage);
        mSocket.off("groupMsg", onGroupMessage);
        mSocket.disconnect();

    }


    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject data = (JSONObject) args[0];
                        Log.e("data", data + "");
                        friendsId = null;
                        userName = data.getString("userName");
                        senderMessage = data.getString("message");
                        id = data.getString("userId");
                        onMessagePicture = data.getString("profilePic");
                        friendsId = data.getString("userTo");
                        typeMessage = data.getString("typeMessage");

                        //   addMessageOfOther(userName, senderMessage, "false", friendsId, "private");
                        if (senderMessage.contains("http://mastersoftwaretechnologies")) {
                            new DownloadImageFromURl(new TaskListener() {
                                @Override
                                public void onFinished(String result) {
                                    addMessageOfOther(userName, "@picPath>" + result, "false", id, typeMessage, onMessagePicture);
                                    createNotification(userName, "Image");
                                }
                            }).execute(senderMessage);
                        } else {
                            addMessageOfOther(userName, senderMessage, "false", id, typeMessage, onMessagePicture);
                            createNotification(userName, senderMessage);
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
                        onGrpMessagePicture = data.getString("profilePic");
                        gTypeMessage = data.getString("typeMessage");
                        gFromUser = data.getString("fromuser");
                        if (gSenderMessage.contains("http://mastersoftwaretechnologies")) {
                            new DownloadImageFromURl(new TaskListener() {
                                @Override
                                public void onFinished(String result) {
                                    addMessageOfOther(gUserName, "@picPath>" + result, "false", gId, gTypeMessage, onGrpMessagePicture);
                                    createNotification(gUserName, "Image");
                                }
                            }).execute(gSenderMessage);
                        } else {
                            addMessageOfOther(gUserName, gFromUser + " says:- " + gSenderMessage, "false", gId, gTypeMessage, onGrpMessagePicture);
                            createNotification(gUserName, gSenderMessage);
                        }
                        //   addMessageOfOther(userName, senderMessage, "false", friendsId, "private");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    private void addMessageOfOther(String username, String message, String value, String friendid, String messageType, String contactImage) {
        try {
            MessageDao messageDao = new MessageDao(MainActivity.this);
            RecentChatDao recentChatDao = new RecentChatDao(MainActivity.this);


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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createNotification(String userName, String notification) {
        Bitmap bmp = BitmapFactory.decodeResource((MainActivity.this).getResources(), R.mipmap.notification);
        NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(MainActivity.this).setLargeIcon(bmp).setSmallIcon(R.mipmap.notification).setContentTitle(userName).setContentText(notification).setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        Intent resultIntent = new Intent(this, MainActivity.class);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(MainActivity.this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mBuilder.setAutoCancel(true);
        mNotificationManager.notify(mId, mBuilder.build());
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
                new AppPreferences(MainActivity.this).setPrefrenceLong("countDownload", System.currentTimeMillis());
                OutputStream output = new FileOutputStream("/sdcard/.MssChat/attachments" + new AppPreferences(MainActivity.this).getPrefrenceLong("countDownload") + ".jpg");
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

            String result = Environment.getExternalStorageDirectory().toString() + "/.MssChat/attachments" + new AppPreferences(MainActivity.this).getPrefrenceLong("countDownload") + ".jpg";

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
