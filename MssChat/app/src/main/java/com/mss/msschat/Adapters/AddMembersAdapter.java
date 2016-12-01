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
import com.mss.msschat.Interfaces.showSelectedMembersImage;
import com.mss.msschat.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by mss on 30/11/16.
 */

public class AddMembersAdapter extends RecyclerView.Adapter<AddMembersAdapter.ViewHolder> {


    Context mContext;
    List<ContactsDto> allFriendsList;
    showSelectedMembersImage listner;

    public AddMembersAdapter(Context context, List<ContactsDto> allContacts, showSelectedMembersImage listner) {

        this.mContext = context;
        this.allFriendsList = allContacts;
        this.listner = listner;
    }


    @Override
    public AddMembersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View contactsView = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_adapter, parent, false);

        return new AddMembersAdapter.ViewHolder(contactsView);
    }

    @Override
    public void onBindViewHolder(AddMembersAdapter.ViewHolder holder, int position) {
        ContactsDto contactsDto = allFriendsList.get(position);


        holder.txtContactName.setText(contactsDto.getUserName());
        holder.txtContactNumber.setText(contactsDto.getPhoneNumber());

        if (contactsDto.getUserPicture() != null) {
            Picasso.with(mContext).load(contactsDto.getUserPicture()).into(holder.imgContactProfile);
            //  holder.imgContactProfile.setImageResource(R.mipmap.ic_launcher);


        } else {
            holder.imgContactProfile.setImageResource(R.mipmap.ic_launcher);

        }


        if (contactsDto.isSelected() == true) {
            holder.imgContactProfile.setImageResource(R.drawable.tick_icon);


        } else {
            //     holder.imgContactProfile.setImageResource(R.mipmap.ic_launcher);


            if (contactsDto.getUserPicture() != null) {
                Glide.with(mContext).load(contactsDto.getUserPicture()).into(holder.imgContactProfile);
                //  holder.imgContactProfile.setImageResource(R.mipmap.ic_launcher);


            } else {
                holder.imgContactProfile.setImageResource(R.mipmap.ic_launcher);

            }


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


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    ContactsDto contactsDto = allFriendsList.get(getPosition());


                    if (contactsDto.isSelected() == true) {

                        contactsDto.setSelected(false);
                        listner.getSelectedMembers();
                    } else {
                        contactsDto.setSelected(true);
                        listner.getSelectedMembers();
                    }

                    notifyDataSetChanged();

                }
            });

        }
    }

    public List<ContactsDto> allSelectedMembers() {
        return allFriendsList;
    }
}
