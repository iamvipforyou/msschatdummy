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
import com.mss.msschat.Activities.ChatMessageActivity;
import com.mss.msschat.Models.RecentChatModel;
import com.mss.msschat.R;

import java.util.ArrayList;

/**
 * Created by mss on 24/11/16.
 */

public class RecentChatAdapter extends RecyclerView.Adapter<RecentChatAdapter.ViewHolder> {

    ArrayList<RecentChatModel> recentChatModelArrayList;
    Context mContext;

    public RecentChatAdapter(Context context, ArrayList<RecentChatModel> recentChatModelArrayList) {
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
        RecentChatModel recentChatModel = recentChatModelArrayList.get(position);
        holder.txtName.setText(recentChatModel.getUserName());
        holder.txtChatMessage.setText(recentChatModel.getContent());
        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(holder.imgProfile);
        Glide.with(mContext).load("http://7606-presscdn-0-74.pagely.netdna-cdn.com/wp-content/uploads/2016/03/Dubai-Photos-Images-Travel-Tourist-Images-Pictures-800x600.jpg").into(imageViewTarget);
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
                    Intent chatMessageIntent = new Intent(mContext, ChatMessageActivity.class);
                    mContext.startActivity(chatMessageIntent);
                }
            });
        }
    }

}
