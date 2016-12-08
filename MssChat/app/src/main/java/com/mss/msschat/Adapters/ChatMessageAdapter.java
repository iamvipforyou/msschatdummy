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
import com.mss.msschat.AppUtils.Utils;
import com.mss.msschat.Models.ChatMessageModel;
import com.mss.msschat.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;


public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.ViewHolder> {


    Context mContext;
    List<ChatMessageModel> dataList;

    public ChatMessageAdapter(Context context, List<ChatMessageModel> chatMessageList) {

        this.mContext = context;
        this.dataList = chatMessageList;


    }


    @Override
    public ChatMessageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View chatMessageView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_chat_messages, parent, false);

        return new ViewHolder(chatMessageView);

    }

    @Override
    public void onBindViewHolder(ChatMessageAdapter.ViewHolder holder, int position) {


        ChatMessageModel chatMessageModel = dataList.get(position);


        if (chatMessageModel.getFrom().equals("true")) {


            if (chatMessageModel.getMessage().contains("@picPath>")) {


                String imageMessage = chatMessageModel.getMessage();


                String[] completePath = imageMessage.split(">");


                String picturePath = completePath[1];


                holder.llUser.setVisibility(View.GONE);
                holder.llSender.setVisibility(View.VISIBLE);
                holder.llSenderText.setVisibility(View.GONE);
                holder.txtSenderMessage.setVisibility(View.GONE);
                holder.llSenderImage.setVisibility(View.VISIBLE);
                holder.txtSenderTime.setText(Utils.getTimeAgo(Long.parseLong(chatMessageModel.getDateTime())));
                Glide.with(mContext).load(new File(picturePath)).into(holder.iVSender);
                //  Utils.loadAllImageFromStorage(holder.iVSender, picturePath);


            } else {

                holder.llSender.setVisibility(View.VISIBLE);
                holder.llUser.setVisibility(View.GONE);
                holder.llSenderImage.setVisibility(View.GONE);
                holder.txtSenderMessage.setText(chatMessageModel.getMessage());
                holder.txtSenderTime.setText(Utils.getTimeAgo(Long.parseLong(chatMessageModel.getDateTime())));
            }


        } else {


            if (chatMessageModel.getMessage().contains("@picPath>")) {


                String imageMessage = chatMessageModel.getMessage();


                String[] completePath = imageMessage.split(">");

                String picturePath = completePath[1];

                ///////////////////////////




                ///////////////////////

                holder.llSender.setVisibility(View.GONE);
                holder.llUser.setVisibility(View.VISIBLE);
                holder.llImageMessage.setVisibility(View.VISIBLE);
                holder.llTextMessage.setVisibility(View.GONE);
                holder.txtUserMessage.setVisibility(View.GONE);
                holder.txtReceiverTime.setText(Utils.getTimeAgo(Long.parseLong(chatMessageModel.getDateTime())));

                Glide.with(mContext).load(new File(picturePath)).into(holder.iVMessage);


            } else {


                holder.llSender.setVisibility(View.GONE);
                holder.llUser.setVisibility(View.VISIBLE);
                holder.llImageMessage.setVisibility(View.GONE);
                holder.txtUserMessage.setText(chatMessageModel.getMessage());
                holder.txtReceiverTime.setText(Utils.getTimeAgo(Long.parseLong(chatMessageModel.getDateTime())));
            }
        }


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtUserMessage, txtSenderMessage, txtReceiverTime, txtSenderTime, txtGrpUserName;
        LinearLayout llUser, llSender, llImageMessage, llTextMessage, llSenderImage, llSenderText;
        ImageView iVMessage, iVSender;

        public ViewHolder(View itemView) {
            super(itemView);


            txtUserMessage = (TextView) itemView.findViewById(R.id.txt_msg);
            txtSenderMessage = (TextView) itemView.findViewById(R.id.txt_sender_msg);
            txtReceiverTime = (TextView) itemView.findViewById(R.id.txt_receiver_time);
            txtSenderTime = (TextView) itemView.findViewById(R.id.txt_sender_time);
            llUser = (LinearLayout) itemView.findViewById(R.id.ll_user);
            llSender = (LinearLayout) itemView.findViewById(R.id.ll_sender);
            llImageMessage = (LinearLayout) itemView.findViewById(R.id.ll_img_chat_msg);
            iVMessage = (ImageView) itemView.findViewById(R.id.img_chat);
            llTextMessage = (LinearLayout) itemView.findViewById(R.id.ll_txt_chat_msg);
            llSenderImage = (LinearLayout) itemView.findViewById(R.id.ll_img_senderchat_msg);
            iVSender = (ImageView) itemView.findViewById(R.id.img_sender_chat);
            llSenderText = (LinearLayout) itemView.findViewById(R.id.ll_sende_chart_txt);


        }
    }
}
