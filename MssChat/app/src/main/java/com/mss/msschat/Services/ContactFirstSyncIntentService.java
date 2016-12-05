package com.mss.msschat.Services;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import com.google.gson.Gson;
import com.mss.msschat.AppUtils.ApiClient;
import com.mss.msschat.AppUtils.Session;
import com.mss.msschat.DataBase.Dao.ContactsDao;
import com.mss.msschat.DataBase.Dto.ContactsDto;
import com.mss.msschat.Interfaces.ApiInterface;
import com.mss.msschat.Models.ContactData;
import com.mss.msschat.Models.ContactListModel;
import com.mss.msschat.Models.ContactResponse;
import com.mss.msschat.Models.VerifyContactsModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactFirstSyncIntentService extends IntentService {
    private Cursor cursor;
    private ArrayList<String> number;
    private ArrayList<ContactListModel> mAllData;
    List<Long> allContactToSendList;

    public ContactFirstSyncIntentService() {
        super("ContactService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.e("Start Service", "Just Start");

        Log.e("Start Service", "List null");
        mAllData = new ArrayList<ContactListModel>();
        String allContacts = null;
        String searchAllContacts = null;
        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
        String _ID = ContactsContract.Contacts._ID;
        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;
        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
        ContentResolver contentResolver = getContentResolver();
        cursor = contentResolver.query(CONTENT_URI, null, null, null, null);
        ContactListModel contacts;
        Cursor phoneCursor;
        if (cursor.getCount() > 0) {
            try {
                while (cursor.moveToNext()) {
                    if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(HAS_PHONE_NUMBER))) > 0) {
                        contacts = new ContactListModel();
                        phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[]{cursor.getString(cursor.getColumnIndex(_ID))}, null);
                        number = new ArrayList<>();
                        while (phoneCursor.moveToNext()) {
                            number.add(phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER)));
                        }
                        contacts.setName(cursor.getString(cursor.getColumnIndex(DISPLAY_NAME)));
                        contacts.setPhoneno(number);
                        phoneCursor.close();
                        mAllData.add(contacts);
                    }


                }


                allContactToSendList = new ArrayList<Long>();
                for (int contactNumber = 0; contactNumber < mAllData.size(); contactNumber++) {

                    ContactListModel contactListModel = mAllData.get(contactNumber);
                    List<String> numbers = contactListModel.getPhoneno();
                    if (numbers.size() > 0) {
                        String validNumber = numbers.get(0).replaceAll("[\\-\\+\\.\\^:,]", "").replaceAll("\\s", "").replaceAll("\\(", "").replaceAll("\\)", "");


                        if (validNumber.length() > 10) {

                            String trimNumber = validNumber.substring(validNumber.length() - 10, validNumber.length());

                            allContactToSendList.add(Long.parseLong(trimNumber));
                        } else if (validNumber.length() == 10) {


                            allContactToSendList.add(Long.parseLong(validNumber));
                        } else {

                        }


                    }


                }


                sendContactsToServer(allContactToSendList);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void sendContactsToServer(List<Long> allContactToSendList) {


        VerifyContactsModel contactsModel = new VerifyContactsModel();

        contactsModel.setContacts(allContactToSendList);


        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<ContactResponse> getAllServerContacts = apiService.getAllAppContactResponse(contactsModel);

        getAllServerContacts.enqueue(new Callback<ContactResponse>() {
            @Override
            public void onResponse(Call<ContactResponse> call, Response<ContactResponse> response) {

                try {
                    if (response.isSuccessful()) {


                        ContactResponse contactsVerified = response.body();

                        int statusCode = contactsVerified.getStatus();


                        if (statusCode == 200) {


                            List<ContactData> contactDataList = contactsVerified.getData();


                            ContactsDao contactsDao = new ContactsDao(ContactFirstSyncIntentService.this);

                            for (int myContact = 0; myContact < contactDataList.size(); myContact++) {

                                ContactsDto contactsDto = new ContactsDto();

                                ContactData contactData = contactDataList.get(myContact);

                                contactsDto.setContactUserId(contactData.getId());
                                contactsDto.setPhoneNumber(String.valueOf(contactData.getPhoneNumber()));
                                contactsDto.setUserPicture(contactData.getProfilePic());
                                contactsDto.setUserName(contactData.getName());
                                contactsDao.insert(contactsDto);
                            }

                            Session.getUpdateContacts();

                            List<ContactsDto> allData = contactsDao.getAllAppContacts();
                            Log.e("nOt", "Successful");
                        } else {


                            Log.e("nOt", "Successful");
                        }


                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call<ContactResponse> call, Throwable t) {


                Log.e("failed", "i am failed");
            }
        });


    }
}
