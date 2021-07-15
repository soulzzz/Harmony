package com.xmut.harmony.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xmut.harmony.Adapter.OrderAdapter;
import com.xmut.harmony.R;
import com.xmut.harmony.entity.Order;
import com.xmut.harmony.entity.Result;
import com.xmut.harmony.entity.User;
import com.xmut.harmony.util.httputil.DatabaseUtil;
import com.xmut.harmony.util.httputil.http.HttpAddress;
import com.xmut.harmony.util.userutil.UserManage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CheckOrderActivity extends AppCompatActivity {
    TextView nullshow,allclick,waitpay,waitsend,waitreceive,success;
    ImageView leaveordercheck;
    RecyclerView recyclerview;
    List<Order> orderList;
    List<Order> orderShowList;
    Context context;
    User user ;
    int lastclick = -1;
    OrderAdapter orderAdapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_order);
        context = this;
        user = UserManage.getInstance().getUserInfo(context);
        getOrderList();
        init();


//        allclick.performClick();
//        if()



    }

    @Override
    protected void onResume() {
        super.onResume();

        if(orderList==null ||orderList.isEmpty()){
            Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
            nullshow.setVisibility(View.VISIBLE);
            
        }else{
            Intent it = getIntent();
            int clickwhich = it.getIntExtra("section",0);
            switch (clickwhich){
                case 0:
                    allclick.performClick();
                    break;
                case 1:
                    waitpay.performClick();
                    break;
                case 2:
                    waitsend.performClick();
                    break;
                case 3:
                    waitreceive.performClick();
                            break;
                case 4:success.performClick();
                break;
                default:allclick.performClick();
                break;
            }
        }
    }

    private void init() {
        orderShowList = new ArrayList<>();
        recyclerview = findViewById(R.id.recyclerview);

        leaveordercheck =findViewById(R.id.leaveordercheck);
        leaveordercheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        nullshow = findViewById(R.id.nullshow);

        allclick = findViewById(R.id.allclick);
        allclick.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                orderAdapter.setOrderList(orderList);
                if(lastclick !=0){
                    ResetOthers(lastclick);
                }
                lastclick =0;
                allclick.setTextColor(getColor(R.color.orange));

            }
        });
        waitpay = findViewById(R.id.waitpay);
        waitpay.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if(orderShowList.size()>0)
                {
                    orderShowList.clear();
                }
                for(Order order :orderList)
                {
                    if(order.getOrder_state().equals("待付款"))
                    {
                        orderShowList.add(order);
                    }
                }
                orderAdapter.setOrderList(orderShowList);
                if(lastclick !=1){
                    ResetOthers(lastclick);
                }
                lastclick =1;
                waitpay.setTextColor(getColor(R.color.orange));

            }
        });
        waitsend = findViewById(R.id.waitsend);
        waitsend.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if(orderShowList.size()>0)
                {
                    orderShowList.clear();
                }
                for(Order order :orderList)
                {
                    if(order.getOrder_state().equals("待发货"))
                    {
                        orderShowList.add(order);
                    }
                }
                orderAdapter.setOrderList(orderShowList);
                if(lastclick !=2){
                    ResetOthers(lastclick);
                }
                lastclick =2;
                waitsend.setTextColor(getColor(R.color.orange));
            }
        });
        waitreceive = findViewById(R.id.waitreceive);
        waitreceive.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if(orderShowList.size()>0)
                {
                    orderShowList.clear();
                }
                for(Order order :orderList)
                {
                    if(order.getOrder_state().equals("待收货"))
                    {
                        orderShowList.add(order);
                    }
                }
                orderAdapter.setOrderList(orderShowList);
                if(lastclick !=3){
                    ResetOthers(lastclick);
                }
                lastclick =3;
                waitreceive.setTextColor(getColor(R.color.orange));
            }
        });
        success = findViewById(R.id.success);
        success.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if(orderShowList.size()>0)
                {
                    orderShowList.clear();
                }
                for(Order order :orderList)
                {
                    if(order.getOrder_state().equals("已完成"))
                    {
                        orderShowList.add(order);
                    }
                }
                orderAdapter.setOrderList(orderShowList);
                if(lastclick !=4){
                    ResetOthers(lastclick);
                }
                lastclick =4;
                success.setTextColor(getColor(R.color.orange));
            }
        });
        //
        orderAdapter =new OrderAdapter(context);
        recyclerview.setAdapter(orderAdapter);
        recyclerview.setLayoutManager(new LinearLayoutManager(context,RecyclerView.VERTICAL,false));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void ResetOthers(int lastclick) {
        switch (lastclick){
            case 0:
                allclick.setTextColor(getColor(R.color.text_topbar));
                break;
            case 1:
                waitpay.setTextColor(getColor(R.color.text_topbar));
                break;
            case 2:
                waitsend.setTextColor(getColor(R.color.text_topbar));
                break;
            case 3:
                waitreceive.setTextColor(getColor(R.color.text_topbar));
                break;
            case 4:success.setTextColor(getColor(R.color.text_topbar));
                break;
        }
    }

    private void getOrderList() {

        Result result = DatabaseUtil.selectList(HttpAddress.get(HttpAddress.order(),"list",user.getId()));
        if(result.getCode()!=200){
            Toast.makeText(context, "获取订单失败", Toast.LENGTH_SHORT).show();
        }else{
            orderList = new ArrayList<>();
            orderList = DatabaseUtil.getObjectList(result,Order.class);
            Collections.sort(orderList);
            System.out.println("获取订单数"+orderList.size());
            for(Order order:orderList)
            {
                System.out.println(order);
            }
            Toast.makeText(context, "获取订单成功", Toast.LENGTH_SHORT).show();

        }
    }
}