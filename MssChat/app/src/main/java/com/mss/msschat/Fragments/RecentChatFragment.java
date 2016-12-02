package com.mss.msschat.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mss.msschat.Adapters.RecentChatAdapter;
import com.mss.msschat.AppUtils.Session;
import com.mss.msschat.DataBase.Dao.RecentChatDao;
import com.mss.msschat.DataBase.Dto.RecentChatDto;
import com.mss.msschat.Interfaces.UpdateRecentChats;
import com.mss.msschat.Models.RecentChatModel;
import com.mss.msschat.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mss on 23/11/16.
 */

public class RecentChatFragment extends Fragment implements UpdateRecentChats {

    View rootView;
    RecyclerView lvRecentChat;
    RecentChatAdapter adapter;
    List<RecentChatDto> recentChatDataList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_recent_chat, container, false);
        initUI();
        return rootView;
    }

    private void initUI() {
        lvRecentChat = (RecyclerView) rootView.findViewById(R.id.lv_recent_chat);
        Session.setUpdateRecentChats(this);
        populateUI();
    }

    private void populateUI() {


        recentChatDataList = new ArrayList<RecentChatDto>();


        RecentChatDao recentChatDao = new RecentChatDao(getActivity());


        recentChatDataList = recentChatDao.getAllRecentMessages();


        adapter = new RecentChatAdapter(getActivity(), recentChatDataList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        lvRecentChat.setLayoutManager(layoutManager);
        lvRecentChat.setItemAnimator(new DefaultItemAnimator());
        lvRecentChat.setAdapter(adapter);


    }


    @Override
    public void updateRecentChatList() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                initUI();


            }
        });


    }



}
