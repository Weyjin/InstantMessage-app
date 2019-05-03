package com.instant.message_app.activity;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.instant.message_app.R;
import com.instant.message_app.adapter.ChatMessageListAdapter;
import com.instant.message_app.adapter.RecyclerViewAdapter;
import com.instant.message_app.constants.SocketConstant;
import com.instant.message_app.entity.ChatMessage;
import com.instant.message_app.entity.Result;
import com.instant.message_app.utils.JsonUtils;
import com.instant.message_app.utils.SharedPreferenceHelper;
import com.instant.message_app.websocket.WebSocketManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 一对一聊天
 */
public class ChatMessageActivity extends Activity implements WebSocketManager.ISocketListener {

    //private ListView listView;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
   // private ChatMessageListAdapter adapter;
    private EditText message;
    private Button send;
    private  List<ChatMessage> chatMessages=new ArrayList<>();
    private int groupId,userId,currentId;
    private static Handler handler=new Handler();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_message);

        //listView=findViewById(R.id.list_chat_message);
        recyclerView=findViewById(R.id.recyclerView);
        message=findViewById(R.id.edit_chat_message);
        send=findViewById(R.id.button_send_message);

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
         groupId=bundle.getInt("groupId");
         userId=bundle.getInt("userId");
         currentId= new SharedPreferenceHelper(this).getUserId();
        //adapter=new ChatMessageListAdapter(this,chatMessages);
        //listView.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter=new RecyclerViewAdapter(this,chatMessages);
        recyclerView.setAdapter(adapter);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg=message.getText().toString();

                String messageString="{\"message\":\""+msg+"\",\"currentUserId\":\""+currentId+"_"+userId+"\",\"toUserId\":\""+userId+"_"+currentId+"\"}";
                message.setText("");
                hideInput();
                WebSocketManager.getInstance().sendText(messageString);


            }
        });

        WebSocketManager.getInstance().connect(SocketConstant.CHAT_MESSAGE_ADDRESS+currentId+"_"+userId+"/"+currentId,this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WebSocketManager.getInstance().disconnect();
    }


    @Override
    public void success(String message) {
        System.out.println(message);

    }

    @Override
    public void error(String message) {
        System.out.println(message);

    }

    @Override
    public void textMessage(String s) {

        ChatMessage chatMessage= JsonUtils.getChatMessage(s);
        chatMessages.add(chatMessage);

        try{
            // 新启动一个子线程
            new Thread(new Runnable() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                            recyclerView.scrollToPosition(chatMessages.size()-1);
                            sendNotification(chatMessage.getUser().getName(),chatMessage.getMessage());

                        }
                    });
                }
            }).start();
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    private void sendNotification(String name,String message){

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(name, name, NotificationManager.IMPORTANCE_LOW);

            notificationManager.createNotificationChannel(mChannel);
            notification = new Notification.Builder(this,"1")
                    .setChannelId(name)
                    .setContentTitle(name)
                    .setContentText(message)
                    .setSmallIcon(android.R.drawable.sym_action_email).build();
        } else {
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,"1")
                    .setContentTitle(name)
                    .setContentText(message)
                    .setSmallIcon(android.R.drawable.sym_action_email)
                    .setOngoing(true);
            notification = notificationBuilder.build();
        }
        notificationManager.notify(111111, notification);


        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone rt = RingtoneManager.getRingtone(getApplicationContext(), uri);
        rt.play();

    }


    protected void hideInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        View v = getWindow().peekDecorView();
        if (null != v) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

}
