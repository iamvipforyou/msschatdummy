package com.mss.msschat.AppUtils;

import com.mss.msschat.Interfaces.SearchContacts;
import com.mss.msschat.Interfaces.UpdateRecentChats;
import com.mss.msschat.Interfaces.searchRecent;
import com.mss.msschat.Interfaces.updateContacts;

/**
 * Created by mss on 1/12/16.
 */

public class Session {


    private static UpdateRecentChats updateRecentChats;

    private static updateContacts mUpdateContacts;

    private static searchRecent mSearchRecent;


    private static SearchContacts mSearchContacts;

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


    public static void setmSearchRecent(searchRecent listner) {

        if (listner != null) {

            mSearchRecent = listner;
        }


    }


    public static void getSearchRecent(String username) {

        if (mSearchRecent != null) {

            mSearchRecent.searchInRecentChat(username);
        }

    }

    public static void setmSearchContacts(SearchContacts listner) {

        if (listner != null) {

            mSearchContacts = listner;

        }
    }

    public static void getSearchContacts(String userName) {

        if (mSearchContacts != null) {

            mSearchContacts.searchInContactsList(userName);
        }

    }

}
