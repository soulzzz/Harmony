package com.xmut.harmony.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xmut.harmony.R;
import com.xmut.harmony.entity.User;
import com.xmut.harmony.entity.Result;
import com.xmut.harmony.util.httputil.DatabaseUtil;
import com.xmut.harmony.util.httputil.http.HttpAddress;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.support.account.AccountAuthManager;
import com.huawei.hms.support.account.request.AccountAuthParams;
import com.huawei.hms.support.account.request.AccountAuthParamsHelper;
import com.huawei.hms.support.account.result.AuthAccount;
import com.huawei.hms.support.account.service.AccountAuthService;
import com.huawei.hms.support.hwid.ui.HuaweiIdAuthButton;
import com.xmut.harmony.util.userutil.ConstantValue;
import com.xmut.harmony.util.userutil.UserManage;

import static com.xmut.harmony.util.userutil.ConstantValue.LoginUser;
import static com.xmut.harmony.util.userutil.ConstantValue.RegisterUser;

public class LoginActivity extends AppCompatActivity {
    public static final int REQUEST_CODE = 1024;
    public static final String APP_ID = "104471839";
    String openid ;
    EditText username;
    EditText userpwd;
    HuaweiIdAuthButton huaweilogin;
    TextView register;
    FloatingActionButton loginbt;
    String TAG ="HUAWEI";
    Context mContext;
//    AccountAuthParams authParams;
//    AccountAuthService service;
    UserManage userManage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext = this;
        permission();
        userManage = UserManage.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        if(userManage.getUserInfo(mContext)!= null) //Obtain User Info ,Slient Sign in
        {
            //将头像存储到本地
            //只验证账号密码
            //成功则取本地头像
            User tempuser = userManage.getUserInfo(mContext);
            tempuser.setAvatar(null);
            Result result=DatabaseUtil.login(tempuser,
                    HttpAddress.get(HttpAddress.user(),"loginwithoutavatar"));
            if( result.getCode()!=200 )
            {
                Toast.makeText(mContext,"自动登录:"+result.getMsg(),Toast.LENGTH_SHORT).show();

                Intent it = new Intent(mContext,LoginActivity.class);
                startActivity(it);
                finish();
            }
            else{
                // MainActivity.curuser = user;
                System.out.println(result.toString()); //or user.toString
//                    Intent it = new Intent(LoginActivity.this,MainActivity.class);
//                    it.putExtra("user",user);
//                    startActivity(it);
//                    finish();
                userManage.Plunge(LoginActivity.this, MainActivity.class,userManage.getUserInfo(mContext), ConstantValue.Userbean);



            }

//            Intent it = new Intent(LoginActivity.this,MainActivity.class);
//            it.putExtra("user",userManage.getUserInfo(mContext));
//            startActivity(it);
//            finish();
        }else{
               //Got No User Info,Init LoginActivity
            initUI();
            //make sure to get the RegisterUserInfo
            User user = userManage.getPlunge(this,RegisterUser);
//        Intent it = this.getIntent();
//        User user =(User)it.getSerializableExtra(RegisterUser);
//        System.out.println(user);
            if(user!=null){
                username.setText(user.getUsername().toString());
                userpwd.setText(user.getPassword().toString());
            }
        }

