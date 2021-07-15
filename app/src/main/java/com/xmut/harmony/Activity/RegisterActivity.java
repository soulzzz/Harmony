package com.xmut.harmony.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.xmut.harmony.R;
import com.xmut.harmony.entity.Result;
import com.xmut.harmony.entity.User;
import com.xmut.harmony.util.httputil.DatabaseUtil;
import com.xmut.harmony.util.httputil.http.HttpAddress;
import com.xmut.harmony.util.userutil.ConstantValue;
import com.xmut.harmony.util.userutil.UserManage;

public class RegisterActivity extends AppCompatActivity {
    EditText reg_username;
    EditText reg_email;
    EditText reg_pwd;
    EditText reg_repwd;
    String openid ;
    FloatingActionButton button;
    Context mContext;
    UserManage userManage = UserManage.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Intent it = getIntent();
        openid = it.getStringExtra(ConstantValue.OpenId);
        mContext = this;
        initUI();
    }

    private void initUI() {
        reg_username = findViewById(R.id.reg_username);
        reg_email = findViewById(R.id.reg_email);
        reg_pwd = findViewById(R.id.reg_pwd);
        reg_repwd = findViewById(R.id.reg_repwd);
        button = findViewById(R.id.reg_goto);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userManage.isEmpty(reg_username.getText().toString())){
                    Toast.makeText(mContext, "用户名不能少于3位", Toast.LENGTH_SHORT).show();
                    return;
                } else if (reg_username.getText().toString().length() <= 3 ) {
                    Toast.makeText(mContext, "用户名少于3位", Toast.LENGTH_SHORT).show();
                    return;
                } else if (userManage.isEmpty(reg_pwd.getText().toString())) {
                    Toast.makeText(mContext, "用户密码为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (reg_pwd.getText().toString().length()<= 3) {
                    Toast.makeText(mContext, "用户密码不能少于3位", Toast.LENGTH_SHORT).show();
                    return;
                } else if (userManage.isEmpty(reg_email.getText().toString())) {
                    Toast.makeText(mContext, "用户邮箱为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (!reg_email.getText().toString().contains("@") || !reg_email.getText().toString().contains(".com") )
                {
                    Toast.makeText(mContext, "用户邮箱格式错误", Toast.LENGTH_SHORT).show();
                    return;
                }else if (!reg_pwd.getText().toString().equals(reg_repwd.getText().toString())) {
                    Toast.makeText(mContext, "两次密码输入不同", Toast.LENGTH_SHORT).show();
                    return;
                }
                String username = userManage.trim(reg_username.getText().toString());
                String email = userManage.trim(reg_email.getText().toString());
                String pwd = userManage.trim(reg_pwd.getText().toString());
                User user;
                if(openid!=null)
                {
                    user = new User(username,pwd,email,openid);
                }else {
                    user = new User(username, pwd, email);
                }
                if(PushGetDataService.PushToken !=null)
                {
                    user.setPushtoken(PushGetDataService.PushToken);
                }

                Result result= DatabaseUtil.insert(user,
                 HttpAddress.get(HttpAddress.user(),"insert"));
                if(result.getCode()!=200)
                {
                    Toast.makeText(mContext, result.getMsg(), Toast.LENGTH_SHORT).show();
                }else{
                    User user1=DatabaseUtil.getEntity(result,User.class);
                    System.out.println("inserted User: "+user1);
                    // MainActivity.curuser = user;
                    System.out.println(result.toString()); //or user.toString
                    userManage.Plunge(RegisterActivity.this,LoginActivity.class,user1,ConstantValue.RegisterUser);
//                    Intent it = new Intent(RegisterActivity.this,LoginActivity.class);
//                    it.putExtra(ConstantValue.RegisterUser,(Serializable)user1);
//                    startActivity(it);
//                    finish();
                }


            }
        });
    }

}