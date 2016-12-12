package com.mss.msschat.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mss.msschat.AppUtils.ApiClient;
import com.mss.msschat.AppUtils.AppPreferences;
import com.mss.msschat.AppUtils.Constants;
import com.mss.msschat.AppUtils.Session;
import com.mss.msschat.AppUtils.UserPicture;
import com.mss.msschat.AppUtils.Utils;
import com.mss.msschat.DataBase.Dao.MessageDao;
import com.mss.msschat.DataBase.Dao.RecentChatDao;
import com.mss.msschat.DataBase.Dto.MessageDto;
import com.mss.msschat.DataBase.Dto.RecentChatDto;
import com.mss.msschat.Interfaces.ApiInterface;
import com.mss.msschat.Models.CreateGroupModel;
import com.mss.msschat.Models.CreateGroupResponse;
import com.mss.msschat.Models.GroupResponseData;
import com.mss.msschat.R;

import java.io.ByteArrayOutputStream;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mss on 30/11/16.
 */

public class CreateGroupActivity extends AppCompatActivity {


    public ViewGroup viewGroup;
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
    @Bind(R.id.view)
    View view;
    @Bind(R.id.img_profile)
    CircleImageView imgProfile;
    @Bind(R.id.ll_profile)
    LinearLayout llProfile;
    @Bind(R.id.edit_grp_name)
    EditText editGrpName;
    @Bind(R.id.card_view)
    CardView cardView;
    @Bind(R.id.btn_signup)
    FloatingActionButton btnSignup;
    @Bind(R.id.ll_pic)
    ImageView llPic;
    @Bind(R.id.ll_sel_pic)
    LinearLayout llSelPic;
    @Bind(R.id.card_view3)
    CardView cardView3;
    private LinearLayout llTakePic;
    private LinearLayout llChoosePic;
    private Bitmap imgBitmap;
    public String userImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);
        ButterKnife.bind(this);
        initUI();
    }

    private void initUI() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        populateUI();
    }

    private void populateUI() {

        Utils.setClassTitle(CreateGroupActivity.this, "New Group", toolbar);
        Utils.setStatusBarColor(CreateGroupActivity.this);
        viewGroup = (ViewGroup) ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);

        llBack.setVisibility(View.VISIBLE);
        ibtnBack.setImageResource(R.mipmap.ic_keyboard_backspace_white_24dp);
        ibtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }


    public void createGroupOnServer() {

        CreateGroupModel createGroupModel = new CreateGroupModel();
        createGroupModel.setName(editGrpName.getText().toString());
        createGroupModel.setAdminId(new AppPreferences(CreateGroupActivity.this).getPrefrenceString(Constants.USER_ID));
        createGroupModel.setGroupPic(userImage);

        Utils.dialog(CreateGroupActivity.this);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<CreateGroupResponse> getGroupDetails = apiService.getCreateGroupDetails(createGroupModel);
        getGroupDetails.enqueue(new Callback<CreateGroupResponse>() {
            @Override
            public void onResponse(Call<CreateGroupResponse> call, Response<CreateGroupResponse> response) {

                try {
                    if (response.isSuccessful()) {
                        Utils.dismissProgressDialog();
                        CreateGroupResponse createGroupResponse = response.body();
                        int statusCode = createGroupResponse.getStatus();
                        if (statusCode == Constants.SUCCESSFUL) {
                            GroupResponseData responseData = createGroupResponse.getData();

                            addDataToLocal(responseData);


                            Intent addMemberIntent = new Intent(CreateGroupActivity.this, AddGroupMembersActivity.class);
                            addMemberIntent.putExtra("groupId", responseData.getId());
                            startActivity(addMemberIntent);
                            finish();
                        }
                    } else {
                        Utils.dismissProgressDialog();


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Utils.dismissProgressDialog();
                    Utils.showErrorOnTop(viewGroup, "Server Error !!");
                }

            }

            @Override
            public void onFailure(Call<CreateGroupResponse> call, Throwable t) {
                Utils.dismissProgressDialog();
            }
        });


    }

    private void addDataToLocal(GroupResponseData responseData) {

        MessageDao messageDao = new MessageDao(CreateGroupActivity.this);
        MessageDto messageDto = new MessageDto();

        messageDto.setUserName(responseData.getName());
        messageDto.setTypeMessage("group");
        messageDto.setSenderId(responseData.getId());
        messageDto.setMessage("");
        messageDto.setFrom("false");
        messageDto.setDateTime("" + System.currentTimeMillis());
        messageDao.insert(messageDto);

        RecentChatDao recentChatDao = new RecentChatDao(CreateGroupActivity.this);

        RecentChatDto recentChatDto = new RecentChatDto();

        recentChatDto.setSenderId(responseData.getId());
        recentChatDto.setDateTime("" + System.currentTimeMillis());
        recentChatDto.setFrom("false");
        recentChatDto.setUserName(responseData.getName());
        recentChatDto.setMessage("");
        recentChatDto.setTypeMessage("group");
        recentChatDto.setProfileImage(responseData.getGroupPic());
        recentChatDto.setMessageCount("0");
        recentChatDao.insert(recentChatDto);


        List<MessageDto> dataCheckList = messageDao.getAllMessages();
        List<RecentChatDto> dataAllRecentChat = recentChatDao.getAllRecentMessages();

        Session.getUpdateRecentChat();

    }

    @OnClick({R.id.btn_signup, R.id.ll_pic})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_signup:
                if (validation()) {

                    createGroupOnServer();
                    //      startActivity(new Intent(CreateGroupActivity.this, GroupDetailsActivity.class));
                }
                break;
            case R.id.ll_pic:
                selectProfile();

                break;

        }

    }

    private boolean validation() {
        if (TextUtils.isEmpty(editGrpName.getText().toString().trim())) {
            Utils.showErrorOnTop(viewGroup, "Please Enter Name");
            return false;
        } else {
            return true;
        }

    }

    private void selectProfile() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_select_picture);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 2) {
                try {
                    imgBitmap = (Bitmap) data.getExtras().get("data");
                    Uri temUri = getImageUri(CreateGroupActivity.this, imgBitmap);
                    imgProfile.setImageBitmap(new UserPicture(temUri, getContentResolver()).getBitmap());
                    userImage = "data:image/png;base64," + BitMapToString(new UserPicture(temUri, getContentResolver()).getBitmap());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 3) {
                try {
                    Uri selectedImageUri = data.getData();
                    imgProfile.setImageBitmap(new UserPicture(selectedImageUri, getContentResolver()).getBitmap());
                    userImage = "data:image/png;base64," + BitMapToString(new UserPicture(selectedImageUri, getContentResolver()).getBitmap());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

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
}
