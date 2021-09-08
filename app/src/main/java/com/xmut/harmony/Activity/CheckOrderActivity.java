package com.xmut.harmony.Activity;

import androidx.annotation.Nullable;
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
    TextView nullshow, allclick, waitpay, waitsend, waitreceive, success;
    ImageView leaveordercheck;
    RecyclerView recyclerview;
    List<Order> orderList = new ArrayList<>();
    List<Order> orderShowList;
    Context context;
    User user;
    int lastclick = -1;
    OrderAdapter orderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_order);
        context = this;
        user = UserManage.getInstance().getUserInfo(context);
//        getOrderList();
        init();
        Intent it = getIntent();
        int clickwhich = it.getIntExtra("section", -1);
        switch (clickwhich) {
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
            case 4:
                success.performClick();
                break;
            default:
                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    private void init() {
        orderShowList = new ArrayList<>();
        recyclerview = findViewById(R.id.recyclerview);

        leaveordercheck = findViewById(R.id.leaveordercheck);
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
                getOrderList(-1);
                orderAdapter.setOrderList(orderList);
                orderAdapter.setPage(1);
                if (lastclick != 0) {
                    ResetOthers(lastclick);
                }
                lastclick = 0;
                allclick.setTextColor(getColor(R.color.orange));

                if (orderList == null || orderList.isEmpty()) {
                    nullshow.setVisibility(View.VISIBLE);
                } else {
                    nullshow.setVisibility(View.INVISIBLE);
                }

            }
        });
        waitpay = findViewById(R.id.waitpay);
        waitpay.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                getOrderList(1);
                orderAdapter.setOrderList(orderList);
                orderAdapter.setPage(2);
                if (lastclick != 1) {
                    ResetOthers(lastclick);
                }
                lastclick = 1;
                waitpay.setTextColor(getColor(R.color.orange));
                if (orderList == null || orderList.isEmpty()) {
                    nullshow.setVisibility(View.VISIBLE);
                } else {
                    nullshow.setVisibility(View.INVISIBLE);
                }
            }
        });
        waitsend = findViewById(R.id.waitsend);
        waitsend.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                getOrderList(2);
                orderAdapter.setOrderList(orderList);
                orderAdapter.setPage(3);
                if (lastclick != 2) {
                    ResetOthers(lastclick);
                }
                lastclick = 2;
                waitsend.setTextColor(getColor(R.color.orange));
                if (orderList == null || orderList.isEmpty()) {
                    nullshow.setVisibility(View.VISIBLE);
                } else {
                    nullshow.setVisibility(View.INVISIBLE);
                }
            }
        });
        waitreceive = findViewById(R.id.waitreceive);
        waitreceive.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                getOrderList(3);
                orderAdapter.setOrderList(orderList);
                orderAdapter.setPage(4);
                if (lastclick != 3) {
                    ResetOthers(lastclick);
                }
                lastclick = 3;
                waitreceive.setTextColor(getColor(R.color.orange));
                if (orderList == null || orderList.isEmpty()) {
                    nullshow.setVisibility(View.VISIBLE);
                } else {
                    nullshow.setVisibility(View.INVISIBLE);
                }
            }
        });
        success = findViewById(R.id.success);
        success.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                getOrderList(4);
                orderAdapter.setOrderList(orderList);
                orderAdapter.setPage(5);
                if (lastclick != 4) {
                    ResetOthers(lastclick);
                }
                lastclick = 4;
                success.setTextColor(getColor(R.color.orange));
                if (orderList == null || orderList.isEmpty()) {
                    nullshow.setVisibility(View.VISIBLE);
                } else {
                    nullshow.setVisibility(View.INVISIBLE);
                }
            }
        });
        //
        orderAdapter = new OrderAdapter(context);
        recyclerview.setAdapter(orderAdapter);
        recyclerview.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void ResetOthers(int lastclick) {
        switch (lastclick) {
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
            case 4:
                success.setTextColor(getColor(R.color.text_topbar));
                break;
        }
    }

    private void getOrderList() {

        Result result = DatabaseUtil.selectList(HttpAddress.get(HttpAddress.order(), "list", user.getId()));
        if (result.getCode() != 200) {
            Toast.makeText(context, "获取订单失败", Toast.LENGTH_SHORT).show();
        } else {
            orderList = new ArrayList<>();
            orderList = DatabaseUtil.getObjectList(result, Order.class);
            Collections.sort(orderList);
            System.out.println("获取订单数" + orderList.size());
            for (Order order : orderList) {
                System.out.println(order);
            }
            Toast.makeText(context, "获取订单成功", Toast.LENGTH_SHORT).show();

        }
    }

    private void getOrderList(int state) {

        Result result = DatabaseUtil.selectList(HttpAddress.get(HttpAddress.order(), "list", user.getId()));
        if (result.getCode() != 200) {
            Toast.makeText(context, "获取订单失败", Toast.LENGTH_SHORT).show();
        } else {
            if (orderList.size() > 0) {
                orderList.clear();
            }
            List<Order> temp = DatabaseUtil.getObjectList(result, Order.class);
            Collections.sort(orderList);
            if (state != -1) {
                for (Order order : temp) {
                    if (order.getOrder_state() == state) {
                        orderList.add(order);
                    }
                }
            } else {
                orderList = temp;
            }
            orderAdapter.notifyDataSetChanged();

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent it) {
        super.onActivityResult(requestCode, resultCode, it);
        if (requestCode == 3 && resultCode == RESULT_OK) {
            int page =  it.getIntExtra("page",1);
            switch (page) {
                case 1:
                    allclick.performClick();
                    break;
                case 2:
                    waitpay.performClick();
                    break;
                case 3:
                    waitsend.performClick();
                    break;
                case 4:
                    waitreceive.performClick();
                    break;
                case 5:
                    success.performClick();
                    break;
                default:
                    break;
            }

        }
    }
}