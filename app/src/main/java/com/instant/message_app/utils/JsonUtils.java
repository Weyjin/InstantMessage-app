package com.instant.message_app.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.instant.message_app.entity.ChatMessage;
import com.instant.message_app.entity.Group;
import com.instant.message_app.entity.GroupChat;
import com.instant.message_app.entity.LoginResult;
import com.instant.message_app.entity.Result;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    public static ChatMessage getChatMessage(String json){
        JSONObject object=JSONObject.parseObject(json);
        ChatMessage chatMessage=new ChatMessage();
        chatMessage.setMessage(object.getString("message"));
        chatMessage.setCurrent(object.getBoolean("current"));
        chatMessage.setUser(getUser(object.getString("user")));
        return chatMessage;
    }
    public static List<Group> getGroups(String json){

        List<Group> groups=new ArrayList<>();
        JSONArray objects = JSON.parseArray(json);
        for (int i=0;i<objects.size();i++){
            JSONObject object = (JSONObject) objects.get(i);
            Group group=new Group();
            group.setId(Integer.parseInt(object.get("id").toString()));
            group.setName(object.get("name").toString());
            List<Result> users=getUsers(object.get("users").toString());
            group.setUsers(users);
            groups.add(group);
        }

        return groups;
    }

    private static Result getUser(String json){
        JSONObject object=JSONObject.parseObject(json);
        Result result=new Result();
        result.setName(object.getString("name"));
        result.setImg(object.getString("img"));
        return result;
    }

    public static LoginResult getLoginResult(String json){
        JSONObject object=JSONObject.parseObject(json);
        LoginResult result=new LoginResult();

        String msg=object.getString("msg");
        if(msg.equals("error")){
            result.setMsg(object.getString("msg"));
            return result;
        }else {
            result.setUserName(object.getString("userName"));
            result.setSignature(object.getString("signature"));
            result.setUserId(object.getInteger("userId"));
            result.setMsg(object.getString("msg"));
            return result;
        }

    }



    private static List<Result> getUsers(String json){
        List<Result> list = new ArrayList();
        JSONArray jsonArray = JSON.parseArray(json);

        for(int i=0;i<jsonArray.size();i++){
            Result result=new Result();
            result.setId(jsonArray.getJSONObject(i).getInteger("id"));
            result.setName(jsonArray.getJSONObject(i).getString("name"));
            result.setImg(jsonArray.getJSONObject(i).getString("img"));
            result.setDescribe(jsonArray.getJSONObject(i).getString("describe"));
            list.add(result);
        }
           return list;
    }


    public static List<GroupChat> getGroupChats(String json){
        List<GroupChat> groupChats=new ArrayList<>();
        JSONArray jsonArray = JSON.parseArray(json);

        for(int i=0;i<jsonArray.size();i++){
            GroupChat groupChat=new GroupChat();
            groupChat.setId(jsonArray.getJSONObject(i).getInteger("id"));
            groupChat.setName(jsonArray.getJSONObject(i).getString("name"));
            groupChat.setImg(jsonArray.getJSONObject(i).getString("img"));
            groupChat.setCount(jsonArray.getJSONObject(i).getInteger("count"));
            groupChats.add(groupChat);
        }

        return groupChats;

    }

}
