package com.mss.msschat.AppUtils;

import com.mss.msschat.Interfaces.UpdateRecentChats;
import com.mss.msschat.Interfaces.updateContacts;

/**
 * Created by mss on 1/12/16.
 */

public class Session {


    private static UpdateRecentChats updateRecentChats;

    private static updateContacts mUpdateContacts;


    public static void setUpdateRecentChats(UpdateRecentChats listner) {

        if (listner != null) {

            updateRecentChats = listner;
        }

    }


    public static void getUpdateRecentChat() {

        if (updateRecentChats != null) {

            updateRecentChats.updateRecentChatList();

        }

    }


    public static void setmUpdateContacts(updateContacts listner) {
        if (listner != null) {

            mUpdateContacts = listner;
        }

    }

    public static void getUpdateContacts() {

        if (mUpdateContacts != null) {

            mUpdateContacts.updateContactList();
        }
    }

}
