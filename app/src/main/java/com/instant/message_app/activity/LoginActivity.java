package com.instant.message_app.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.instant.message_app.R;
import com.instant.message_app.entity.LoginResult;
import com.instant.message_app.utils.HttpUtils;
import com.instant.message_app.utils.JsonUtils;
import com.instant.message_app.utils.SharedPreferenceHelper;

public class LoginActivity extends AppCompatActivity {

    private EditText name,password;
    private Button login;
    private SharedPreferenceHelper helper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        name=findViewById(R.id.edit_name);
        password=findViewById(R.id.edit_password);
        login=findViewById(R.id.button_login);

        helper=new SharedPreferenceHelper(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameText=name.getText().toString();
                String passwordText=password.getText().toString();
                new MyTask().execute(nameText,passwordText);
            }
        });
    }

    private class MyTask extends AsyncTask<String, Integer, String> {

        private ProgressDialog pDialog = null;
        //执行后台任务前做一些UI操作
        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(LoginActivity.this);

            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setMessage("正在登录……");
            // 设置ProgressDialog 是否可以按退回按键取消
            pDialog.setCanceledOnTouchOutside(false);
            // 让ProgressDialog显示
            pDialog.show();

        }

        //执行后台任务（耗时操作）,不可在此方法内修改UI
        @Override
        protected String doInBackground(String... params) {

            String result= HttpUtils.login(params[0],params[1]);
            return result;
        }

        //更新进度信息
        @Override
        protected void onProgressUpdate(Integer... progresses) {

        }

        //执行完后台任务后更新UI
        @Override
        protected void onPostExecute(String result) {
            pDialog.dismiss();
            System.out.println(result);
            if(result==null){
                Toast.makeText(LoginActivity.this,"服务器出现异常",Toast.LENGTH_SHORT).show();
            }else {
                LoginResult loginResult= JsonUtils.getLoginResult(result);
                if(loginResult.getMsg().equals("success")){

                    helper.saveUserId(loginResult.getUserId());
                    helper.saveUserName(loginResult.getUserName());
                    helper.saveUserSignature(loginResult.getSignature());

                    Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(LoginActivity.this,"登录失败",Toast.LENGTH_SHORT).show();
                }
            }


        }

        //取消执行中的任务时更改UI
        @Override
        protected void onCancelled() {

        }
    }



}
