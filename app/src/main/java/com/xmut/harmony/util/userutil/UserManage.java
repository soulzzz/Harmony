package com.xmut.harmony.util.userutil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.xmut.harmony.entity.User;

import java.util.regex.Pattern;

public class UserManage {
    SharedPreferences sp;
    private static UserManage instance;

    private UserManage() {
    }

    public static UserManage getInstance() {
        if (instance == null) {
            instance = new UserManage();
        }
        return instance;
    }
    /**
     * 保存自动登录的用户信息
     */
    public void saveUserInfo(Context context, User user) {
        clearUserInfo(context);
       sp = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);//Context.MODE_PRIVATE表示SharePrefences的数据只有自己应用程序能访问。
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        String json = gson.toJson(user);
        editor.putString(ConstantValue.Userbean, json);
        editor.commit();
        Log.d("GetSharedPreferences","Saved user info");
    }


    /**
     * 获取用户信息model
     *
     * @param context
     * @param
     * @param
     */
    public User getUserInfo(Context context) {
        if (sp == null) {
            sp = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
            Log.d("GetSharedPreferences","Got user info");
        }
        Gson gson = new Gson();
        String json = sp.getString(ConstantValue.Userbean, null);
        if(json !=null){
            User user = gson.fromJson(json, User.class);
            return user;
        }else
        return null;
    }

    public void clearUserInfo(Context context){
        if (sp == null) {
            sp = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        }
        Log.d("GetSharedPreferences","Cleared user info");
        sp.edit().clear().commit();
    }
    public void Plunge(Activity a1, Class a2, User user,String method){
        Intent it = new Intent(a1,a2);
        it.putExtra(method,user);
        Log.d(method,"Send user info");
        a1.startActivity(it);
        a1.finish();
    }
    public User getPlunge(Activity a1,String method){
        Intent it = a1.getIntent();
        User user =(User)it.getSerializableExtra(method);
        if(user!=null){
            Log.d(method,"Got user info");
            return user;
        }
        else
        {
            Log.d(method,"Can't get user info");
            return null;
        }
    }
    public static boolean isEmpty(String value) {
        return TextUtils.isEmpty(value) || value.trim().length() == 0;
    }
    public static String trim(String input){

        return Pattern.compile("\t|\r|\n| ").matcher(input).replaceAll("");
    }


}

