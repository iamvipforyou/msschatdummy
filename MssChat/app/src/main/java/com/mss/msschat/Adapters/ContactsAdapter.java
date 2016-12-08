package com.mss.msschat.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.mss.msschat.Activities.ChatMessageActivity;
import com.mss.msschat.AppUtils.Utils;
import com.mss.msschat.DataBase.Dto.ContactsDto;
import com.mss.msschat.DataBase.Dto.RecentChatDto;
import com.mss.msschat.Models.ContactListModel;
import com.mss.msschat.R;

import java.util.ArrayList;
import java.util.List;


public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {

    Context mContext;
    List<ContactsDto> allFriendsList;


    public ContactsAdapter(Context context, List<ContactsDto> allContacts) {

        this.mContext = context;
        this.allFriendsList = allContacts;

    }


    @Override
    public ContactsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View contactsView = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_adapter, parent, false);

        return new ViewHolder(contactsView);
    }

    @Override
    public void onBindViewHolder(ContactsAdapter.ViewHolder holder, int position) {

        final ContactsDto contactsDto = allFriendsList.get(position);


        holder.txtContactName.setText(contactsDto.getUserName());
        holder.txtContactNumber.setText(contactsDto.getPhoneNumber());

        if (contactsDto.getUserPicture() != null) {
            Glide.with(mContext).load(contactsDto.getUserPicture()).into(holder.imgContactProfile);

        } else {
            holder.imgContactProfile.setImageResource(R.mipmap.ic_launcher);

        }


        holder.imgContactProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Utils.showImageDialog(mContext, contactsDto.getUserPicture(), contactsDto.getUserName());
            }
        });

    }

    @Override
    public int getItemCount() {
        return allFriendsList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtContactName;
        TextView txtContactNumber;
        ImageView imgContactProfile;
        LinearLayout llmain;

        public ViewHolder(View itemView) {
            super(itemView);


            txtContactName = (TextView) itemView.findViewById(R.id.txt_contact_name);
            txtContactNumber = (TextView) itemView.findViewById(R.id.txt_contact_number);
            imgContactProfile = (ImageView) itemView.findViewById(R.id.img_contact_profile);
            llmain = (LinearLayout) itemView.findViewById(R.id.llmain);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ContactsDto recentChatDto = allFriendsList.get(getPosition());


                    Intent chatMessageIntent = new Intent(mContext, ChatMessageActivity.class);
                    chatMessageIntent.putExtra("intentFrom", "contacts");
                    chatMessageIntent.putExtra("id", recentChatDto.getContactUserId());
                    chatMessageIntent.putExtra("user_name", recentChatDto.getUserName());
                    chatMessageIntent.putExtra("user_image", recentChatDto.getUserPicture());
                    chatMessageIntent.putExtra("typeMessage", "private");
                    mContext.startActivity(chatMessageIntent);
                }
            });
        }


    }

}
