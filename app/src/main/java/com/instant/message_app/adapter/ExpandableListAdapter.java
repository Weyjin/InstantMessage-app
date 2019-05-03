package com.instant.message_app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.instant.message_app.R;
import com.instant.message_app.constants.SocketConstant;
import com.instant.message_app.entity.Group;
import com.instant.message_app.entity.Result;
import com.instant.message_app.ui.CircleImageView;
import com.instant.message_app.utils.LoadImagesTask;

import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private List<Group> groups;

    public ExpandableListAdapter(Context context,List<Group> groups){
        this.mContext=context;
        this.groups=groups;
    }
    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return groups.get(groupPosition).getUsers().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return groups.get(groupPosition).getUsers().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder holder;
        if(convertView==null){
            convertView= LayoutInflater.from(mContext).inflate(R.layout.group_item_layout,null);
            holder=new GroupViewHolder();
            holder.mGroupName=convertView.findViewById(R.id.text_group_name);
            holder.mChildCount=convertView.findViewById(R.id.text_child_count);
            holder.mGroupArrow=convertView.findViewById(R.id.text_group_arrow);
            convertView.setTag(holder);
        }else {
            holder= (GroupViewHolder) convertView.getTag();
        }
        Group group= (Group) getGroup(groupPosition);
        holder.mGroupName.setText(group.getName());
        holder.mChildCount.setText(group.getUsers().size()+"");
        if(isExpanded){
            holder.mGroupArrow.setText("▾");
        }else {
            holder.mGroupArrow.setText("▸");
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder holder;
        if(convertView==null){
            convertView=LayoutInflater.from(mContext).inflate(R.layout.child_item_layout,null);
            holder=new ChildViewHolder();
            holder.mChildName=convertView.findViewById(R.id.text_child_name);
            holder.circleImageView=convertView.findViewById(R.id.img);
            holder.mChildDescribe=convertView.findViewById(R.id.text_child_describe);
            convertView.setTag(holder);
        }else {
            holder= (ChildViewHolder) convertView.getTag();
        }
        Result user= (Result) getChild(groupPosition,childPosition);
        holder.mChildName.setText(user.getName());
        holder.mChildDescribe.setText(user.getDescribe());
        String uri= SocketConstant.HOST_NAME+"/"+user.getImg();
        new LoadImagesTask(holder.circleImageView).execute(uri);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private static final class GroupViewHolder {
        TextView mGroupName;
        TextView mChildCount;
        TextView mGroupArrow;
    }

    private static final class ChildViewHolder {
        TextView mChildName,mChildDescribe;
        CircleImageView circleImageView;
    }
}
