package com.instant.message_app.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.instant.message_app.R;
import com.instant.message_app.adapter.ExpandableListAdapter;
import com.instant.message_app.entity.Group;
import com.instant.message_app.entity.Result;
import com.instant.message_app.entity.User;
import com.instant.message_app.utils.HttpUtils;
import com.instant.message_app.utils.JsonUtils;

import java.util.ArrayList;
import java.util.List;

public class ContactsFragment extends Fragment {

    private ExpandableListAdapter adapter;
    private ExpandableListView expandableListView;
    private ContactsListener listener;

    public ContactsFragment() {
        // Required empty public constructor
    }


    public static ContactsFragment newInstance() {
        ContactsFragment fragment = new ContactsFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listener= (ContactsListener) getContext();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_contacts, container, false);
        expandableListView=view.findViewById(R.id.expandable_list);

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Group group= (Group) adapter.getGroup(groupPosition);
                Result result= (Result) adapter.getChild(groupPosition,childPosition);
                listener.toMessage(group.getId(),result.getId());
                return true;
            }
        });
        new MyTask().execute();
        return view;
    }

    private class MyTask extends AsyncTask<String, Integer, String> {
        //执行后台任务前做一些UI操作
        @Override
        protected void onPreExecute() {

        }

        //执行后台任务（耗时操作）,不可在此方法内修改UI
        @Override
        protected String doInBackground(String... params) {
            try {
                String groups = HttpUtils.getGroups();
                return groups;
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        //更新进度信息
        @Override
        protected void onProgressUpdate(Integer... progresses) {

        }

        //执行完后台任务后更新UI
        @Override
        protected void onPostExecute(String result) {
               System.out.println(result);
               if(result==null){
                   Toast.makeText(getContext(),"服务器出现异常",Toast.LENGTH_SHORT).show();
               }else {
                   List<Group> groups= JsonUtils.getGroups(result);
                   adapter=new ExpandableListAdapter(getContext(),groups);
                   expandableListView.setAdapter(adapter);
               }


        }

        //取消执行中的任务时更改UI
        @Override
        protected void onCancelled() {

        }
    }

    public interface ContactsListener{
        void toMessage(int groupId,int userId);
    }

}
