package com.instant.message_app.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class DisplayUtil {


    public static int getWidth(Context context){
        int widthPixels = getDisplayMetrics(context).widthPixels;
        return widthPixels;
    }

    public static int getHeight(Context context){
        int heightPixels = getDisplayMetrics(context).heightPixels;
        return heightPixels;
    }

    private static DisplayMetrics getDisplayMetrics(Context context){
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if(windowManager==null){
            return null;
        }
        DisplayMetrics outMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getRealMetrics(outMetrics);
        return outMetrics;
    }
}
