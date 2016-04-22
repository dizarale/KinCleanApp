package com.kinclean.dizarale.kinclean.service;


import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;


/**
 * Created by dizar on 2/2/2016.
 */
public class APP_config {
    public static String Host = "http://um-project.com/projectx/index.php/example_api/";
    static String SubRes = "sub=1";

    int sdkVersion = android.os.Build.VERSION.SDK_INT;

    public static String user_name = "";
    public static String user_tel = "";
    public static String imei = "";


    public APP_config(){}
    public APP_config(Context contex){

    }
    private String timestamp = "time="+(System.currentTimeMillis()/1000)+"";

    public int getSdkVersion(){
        return sdkVersion;
    }

    public String PostUser = Host+"cus?"+timestamp;
    public String GetAllMenu = Host+"menu?"+SubRes+"&&"+timestamp;
    public String PostPreorder = Host+"preorder"+"?"+timestamp;
    public String GetpPreorder = Host+"preorder?cus_tel="+user_tel+"&&"+timestamp;
    public String DelectPreorder = Host+"preorderdel?"+timestamp;
    public String PostcomfrimOrder = Host+"orderConfirm?"+timestamp;
    public String GetOrderUer = Host+"userorder?"+"cus_tel="+user_tel+"&&"+timestamp;

    String []loc_arr ={"DPU","DPU Build 1","DPU Build 2","DPU Build 3","DPU Build 4"};

    public String[] getLoc_arr() {
        return loc_arr;
    }
}
