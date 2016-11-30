package com.mss.msschat.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mss.msschat.Adapters.RecentChatAdapter;
import com.mss.msschat.Models.RecentChatModel;
import com.mss.msschat.R;

import java.util.ArrayList;

/**
 * Created by mss on 23/11/16.
 */

public class RecentChatFragment extends Fragment {

    View rootView;
    RecyclerView lvRecentChat;
    RecentChatAdapter adapter;
    ArrayList<RecentChatModel> recentChatDataList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_recent_chat, container, false);
        initUI(rootView);
        return rootView;
    }
    private void initUI(View rootView) {
        lvRecentChat = (RecyclerView) rootView.findViewById(R.id.lv_recent_chat);
        populateUI();
    }
    private void populateUI() {
        recentChatDataList = new ArrayList<RecentChatModel>();
        for (int i = 0; i < 10; i++) {
            RecentChatModel recentChatModel = new RecentChatModel();
            recentChatModel.setUserName("John Smith");
            recentChatModel.setTime("8998988");
            recentChatModel.setContent("Hello I am here");
            recentChatModel.setUserId("2345");
            recentChatModel.setImageUrl("http");
            recentChatDataList.add(recentChatModel);
        }

        adapter = new RecentChatAdapter(getActivity(), recentChatDataList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        lvRecentChat.setLayoutManager(layoutManager);
        lvRecentChat.setItemAnimator(new DefaultItemAnimator());
        lvRecentChat.setAdapter(adapter);


    }




}
