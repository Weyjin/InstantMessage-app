package com.instant.message_app.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.instant.message_app.R;
import com.instant.message_app.entity.ChatMessage;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private Context mContext;
    private List<ChatMessage> chatMessages;
    private LayoutInflater inflater;

    public RecyclerViewAdapter(Context context,List<ChatMessage> chatMessages){
        this.mContext=context;
        this.chatMessages=chatMessages;
        this.inflater=LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if(viewType==1){
            View view = inflater.inflate(R.layout.chat_message_item_right_layout,viewGroup,false);
            return new ViewHolder(view);
        }else {
            View view = inflater.inflate(R.layout.chat_message_item_left_layout,viewGroup,false);
            return new ViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.name.setText(chatMessages.get(position).getUser().getName());
        viewHolder.message.setText(chatMessages.get(position).getMessage());
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage chatMessage=chatMessages.get(position);
        if(chatMessage==null){
            return 0;
        }else {
            return chatMessage.isCurrent()==true?1:0;
        }

    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView name,message;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.text_name);
            message=itemView.findViewById(R.id.text_message);
        }
    }

}
