package com.mss.msschat.AppUtils;

import com.mss.msschat.Interfaces.UpdateRecentChats;

/**
 * Created by mss on 1/12/16.
 */

public class Session {


    private static UpdateRecentChats updateRecentChats;


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


}
