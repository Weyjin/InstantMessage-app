package com.instant.message_app.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.instant.message_app.R;
import com.instant.message_app.entity.GroupChat;

import java.util.List;

public class GroupChatRecyclerViewAdapter extends RecyclerView.Adapter<GroupChatRecyclerViewAdapter.ViewHolder>{

    private Context mContext;
    private List<GroupChat> groupChats;
    private LayoutInflater inflater;
    private OnItemClickListener onItemClickListener;

    public GroupChatRecyclerViewAdapter(Context context,List<GroupChat> groupChats){
        this.mContext=context;
        this.groupChats=groupChats;
        this.inflater=LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public GroupChatRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

            View view = inflater.inflate(R.layout.group_chat_item_layout,viewGroup,false);

            return new GroupChatRecyclerViewAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull GroupChatRecyclerViewAdapter.ViewHolder viewHolder, int position) {
        viewHolder.name.setText(groupChats.get(position).getName());
        //viewHolder.synopsis.setText(groupChats.get(position).getName());
        viewHolder.itemView.setOnClickListener(v->onItemClickListener.onClick(position));
    }

    @Override
    public int getItemCount() {
        return groupChats.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView name,synopsis;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.text_name);
            synopsis=itemView.findViewById(R.id.text_synopsis);
        }
    }

    public interface OnItemClickListener{
        void onClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

}
