package com.instant.message_app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.instant.message_app.R;
import com.instant.message_app.entity.ChatMessage;

import java.util.List;

public class ChatMessageListAdapter extends BaseAdapter {

    private Context mContext;
    private List<ChatMessage> chatMessages;

    public ChatMessageListAdapter(Context context,List<ChatMessage> chatMessages){
        this.mContext=context;
        this.chatMessages=chatMessages;
    }

    @Override
    public int getCount() {
        return chatMessages.size();
    }

    @Override
    public Object getItem(int position) {
        return chatMessages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ChatMessage chatMessage= (ChatMessage) getItem(position);
         ViewHolder holder;
        if(convertView==null){
            holder=new ViewHolder();
           if(chatMessage.isCurrent()){
               convertView= LayoutInflater.from(mContext).inflate(R.layout.chat_message_item_right_layout,parent,false);
               holder.name=convertView.findViewById(R.id.text_name);
               holder.message=convertView.findViewById(R.id.text_message);
               convertView.setTag(holder);
           }else {
               convertView= LayoutInflater.from(mContext).inflate(R.layout.chat_message_item_left_layout,null);
               holder.name=convertView.findViewById(R.id.text_name);
               holder.message=convertView.findViewById(R.id.text_message);
               convertView.setTag(holder);
           }
        }else {
            holder= (ViewHolder) convertView.getTag();
        }

        holder.name.setText(chatMessage.getUser().getName());
        holder.message.setText(chatMessage.getMessage());

        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return chatMessages.get(position).isCurrent()==true?1:0;
    }
    @Override
    public int getViewTypeCount() {
        return 2;
    }
    private class ViewHolder{
        TextView name,message;
    }

}
