package com.mss.msschat.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.mss.msschat.DataBase.Dto.ContactsDto;
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

        ContactsDto contactsDto = allFriendsList.get(position);


        holder.txtContactName.setText(contactsDto.getUserName());
        holder.txtContactNumber.setText(contactsDto.getPhoneNumber());

        if (contactsDto.getUserPicture() != null) {
            Glide.with(mContext).load(contactsDto.getUserPicture()).into(holder.imgContactProfile);

        } else {
            holder.imgContactProfile.setImageResource(R.mipmap.ic_launcher);

        }

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
        }


    }

}
