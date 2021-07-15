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

import com.xmut.harmony.Adapter.CheckOrderDetailAdapter;
import com.xmut.harmony.Adapter.OrderAdapter;
import com.xmut.harmony.R;
import com.xmut.harmony.entity.Order;

import java.util.ArrayList;
import java.util.List;

public class CheckOrderDetailActivity extends AppCompatActivity {
    ImageView leaveorder;
    RecyclerView recyclerview;
    TextView time,name1,tel1,add1,state,price;
    Order order;
    List<Order> templist=new ArrayList<>();
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_order_detail);
        context =this;
        Intent it = getIntent();
        order = (Order) it.getSerializableExtra("order");
        if(order!=null){
            templist.add(order);
        }

        init();

    }

    private void init() {
        price = findViewById(R.id.price);
        recyclerview = findViewById(R.id.recyclerview);
        time = findViewById(R.id.time);
        name1 =findViewById(R.id.name1);
        tel1 =findViewById(R.id.tel1);
        add1 = findViewById(R.id.add1);
        state =findViewById(R.id.state);
        price.setText(String.valueOf(order.getOrder_price()) );
        state.setText(order.getOrder_state());
        time.setText(order.getOrder_date());
        name1.setText(order.getUser_name());
        tel1.setText(order.getUser_tel());
        add1.setText(order.getUser_address());

        leaveorder = findViewById(R.id.leaveorder);
        leaveorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        CheckOrderDetailAdapter adapter = new CheckOrderDetailAdapter(context);
        adapter.setOrderList(templist);
        recyclerview.setAdapter(adapter);
        recyclerview.setLayoutManager(new LinearLayoutManager(context,RecyclerView.VERTICAL,false) );

    }
}