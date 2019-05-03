package com.instant.message_app.utils;

import android.graphics.Bitmap;

import com.instant.message_app.constants.SocketConstant;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtils {

    public static String getGroups(){

        OkHttpClient client = new OkHttpClient();

        try {
            Request request = new Request.Builder().url(SocketConstant.GET_GROUPS_ADDRESS).build();
            Response response = null;
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                throw new IOException("Unexpected code " + response);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
      return null;

    }


    public static String getGroupChats(String id){
        OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("userId", id)
                    .build();

            Request request = new Request.Builder()
                    .url(SocketConstant.GET_GROUP_CHATS_ADDRESS)
                    .post(formBody)
                    .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                throw new IOException("Unexpected code " + response);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
       return null;

    }


    public static String login(String name,String password){

        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("name", name)
                .add("password",password)
                .build();

        Request request = new Request.Builder()
                .url(SocketConstant.LOGIN_ADDRESS)
                .post(formBody)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                throw new IOException("Unexpected code " + response);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }


}
