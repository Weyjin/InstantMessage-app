package com.instant.message_app.constants;

public class SocketConstant {

    private SocketConstant(){}

    public static final String HOST="127.0.0.1:8080";//你自己的地址
    public static final String HOST_NAME="http://"+HOST;

    public static final String  CHAT_MESSAGE_ADDRESS="ws://"+HOST+"/websocket/OneToOne/";
    public static final String  GROUP_CHATS_MESSAGE_ADDRESS="ws://"+HOST+"/websocket/OneToMultiple/_g";
    public static final String  SCAN_CODE_ADDRESS="ws://"+HOST+"/websocket/ScanCode/";


    public static  final String GET_GROUPS_ADDRESS=HOST_NAME+"/api/getGroups";

    public static  final String GET_GROUP_CHATS_ADDRESS=HOST_NAME+"/api/getGroupChats";

    public static  final String LOGIN_ADDRESS=HOST_NAME+"/api/login";






}
