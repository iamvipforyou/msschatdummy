package com.mss.msschat.DataBase.Dao;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.mss.msschat.AppUtils.Constants;
import com.mss.msschat.DataBase.DatabaseHelper;
import com.mss.msschat.DataBase.Dto.MessageDto;
import com.mss.msschat.DataBase.Dto.RecentChatDto;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mss on 1/12/16.
 */

public class RecentChatDao extends Dao<RecentChatDto> {


    Context mContext;

    public RecentChatDao(Context context) {
        super(Constants.DataBaseParms.RECENT_CHAT_MESSAGES);

        this.mContext = context;

    }

    @Override
    public void insert(RecentChatDto dto) {

        insertOrReplace(dto, "rSenderId", dto.getSenderId());

    }

    @Override
    public void replace(RecentChatDto dto) {

    }

    @Override
    public void delete(String id) {

        deletebyId(id, "rSenderId", mContext);

    }

    @Override
    public RecentChatDto buildDto(JSONObject json) throws JSONException {
        return null;
    }

    @Override
    protected RecentChatDto buildDto(Cursor cursor) {


        RecentChatDto recentChatDto = new RecentChatDto();


        recentChatDto.setMessage(cursor.getString(cursor.getColumnIndexOrThrow("rMessage")));
        recentChatDto.setUserName(cursor.getString(cursor.getColumnIndexOrThrow("rUserName")));
        recentChatDto.setFrom(cursor.getString(cursor.getColumnIndexOrThrow("rFrom")));
        recentChatDto.setSenderId(cursor.getString(cursor.getColumnIndexOrThrow("rSenderId")));
        recentChatDto.setDateTime(cursor.getString(cursor.getColumnIndexOrThrow("rDateTime")));
        recentChatDto.setTypeMessage(cursor.getString(cursor.getColumnIndexOrThrow("rTypeMessage")));
        recentChatDto.setProfileImage(cursor.getString(cursor.getColumnIndexOrThrow("rImage")));

        return recentChatDto;
    }

    @Override
    protected List<RecentChatDto> listDirtyDto(String field) {
        return null;
    }


    public ArrayList<RecentChatDto> getAllRecentMessages() {
        ArrayList<RecentChatDto> messageList = new ArrayList<RecentChatDto>();
        try {

            String sql = "select  * from recentChatMessages" + ";";
            DatabaseHelper dbHelper = new DatabaseHelper(mContext);
            Cursor cursor = dbHelper.getReadableDatabase().rawQuery(sql, null);
            while (cursor.moveToNext()) {
                try {
                    RecentChatDto dto = buildDto(cursor);
                    if (dto != null) {
                        messageList.add(dto);
                    }
                } catch (Exception e) {
                    Log.e(sql, "field not found", e);
                }
            }
            cursor.close();
            dbHelper.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return messageList;
    }


    public List<RecentChatDto> getRecentListFriendId(String friendId) {
        List<RecentChatDto> messageList = new ArrayList<RecentChatDto>();
        try {
            String sql = "select  * from  recentChatMessages  where rSenderId = '" + friendId + "' ;";
            DatabaseHelper dbHelper = new DatabaseHelper(mContext);
            Cursor cursor = dbHelper.getReadableDatabase().rawQuery(sql, null);
            while (cursor.moveToNext()) {
                try {
                    RecentChatDto dto = buildDto(cursor);
                    if (dto != null) {
                        messageList.add(dto);
                    }
                } catch (Exception e) {
                    Log.e(sql, "field not found", e);
                }
            }
            cursor.close();
            dbHelper.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return messageList;
    }


}
