package com.mss.msschat.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.mss.msschat.Activities.AddGroupMembersActivity;
import com.mss.msschat.Activities.ChatMessageActivity;
import com.mss.msschat.AppUtils.Utils;
import com.mss.msschat.DataBase.Dto.RecentChatDto;
import com.mss.msschat.Models.RecentChatModel;
import com.mss.msschat.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mss on 24/11/16.
 */

public class RecentChatAdapter extends RecyclerView.Adapter<RecentChatAdapter.ViewHolder> {

    List<RecentChatDto> recentChatModelArrayList;
    Context mContext;
    public int longClicked;
    public int checkedCount = 0;

    public RecentChatAdapter(Context context, List<RecentChatDto> recentChatModelArrayList) {
        this.mContext = context;
        this.recentChatModelArrayList = recentChatModelArrayList;
    }

    @Override
    public RecentChatAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemListView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_chat_adapter, parent, false);
        return new ViewHolder(itemListView);
    }

    @Override
    public void onBindViewHolder(RecentChatAdapter.ViewHolder holder, int position) {
        RecentChatDto recentChatModel = recentChatModelArrayList.get(position);
        holder.txtName.setText(recentChatModel.getUserName());
        holder.txtChatMessage.setText(recentChatModel.getMessage());
        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(holder.imgProfile);

        if (recentChatModel.getProfileImage() != null) {
            Glide.with(mContext).load(recentChatModel.getProfileImage()).into(imageViewTarget);
        } else {
            Glide.with(mContext).load(R.mipmap.ic_launcher).into(imageViewTarget);
        }
        holder.txtDate.setText(Utils.getTimeAgo(Long.parseLong(recentChatModel.getDateTime())));





    }

    @Override
    public int getItemCount() {
        return recentChatModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName;
        TextView txtChatMessage;
        TextView txtDate;
        TextView txtBadge;
        ImageView imgCheckedBadge, imgProfile;

        public ViewHolder(View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.txt_friend_name);
            txtChatMessage = (TextView) itemView.findViewById(R.id.txt_msg);
            txtDate = (TextView) itemView.findViewById(R.id.txt_msg_date);
            txtBadge = (TextView) itemView.findViewById(R.id.badge);
            imgCheckedBadge = (ImageView) itemView.findViewById(R.id.checked_badge);
            imgProfile = (ImageView) itemView.findViewById(R.id.img_profile);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (longClicked == 1) {


                        if (checkedCount == recentChatModelArrayList.size()) {


                            longClicked = 0;
                            notifyDataSetChanged();


                        } else {

                            RecentChatDto groupMessageData = recentChatModelArrayList.get(getPosition());


                            if (groupMessageData.isSELECTED() == false) {

                                groupMessageData.setSELECTED(true);

                                if (checkedCount > 0) {
                                    checkedCount--;
                                }

                                notifyDataSetChanged();


                            } else {


                                groupMessageData.setSELECTED(false);

                                checkedCount++;
                                notifyDataSetChanged();

                            }


                        }


                    } else {


                        RecentChatDto recentChatDto = recentChatModelArrayList.get(getPosition());


                        Intent chatMessageIntent = new Intent(mContext, ChatMessageActivity.class);
                        chatMessageIntent.putExtra("intentFrom", "recent");
                        chatMessageIntent.putExtra("id", recentChatDto.getSenderId());
                        chatMessageIntent.putExtra("user_image", recentChatDto.getProfileImage());
                        chatMessageIntent.putExtra("typeMessage", recentChatDto.getTypeMessage());
                        mContext.startActivity(chatMessageIntent);
                    }
                }
            });


            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    if (longClicked == 0) {

                        longClicked = 1;
                        RecentChatDto groupMessageData = recentChatModelArrayList.get(getPosition());

                        groupMessageData.setSELECTED(true);

                        checkedCount = recentChatModelArrayList.size();


                        checkedCount--;


                        notifyDataSetChanged();

                        //    listner.changeMenuItems();


                    }


                    return false;


                }
            });


        }
    }

}
