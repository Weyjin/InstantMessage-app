package com.instant.message_app.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.instant.message_app.R;
import com.instant.message_app.adapter.GroupChatRecyclerViewAdapter;
import com.instant.message_app.entity.GroupChat;
import com.instant.message_app.utils.HttpUtils;
import com.instant.message_app.utils.JsonUtils;
import com.instant.message_app.utils.SharedPreferenceHelper;

import java.util.List;

public class GroupChatFragment extends Fragment {

    private RecyclerView recyclerView;
    private GroupChatRecyclerViewAdapter adapter;
    private SharedPreferenceHelper helper;
    private OnItemClickListener listener;


    public GroupChatFragment() {
        // Required empty public constructor
    }


    public static GroupChatFragment newInstance() {
        GroupChatFragment fragment = new GroupChatFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helper=new SharedPreferenceHelper(getContext());
        listener= (OnItemClickListener) getContext();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_group_chat, container, false);
        recyclerView=view.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
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
            int id=helper.getUserId();
            try{
                String groupChats= HttpUtils.getGroupChats(id+"");
                return groupChats;
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
                List<GroupChat> groupChats= JsonUtils.getGroupChats(result);
                adapter=new GroupChatRecyclerViewAdapter(getContext(),groupChats);
                adapter.setOnItemClickListener(new GroupChatRecyclerViewAdapter.OnItemClickListener() {
                    @Override
                    public void onClick(int position) {
                        GroupChat groupChat=groupChats.get(position);
                        listener.onGroupChatItemClick(groupChat);
                    }
                });
                recyclerView.setAdapter(adapter);

            }



        }

        //取消执行中的任务时更改UI
        @Override
        protected void onCancelled() {

        }
    }

    public interface OnItemClickListener{
        void onGroupChatItemClick(GroupChat groupChat);
    }
}
