package com.xmut.harmony.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xmut.harmony.Adapter.AddressAdapter;
import com.xmut.harmony.R;
import com.xmut.harmony.entity.UserAddress;
import com.xmut.harmony.util.userutil.UserManage;

import java.util.List;
/*
* 该活动用于查看本人收货地址
*
* */
public class AddressActivity extends AppCompatActivity {
    AddressAdapter adressAdapter;
    List<UserAddress> userAddressList;
    Context context;
    ImageView leavearrow;
    RecyclerView recyclerView;
    TextView nullshow ,addAddress;
    public static AddressActivity instance;
    UserManage userManage =UserManage.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        context = this;
        instance= this;
        //查询 addresslist
//        Result result = DatabaseUtil.selectUserAddressbyUserid(HttpAddress.get(HttpAddress.user(),"listuseraddress",MainActivity.curuser.getId()));
//        if(result.getCode()!=200)
//        {
//            Toast.makeText(this, "获取地址失败", Toast.LENGTH_SHORT).show();
//        }else{
//            userAddressList =new ArrayList<>();
//            userAddressList = DatabaseUtil.getObjectList(result,UserAddress.class);
//
//        }
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //重新获取本地的地址  防止更新后没有刷新数据
        userAddressList=userManage.getUserInfo(context).getAddress();
        if(userAddressList.isEmpty())
        {
            nullshow.setVisibility(View.VISIBLE);
        }else {
            nullshow.setVisibility(View.GONE);
        }
        adressAdapter.setAddressList(userAddressList);
//        //刷新数据
//        AddressAdapter temp =new AddressAdapter(context);
//        recyclerView.setAdapter(temp);


    }

    private void init() {
        addAddress = findViewById(R.id.addAddress);
        addAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it =  new Intent(context, ChangeAddressActivity.class);
                startActivity(it);
            }
        });

        leavearrow = findViewById(R.id.leaveaddress);
        leavearrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.addressrecycler);
        nullshow = findViewById(R.id.nullshow);

        recyclerView.setLayoutManager(new LinearLayoutManager(context,RecyclerView.VERTICAL,false));
         adressAdapter =new AddressAdapter(context);
        recyclerView.setAdapter(adressAdapter);

    }
}