package com.mss.msschat.Fragments;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mss.msschat.Adapters.ContactsAdapter;
import com.mss.msschat.AppUtils.Session;
import com.mss.msschat.DataBase.Dao.ContactsDao;
import com.mss.msschat.DataBase.Dto.ContactsDto;
import com.mss.msschat.Interfaces.updateContacts;
import com.mss.msschat.Models.ContactListModel;
import com.mss.msschat.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by mss on 23/11/16.
 */

public class ContactChatFragment extends Fragment implements updateContacts {

    View rootView;
    Cursor cursor;
    RecyclerView lvContacts;
    ArrayList<ContactListModel> contactList;
    ArrayList<String> number;
    private ProgressDialog pDialog;
    private Handler updateBarHandler;
    ContactsAdapter adapter;
    ContactsDao contactsDao;


    List<ContactsDto> allUserFromContactList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_contact_chat, container, false);
        initUI();
        return rootView;
    }

    private void initUI() {
        lvContacts = (RecyclerView) rootView.findViewById(R.id.lv_contacts);
        contactsDao = new ContactsDao(getActivity());
        Session.setmUpdateContacts(this);
        //  setUpContacts();


        populateUI();


    }

    private void populateUI() {


        allUserFromContactList = contactsDao.getAllAppContacts();


        // Collections.sort(contactList, new SortBasedOnName());
        adapter = new ContactsAdapter(getActivity(), allUserFromContactList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        lvContacts.setLayoutManager(layoutManager);
        lvContacts.setItemAnimator(new DefaultItemAnimator());
        lvContacts.setAdapter(adapter);
      /*  updateBarHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                pDialog.cancel();
            }
        }, 200);
        */


    }

    private void getContacts() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String allContacts = null;
                String searchAllContacts = null;
                contactList = new ArrayList<ContactListModel>();
                contactList.clear();
                Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
                String _ID = ContactsContract.Contacts._ID;
                String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
                String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;
                Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
                String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
                ContentResolver contentResolver = getActivity().getContentResolver();
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
                                contactList.add(contacts);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    public void setUpContacts() {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Reading contacts...");
        pDialog.setCancelable(false);
        pDialog.show();
        updateBarHandler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                getContacts();
            }
        }).start();
    }

    @Override
    public void updateContactList() {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                initUI();


            }
        });


    }

    public class SortBasedOnName implements Comparator {
        public int compare(Object o1, Object o2) {
            ContactListModel contactSortList1 = (ContactListModel) o1;
            ContactListModel contactSortList2 = (ContactListModel) o2;
            return contactSortList1.getName().compareToIgnoreCase(contactSortList2.getName());
        }
    }
}