        //　(1)当用户按下HOME键时。
        // (2)、长按HOME键，选择运行其他的程序时。
        // (3)、按下电源按键（关闭屏幕显示）时。
        // (4)、从activity A中启动一个新的activity时。
        //(5)、屏幕方向切换时，例如从竖屏切换到横屏时。
//        if (savedInstanceState != null) {
//            if(savedInstanceState.getSerializable(SavedUser)!=null) ;
//            userManage.Plunge(LoginActivity.this,MainActivity.class,(User)savedInstanceState.getSerializable(SavedUser));
////            Intent it = new Intent(LoginActivity.this,MainActivity.class);
////            startActivity(it);
////            finish();
//        }


    }

    private void permission() { //动态申请外部内存的写权限  根据返回码及返回结果查看是否需要再次申请权限 跳入onActivityResult
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){  // >= 30
            Log.d("Permission","Android 11");
//            Toast.makeText(this,"Android 11",Toast.LENGTH_SHORT).show();
            if(Environment.isExternalStorageManager())
            {
                //musicFiles = getAllAudio(this);
                //Toast.makeText(this, "size:"+musicFiles.size(), Toast.LENGTH_SHORT).show();
                //initViewPager();
            }
            else{
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
                startActivityForResult(intent, REQUEST_CODE);
            }

        }
        else{ // <30
            if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(LoginActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE);
            }
            else{
                //Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show();

                //Toast.makeText(this, "size:"+musicFiles.size(), Toast.LENGTH_SHORT).show();

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) { //ActivityCompat.requestPermissions的后继返回函数
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode ==REQUEST_CODE){
            if(grantResults[0] ==PackageManager.PERMISSION_GRANTED){
                //Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show();
                // musicFiles = getAllAudio(this);
                //Toast.makeText(this, "size:"+musicFiles.size(), Toast.LENGTH_SHORT).show();
                //initViewPager();
            }
            else{
                permission();
                //ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE);
            }
        }
    }
    private void initUI(){
        username = (EditText) findViewById(R.id.login_Username);
        userpwd = (EditText) findViewById(R.id.login_pwd);
        huaweilogin = findViewById(R.id.huaweilogin);
        register = findViewById(R.id.registerGOTO);
        loginbt = findViewById(R.id.loginbt);
        loginbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userManage.isEmpty(username.getText().toString()) ||userManage.isEmpty(userpwd.getText().toString()))
                {
                    Toast.makeText(mContext, "用户名或密码为空", Toast.LENGTH_SHORT).show();
                    return ;
                }
                //int id = Integer.parseInt(userid.getText().toString());
                String uname = userManage.trim(username.getText().toString());
                String password=userManage.trim(userpwd.getText().toString());
                User user1=new User(uname,password);


                //log in
                Result result=DatabaseUtil.login(user1,
                HttpAddress.get(HttpAddress.user(),"login"));
                if(result.getCode()!=200)
                {
                    Toast.makeText(mContext,result.getMsg(),Toast.LENGTH_SHORT).show();
                }
                else{
                    User user=DatabaseUtil.getEntity(result,User.class);
                   // MainActivity.curuser = user;
                    System.out.println(result.toString()); //or user.toString
//                    Intent it = new Intent(LoginActivity.this,MainActivity.class);
//                    it.putExtra("user",user);
//                    startActivity(it);
//                    finish();
                    userManage.Plunge(LoginActivity.this,MainActivity.class,user,LoginUser);
                    userManage.saveUserInfo(mContext,user); //保存登录的用户信息

                }
                /**插入单条数据
                 Result result=DatabaseUtil.insert(user1,
                 HttpAddress.get(HttpAddress.user(),"insert"));
                 System.out.println(result.toString());
                 // */

                /**删除单个数据
                 Result result=DatabaseUtil.deleteById(HttpAddress.
                 get(HttpAddress.user(),"delete",id));
                 System.out.println(result.toString());
                 // */

                /**更新单个数据
                 Result result=DatabaseUtil.updateById(user1,
                 HttpAddress.get(HttpAddress.user(),"update"));
                 System.out.println(result.toString());
                 //*/

                /**查单行数据
                 Result result=DatabaseUtil.selectLineById(
                 HttpAddress.get(HttpAddress.user(),"line",id));
                 User user=DatabaseUtil.getEntity(result,User.class);
                 System.out.println(user.toString());
                 // */

                /**查列表数据
                 Result result =DatabaseUtil.selectList(
                 HttpAddress.get(HttpAddress.user(),"list"));
                 List<User> users=DatabaseUtil.getList(result);
                 System.out.println(users.toString());
                 // */

            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);

            }
        });
