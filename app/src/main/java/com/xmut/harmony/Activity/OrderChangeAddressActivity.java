package com.xmut.harmony.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xmut.harmony.R;
import com.xmut.harmony.Video.utils.StringUtil;
import com.xmut.harmony.entity.Result;
import com.xmut.harmony.entity.User;
import com.xmut.harmony.entity.UserAddress;
import com.xmut.harmony.util.httputil.DatabaseUtil;
import com.xmut.harmony.util.httputil.http.HttpAddress;
import com.xmut.harmony.util.userutil.UserManage;

import java.util.List;

public class OrderChangeAddressActivity extends AppCompatActivity {
    ImageView leaveaddress;
    TextView deletebt1,saveaddress;
    EditText tel,name,address;
    UserAddress useraddress;
    UserManage userManage = UserManage.getInstance();
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_address);
        context = this;
        init();
        Intent it =getIntent();
        if(it.getSerializableExtra("address")!=null)
        {
            useraddress = (UserAddress) it.getSerializableExtra("address");
            deletebt1.setVisibility(View.VISIBLE);
            tel.setText(useraddress.getUser_tel());
            name.setText(useraddress.getUser_name());
            address.setText(useraddress.getUser_address());
        }else{
            useraddress =new UserAddress();
            useraddress.setUser_id(userManage.getUserInfo(context).getId());
        }

    }

    private void init() {
        leaveaddress = findViewById(R.id.leaveaddress);
        deletebt1 = findViewById(R.id.deletebt1);
        saveaddress = findViewById(R.id.saveaddress);
        tel = findViewById(R.id.tel);
        name = findViewById(R.id.name);
        address = findViewById(R.id.address);

        leaveaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        deletebt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Result result = DatabaseUtil.deleteById(HttpAddress.get(HttpAddress.user(),"deleteuseraddress",useraddress.getAddress_id() ) );
                if(result.getCode()!=200)
                {
                    Toast.makeText(context, "删除地址失败", Toast.LENGTH_SHORT).show();
                }else{
                    Result result1 = DatabaseUtil.selectList(HttpAddress.get(HttpAddress.user(),"listUserAddress",useraddress.getUser_id()));
                    List<UserAddress> temp  =DatabaseUtil.getObjectList(result1,UserAddress.class);
                    User curuser = userManage.getUserInfo(context);
                    curuser.setAddress(temp);
                    userManage.saveUserInfo(context,curuser);

                    Toast.makeText(context, "删除地址成功", Toast.LENGTH_SHORT).show();
                    if(OrderAddressActivity.instance!=null)
                    {
                        OrderAddressActivity.instance.finish();
                    }
                    Intent it = new Intent(context,OrderAddressActivity.class);
                    startActivity(it);
                    finish();
                }
            }
        });

        saveaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!StringUtil.isEmpty(tel.getText().toString()) &&!StringUtil.isEmpty(name.getText().toString()) &&!StringUtil.isEmpty(address.getText().toString()))
                {
                    useraddress.setUser_address( address.getText().toString());
                    useraddress.setUser_name(name.getText().toString());
                    useraddress.setUser_tel(tel.getText().toString());
                    Result result;
                    if(useraddress.getAddress_id()!=null) //update
                    {
                        result = DatabaseUtil.updateById(useraddress,HttpAddress.get(HttpAddress.user(),"updateuseraddress"));
                    }else{ //insert
                        result = DatabaseUtil.insert(useraddress,HttpAddress.get(HttpAddress.user(),"insertuseraddress"));
                    }
                    if(result.getCode()!=200)
                    {
                        Toast.makeText(context, "更新收货地址失败", Toast.LENGTH_SHORT).show();
                    }else{
                        List<UserAddress> temp = DatabaseUtil.getObjectList(result, UserAddress.class);
                        User curuser = userManage.getUserInfo(context);
                        curuser.setAddress(temp);
                        userManage.saveUserInfo(context,curuser);
                        System.out.println(userManage.getUserInfo(context).getAddress() );
                        Toast.makeText(context, "更新收货地址成功", Toast.LENGTH_SHORT).show();
                        if(OrderAddressActivity.instance!=null)
                        {
                            OrderAddressActivity.instance.finish();
                        }
                        Intent it = new Intent(context,OrderAddressActivity.class);
                        startActivity(it);
                        finish();

                    }

                }else{
                    Toast.makeText(context, "各项信息不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}