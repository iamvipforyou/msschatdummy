package com.mss.msschat.DataBase.Dao;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.mss.msschat.AppUtils.Constants;
import com.mss.msschat.DataBase.DatabaseHelper;
import com.mss.msschat.DataBase.Dto.ContactsDto;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mss on 29/11/16.
 */

public class ContactsDao extends Dao<ContactsDto> {


    Context mContext;


    public ContactsDao(Context context) {
        super(Constants.DataBaseParms.CONTACTS_TABLE);
        this.mContext = context;
    }


    @Override
    public void insert(ContactsDto dto) {

        insertOrReplace(dto, "contactId", dto.getContactId(),mContext);
    }

    @Override
    public void replace(ContactsDto dto) {

    }

    @Override
    public void delete(String id) {

        deletebyId(id, "contactId", mContext);
    }

    @Override
    public ContactsDto buildDto(JSONObject json) throws JSONException {
        return null;
    }

    @Override
    protected ContactsDto buildDto(Cursor cursor) {


        ContactsDto contactsDto = new ContactsDto();

        contactsDto.setContactId(cursor.getString(cursor.getColumnIndexOrThrow("contactId")));
        contactsDto.setContactUserId(cursor.getString(cursor.getColumnIndexOrThrow("contactUserId")));
        contactsDto.setPhoneNumber(cursor.getString(cursor.getColumnIndexOrThrow("phoneNumber")));
        contactsDto.setUserPicture(cursor.getString(cursor.getColumnIndexOrThrow("userPicture")));
        contactsDto.setUserName(cursor.getString(cursor.getColumnIndexOrThrow("userName")));


        return contactsDto;
    }

    @Override
    protected List<ContactsDto> listDirtyDto(String field) {
        return null;
    }


    public ArrayList<ContactsDto> getAllAppContacts() {
        ArrayList<ContactsDto> messageList = new ArrayList<ContactsDto>();
        try {

            String sql = "select  * from contatcs" + ";";
            DatabaseHelper dbHelper = new DatabaseHelper(mContext);
            Cursor cursor = dbHelper.getReadableDatabase().rawQuery(sql, null);
            while (cursor.moveToNext()) {
                try {
                    ContactsDto dto = buildDto(cursor);
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
