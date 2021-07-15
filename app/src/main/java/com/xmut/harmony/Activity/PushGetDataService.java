package com.xmut.harmony.Activity;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.huawei.hms.push.HmsMessageService;
import com.huawei.hms.push.RemoteMessage;
import com.xmut.harmony.entity.Result;
import com.xmut.harmony.entity.User;
import com.xmut.harmony.util.httputil.DatabaseUtil;
import com.xmut.harmony.util.httputil.http.HttpAddress;
import com.xmut.harmony.util.userutil.UserManage;

import java.util.Arrays;

import static com.xmut.harmony.Activity.MainActivity.curuser;

public class PushGetDataService extends HmsMessageService {
    private static final String TAG = "PushGetDataService";
    public static String PushToken ;
    UserManage userManage =UserManage.getInstance();
    // This method callback must be completed in 10 seconds. Otherwise, you need to start a new Job for callback processing.
    // extends HmsMessageService super class

    @Override
    public void onNewToken(String token) {
        Log.i(TAG, "received refresh token:" + token);
        PushToken = token;
        // send the token to your app server.
        if (!TextUtils.isEmpty(token)) {
            refreshedTokenToServer(token);
        }
    }

    @Override
    public void onMessageReceived(RemoteMessage message) {
        Log.i(TAG, "onMessageReceived is called");
        if (message == null) {
            Log.e(TAG, "Received message entity is null!");
            return;
        }

        Log.i(TAG, "getCollapseKey: " + message.getCollapseKey()
                + "\n getData: " + message.getData()
                + "\n getFrom: " + message.getFrom()
                + "\n getTo: " + message.getTo()
                + "\n getMessageId: " + message.getMessageId()
                + "\n getSendTime: " + message.getSentTime()
                + "\n getDataMap: " + message.getDataOfMap()
                + "\n getMessageType: " + message.getMessageType()
                + "\n getTtl: " + message.getTtl()
                + "\n getToken: " + message.getToken());

        RemoteMessage.Notification notification = message.getNotification();
        if (notification != null) {
            Log.i(TAG, "\n getImageUrl: " + notification.getImageUrl()
                    + "\n getTitle: " + notification.getTitle()
                    + "\n getTitleLocalizationKey: " + notification.getTitleLocalizationKey()
                    + "\n getTitleLocalizationArgs: " + Arrays.toString(notification.getTitleLocalizationArgs())
                    + "\n getBody: " + notification.getBody()
                    + "\n getBodyLocalizationKey: " + notification.getBodyLocalizationKey()
                    + "\n getBodyLocalizationArgs: " + Arrays.toString(notification.getBodyLocalizationArgs())
                    + "\n getIcon: " + notification.getIcon()
                    + "\n getSound: " + notification.getSound()
                    + "\n getTag: " + notification.getTag()
                    + "\n getColor: " + notification.getColor()
                    + "\n getClickAction: " + notification.getClickAction()
                    + "\n getChannelId: " + notification.getChannelId()
                    + "\n getLink: " + notification.getLink()
                    + "\n getNotifyId: " + notification.getNotifyId());
        }
        Boolean judgeWhetherIn10s = false;
        // If the messages are not processed in 10 seconds, the app needs to use WorkManager for processing.
        if (judgeWhetherIn10s) {
            startWorkManagerJob(message);
        } else {
            // Process message within 10s
            processWithin10s(message);
        }
    }
    private void startWorkManagerJob(RemoteMessage message) {
        Log.d(TAG, "Start new job processing.");
    }
    private void processWithin10s(RemoteMessage message) {
        Log.d(TAG, "Processing now.");
    }

    private void refreshedTokenToServer(String token) {
        Log.i(TAG, "sending token to server. token:" + token);
        if(curuser!=null) {

            if(curuser.getPushtoken()== null || !curuser.getPushtoken().equals(token))
            {
                curuser.setPushtoken(token);
                Log.d("Update user token","should update");
                Result result = DatabaseUtil.updateById(curuser,
                        HttpAddress.get(HttpAddress.user(),"update"));
                System.out.println(result.toString());
                if(result.getCode()!=200)
                {
                    Toast.makeText(MainActivity.mContext, "修改Token失败", Toast.LENGTH_SHORT).show();
                }else{

                    User user = DatabaseUtil.getEntity(result, User.class);
                    userManage.saveUserInfo(MainActivity.mContext,user);
                    Toast.makeText(MainActivity.mContext, "修改Token成功", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }
}