//        huaweilogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                 authParams = new AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
//                         .setAuthorizationCode()
//                         .createParams();
//                 service = AccountAuthManager.getService(LoginActivity.this, authParams);
//                startActivityForResult(service.getSignInIntent(), 8888);
//            }
//        });

        //ID Token
        huaweilogin.setOnClickListener(new View.OnClickListener() { //ID Token
            @Override
            public void onClick(View v) {
                AccountAuthParams authParams = new AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
                        .setIdToken()
                        .createParams();
                AccountAuthService service = AccountAuthManager.getService(LoginActivity.this, authParams);
                startActivityForResult(service.getSignInIntent(), 8888);
            }
        });
    }
    //ID Token result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //授权登录结果处理，从AuthAccount中获取ID Token
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 8888) {
            Task<AuthAccount> authAccountTask = AccountAuthManager.parseAuthResultFromIntent(data);
            if (authAccountTask.isSuccessful()) {
                //登录成功，获取用户的帐号信息和ID Token
                AuthAccount authAccount = authAccountTask.getResult();
                openid = authAccount.getOpenId();
                Log.i(TAG, "openid:"+openid);
                Log.i(TAG,"idToken:" + authAccount.getIdToken());
                Log.i(TAG, " Email:"+authAccount.getEmail()+" "+authAccount.getDisplayName()+" "+authAccount.getGivenName());
                //获取帐号类型，0表示华为帐号、1表示AppTouch帐号
                Log.i(TAG, "accountFlag:" + authAccount.getAccountFlag());
                Result result=DatabaseUtil.selectLineByopenid(HttpAddress.get(HttpAddress.user(),"openid",openid));
                if(result.getCode()!=200)
                {
                    Toast.makeText(mContext,result.getMsg(),Toast.LENGTH_SHORT).show();
                }
                else{
                    if(!result.getResult().equals("null")) //数据库openid查找到user
                    {
                        User user=DatabaseUtil.getEntity(result,User.class);
                        // MainActivity.curuser = user;
                        System.out.println(result.toString()); //or user.toString
                        userManage.Plunge(LoginActivity.this,MainActivity.class,user,LoginUser);
                        userManage.saveUserInfo(mContext,user); //保存登录的用户信息
//                        Intent it = new Intent(LoginActivity.this,MainActivity.class);
//                        it.putExtra("user",user);
//
//                        startActivity(it);
//                        finish();
                    }else{ //数据库openid查找不到user，需要注册

                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext,android.R.style.Theme_Holo_Light_Dialog);
                        //builder.setIcon(R.drawable.ic_launcher);
                        builder.setTitle("Register Choice");
                        //    指定下拉列表的显示数据
                        final String[] items = {"Manual Register"};
                        AlertDialog.Builder alertdialog = new AlertDialog.Builder(this);
                        alertdialog.setTitle("Choose a Way to register").setItems(items, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case 0:
                                        Intent it = new Intent(mContext,RegisterActivity.class);
                                        it.putExtra(ConstantValue.OpenId,openid);
                                        startActivity(it);
                                        finish();

                                }

                            }
                        });
                        alertdialog.create().show();
                    }

                }

            } else {
                //登录失败，不需要做处理，打点日志方便定位
                Log.e(TAG, "sign in failed : " +((ApiException) authAccountTask.getException()).getStatusCode());
            }
        }
        if (requestCode == REQUEST_CODE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                //musicFiles = getAllAudio(this);
                //Toast.makeText(this, "size:"+musicFiles.size(), Toast.LENGTH_SHORT).show();
                //initViewPager();
            } else {
                Toast.makeText(this,"Fail ",Toast.LENGTH_SHORT).show();
                permission();
            }
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        //授权登录结果处理，从AuthAccount中获取Authorization Code
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 8888) {
//            Task<AuthAccount> authAccountTask = AccountAuthManager.parseAuthResultFromIntent(data);
//            if (authAccountTask.isSuccessful()) {
//                //登录成功，获取用户的帐号信息和Authorization Code
//                AuthAccount authAccount = authAccountTask.getResult();
//                Log.i(TAG, "serverAuthCode:" + authAccount.getAuthorizationCode());
//                Log.i(TAG, authAccount.getDisplayName() + " signIn success ");
//                Log.i(TAG, "AccessToken: " + authAccount.getAccessToken());
//            } else {
//                //登录失败
//                Log.e(TAG, "sign in failed:" + ((ApiException) authAccountTask.getException()).getStatusCode());
//            }
//        }
//    }
}