package com.mss.msschat.DataBase.Dao;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.mss.msschat.AppUtils.Constants;
import com.mss.msschat.DataBase.DatabaseHelper;
import com.mss.msschat.DataBase.Dto.ContactsDto;
import com.mss.msschat.DataBase.Dto.MessageDto;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mss on 1/12/16.
 */

public class MessageDao extends Dao<MessageDto> {


    Context mContext;


    public MessageDao(Context context) {
        super(Constants.DataBaseParms.MESSAGE_TABLE);

        this.mContext = context;


    }

    @Override
    public void insert(MessageDto dto) {

        insertOrReplace(dto, "messageId", dto.getMessageId(),mContext);

    }

    @Override
    public void replace(MessageDto dto) {

    }

    @Override
    public void delete(String id) {


        deletebyId(id, "messageId", mContext);

    }

    @Override
    public MessageDto buildDto(JSONObject json) throws JSONException {
        return null;
    }

    @Override
    protected MessageDto buildDto(Cursor cursor) {

        MessageDto messageDto = new MessageDto();


        messageDto.setMessageId(cursor.getString(cursor.getColumnIndexOrThrow("messageId")));
        messageDto.setMessage(cursor.getString(cursor.getColumnIndexOrThrow("message")));
        messageDto.setUserName(cursor.getString(cursor.getColumnIndexOrThrow("userName")));
        messageDto.setFrom(cursor.getString(cursor.getColumnIndexOrThrow("sideFrom")));
        messageDto.setSenderId(cursor.getString(cursor.getColumnIndexOrThrow("senderId")));
        messageDto.setDateTime(cursor.getString(cursor.getColumnIndexOrThrow("dateTime")));
        messageDto.setTypeMessage(cursor.getString(cursor.getColumnIndexOrThrow("typeMessage")));

        return messageDto;
    }

    @Override
    protected List<MessageDto> listDirtyDto(String field) {
        return null;
    }



    public ArrayList<MessageDto> getAllMessages() {
        ArrayList<MessageDto> messageList = new ArrayList<MessageDto>();
        try {

            String sql = "select  * from allMessages" + ";";
            DatabaseHelper dbHelper = new DatabaseHelper(mContext);
            Cursor cursor = dbHelper.getReadableDatabase().rawQuery(sql, null);
            while (cursor.moveToNext()) {
                try {
                    MessageDto dto = buildDto(cursor);
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

    public ArrayList<MessageDto> getAllMessagesUser(String senderId) {
        ArrayList<MessageDto> messageList = new ArrayList<MessageDto>();
        try {

           // String sql = "select  * from allMessages" + ";";
            String sql = "select  * from allMessages where senderId = '" + senderId + "' ;";
            DatabaseHelper dbHelper = new DatabaseHelper(mContext);
            Cursor cursor = dbHelper.getReadableDatabase().rawQuery(sql, null);
            while (cursor.moveToNext()) {
                try {
                    MessageDto dto = buildDto(cursor);
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
