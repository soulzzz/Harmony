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

import com.xmut.harmony.Adapter.AddressChooseAdapter;
import com.xmut.harmony.R;
import com.xmut.harmony.entity.UserAddress;
import com.xmut.harmony.util.userutil.UserManage;

import java.util.List;

public class OrderAddressActivity extends AppCompatActivity {
    List<UserAddress> userAddressList;
    Context context;
    ImageView leavearrow;
    RecyclerView recyclerView;
    TextView nullshow ,addAddress;
    public static OrderAddressActivity instance;
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
        userAddressList=userManage.getUserInfo(context).getAddress();
        if(userAddressList.isEmpty())
        {
            nullshow.setVisibility(View.VISIBLE);
        }else {
            nullshow.setVisibility(View.GONE);
        }
        AddressChooseAdapter temp = new AddressChooseAdapter(context);
        recyclerView.setAdapter(temp);

    }

    private void init() {
        addAddress = findViewById(R.id.addAddress);
        addAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it =  new Intent(context,OrderChangeAddressActivity.class);
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




    }
}