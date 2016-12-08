package com.mss.msschat.Fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import com.mss.msschat.Interfaces.DeleteSelectedRecent;
import com.mss.msschat.Interfaces.UpdateRecentChats;
import com.mss.msschat.Models.RecentChatModel;
import com.mss.msschat.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mss on 23/11/16.
 */

public class RecentChatFragment extends Fragment implements UpdateRecentChats, DeleteSelectedRecent {

    View rootView;
    RecyclerView lvRecentChat;
    RecentChatAdapter adapter;
    List<RecentChatDto> recentChatDataList;
    FloatingActionButton btnDelete;
    List<RecentChatDto> allSelectedChatList;
    private int count;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_recent_chat, container, false);
        initUI();
        return rootView;
    }

    private void initUI() {
        lvRecentChat = (RecyclerView) rootView.findViewById(R.id.lv_recent_chat);
        btnDelete = (FloatingActionButton) rootView.findViewById(R.id.btn_delete);
        Session.setUpdateRecentChats(this);
        populateUI();
    }

    private void populateUI() {

        btnDelete.setVisibility(View.GONE);
        recentChatDataList = new ArrayList<RecentChatDto>();


        final RecentChatDao recentChatDao = new RecentChatDao(getActivity());


        recentChatDataList = recentChatDao.getAllRecentMessages();


        adapter = new RecentChatAdapter(getActivity(), recentChatDataList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        lvRecentChat.setLayoutManager(layoutManager);
        lvRecentChat.setItemAnimator(new DefaultItemAnimator());
        lvRecentChat.setAdapter(adapter);


        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RecentChatDao recentChatDao = new RecentChatDao(getActivity());
                for (int i = 0; i < allSelectedChatList.size(); i++) {

                    RecentChatDto recentChatDto = allSelectedChatList.get(i);


                    if (recentChatDto.isSELECTED()) {


                        recentChatDao.delete(recentChatDto.getSenderId());
                    }


                }
                initUI();

            }
        });


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


    @Override
    public void deleteSelected() {

        allSelectedChatList = ((RecentChatAdapter) adapter).getAllSelectedList();

        count = allSelectedChatList.size();
        for (int i = 0; i < allSelectedChatList.size(); i++) {


            RecentChatDto recentChatDto = allSelectedChatList.get(i);


            if (recentChatDto.isSELECTED()) {


                if (count > 0) {
                    count--;
                }

            }


        }
        if (count < allSelectedChatList.size()) {
            btnDelete.setVisibility(View.VISIBLE);
        } else {
            btnDelete.setVisibility(View.GONE);
        }

    }
}
