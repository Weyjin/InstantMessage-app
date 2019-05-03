package com.instant.message_app.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.instant.message_app.R;
import com.instant.message_app.constants.SocketConstant;
import com.instant.message_app.entity.GroupChat;
import com.instant.message_app.entity.ScanCode;
import com.instant.message_app.fragments.ContactsFragment;
import com.instant.message_app.fragments.GroupChatFragment;
import com.instant.message_app.fragments.HomeFragment;
import com.instant.message_app.utils.DisplayUtil;
import com.instant.message_app.utils.SharedPreferenceHelper;
import com.instant.message_app.websocket.WebSocketManager;

import org.apache.commons.codec.binary.Base64;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements ContactsFragment.ContactsListener ,
        HomeFragment.LogoutListener,GroupChatFragment.OnItemClickListener, WebSocketManager.ISocketListener{

    private ViewPager viewPager;
    private TextView groupChat,contacts,home;
    private List<TextView> textViews;
    private SharedPreferenceHelper helper;
    private static final String TAG="MainActivity";
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext=this;
        helper=new SharedPreferenceHelper(this);

        viewPager=findViewById(R.id.viewpager);
        groupChat=findViewById(R.id.text_group_chat);
        contacts=findViewById(R.id.text_contacts);
        home=findViewById(R.id.text_home);
        textViews=new ArrayList<>();
        textViews.add(groupChat);
        textViews.add(contacts);
        textViews.add(home);

        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        viewPager.setCurrentItem(1);
        select(1);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                   selectNone();
                   select(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        bindClick();
    }

    private void bindClick(){
        for(int i=0;i<textViews.size();i++){
            final int finalI = i;
            textViews.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int current=viewPager.getCurrentItem();
                    if(current!=finalI){
                        viewPager.setCurrentItem(finalI);
                    }

                }
            });
        }
    }
    private void selectNone(){
        for(TextView t:textViews){
            t.setTextColor(this.getResources().getColor(R.color.textSelectNone));
        }
    }
    private void select(int index){
        TextView textView=textViews.get(index);
        textView.setTextColor(this.getResources().getColor(R.color.textSelect));
    }

    @Override
    public void toMessage(int groupId,int userId){
        Intent intent=new Intent(this,ChatMessageActivity.class);
        Bundle bundle=new Bundle();
        bundle.putInt("groupId",groupId);
        bundle.putInt("userId",userId);
        intent.putExtras(bundle);
        startActivity(intent);


    }

    @Override
    public void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("提示");
        builder.setMessage("确定退出吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
      {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                AlertDialog alertDialog=builder.show();
                alertDialog.dismiss();
                Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
                helper.clear();
                finish();

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
{
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                     AlertDialog alertDialog=builder.show();
                     alertDialog.dismiss();
            }
        });
        builder.show();
    }

    private AlertDialog dialog;
    private void showDialog(String content){
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_scan_code_confirm_layout, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.DialogTheme);

        int heightPixels = DisplayUtil.getHeight(this);

        TextView textView=view.findViewById(R.id.text_login_message);
        Button ok=view.findViewById(R.id.button_ok);
        Button cancel=view.findViewById(R.id.button_cancel);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code= UUID.randomUUID().toString();
                ScanCode scanCode=new ScanCode();
                scanCode.setCode(content);
                int id=helper.getUserId();
                scanCode.setId(id);
                String json= JSON.toJSONString(scanCode);

                String encodeString=encode(json);
                Log.i(TAG,encodeString);
                WebSocketManager.getInstance().connect(SocketConstant.SCAN_CODE_ADDRESS+code+"/"+ encodeString,mContext);
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(v->dialog.dismiss());
        textView.setMinHeight(heightPixels);
        builder.setView(view);

        dialog = builder.create();
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.dialog_animation);
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);

        dialog.show();
    }
    private String encode(String code){
        byte[] b=code.getBytes();
        Base64 base64=new Base64();
        b=base64.encode(b);
        String s=new String(b);
        return s;
    }
    //TODO:扫码成功之后，连接socket,弹出登录界面
    @Override
    public void resultContent(String content) {

       showDialog(content);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WebSocketManager.getInstance().disconnect();
    }

    @Override
    public void onGroupChatItemClick(GroupChat groupChat) {

        Intent intent=new Intent(this,GroupChatsMessageActivity.class);
        Bundle bundle=new Bundle();
        bundle.putInt("groupId",groupChat.getId());
        intent.putExtras(bundle);
        startActivity(intent);

    }

    @Override
    public void success(String message) {
        Log.i(TAG,message);
        //连接成功之后关闭
        WebSocketManager.getInstance().disconnect();
    }

    @Override
    public void error(String message) {
        Log.i(TAG,message);

    }

    @Override
    public void textMessage(String message) {
        Log.i(TAG,message);

    }


    private class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private List<Fragment> fragments;

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            fragments=new ArrayList<>();
            fragments.add(GroupChatFragment.newInstance());
            fragments.add(ContactsFragment.newInstance());
            fragments.add(HomeFragment.newInstance());
        }

        @Override
        public Fragment getItem(int i) {
            return fragments.get(i);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }
}
