package com.xmut.harmony.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huawei.hms.adapter.sysobs.SystemManager;
import com.xmut.harmony.Adapter.OrderProductAdapter;
import com.xmut.harmony.R;
import com.xmut.harmony.entity.Order;
import com.xmut.harmony.entity.Product;
import com.xmut.harmony.entity.Result;
import com.xmut.harmony.entity.Store;
import com.xmut.harmony.entity.UserAddress;
import com.xmut.harmony.util.httputil.DatabaseUtil;
import com.xmut.harmony.util.httputil.http.HttpAddress;
import com.xmut.harmony.util.userutil.CodeUtil;
import com.xmut.harmony.util.userutil.UserManage;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class OrderActivity extends AppCompatActivity {
    String strDateFormat = "yyyy-MM-dd HH:mm:ss";
    SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
    String orderid;
    Date date ;
    RelativeLayout userinfo,addresssection,nulladdress;

    List<Product> products;
    ImageView leaveorder;
    TextView payorder;
    Store store;
    Context context;
    RecyclerView recyclerView;
    OrderProductAdapter orderProductAdapter;
    TextView time,orderprice,orderaddress,orderusername,ordertel;
    public static OrderActivity instance;
    UserAddress userAddress ;
    double totalcount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        context = this;
        init();
        instance= this;


    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent it =getIntent();
        if(it.getSerializableExtra("useraddress")!=null){
            userAddress =(UserAddress) it.getSerializableExtra("useraddress");
        }else{
            if( !UserManage.getInstance().getUserInfo(context).getAddress().isEmpty() )
            {
                userAddress =UserManage.getInstance().getUserInfo(context).getAddress().get(0);
            }else{
                userAddress = null;
            }
        }

        if(userAddress!=null){
            orderaddress.setText(String.valueOf(userAddress.getUser_address() ));
            orderusername.setText(String.valueOf(userAddress.getUser_name()));
            ordertel.setText(String.valueOf(userAddress.getUser_tel()));
        }else{
            addresssection.setVisibility(View.GONE);
            nulladdress.setVisibility(View.VISIBLE);
        }

    }

    private void init() {
        products = new ArrayList<>();
        orderid = CodeUtil.createOrderId();
//        int i=0;
//        for (Map.Entry< Product, Integer> entry : StoreActivity.myorder.entrySet()) {
//            try {
//                products.add((Product) entry.getKey().clone());
//            } catch (CloneNotSupportedException e) {
//                e.printStackTrace();
//            }
//            products.get(i).setProduct_num(entry.getValue());
//            products.get(i).setProduct_stock( products.get(i).getProduct_stock()-entry.getValue());
//            products.get(i).setOrder_id(orderid);
//            totalcount += (products.get(i).getProduct_price()*products.get(i).getProduct_num());
//            i++;
//        }
        products = (List<Product>) getIntent().getSerializableExtra("list");
        for(Product product :products){
            totalcount+=product.getProduct_price()*product.getProduct_num();
            product.setProduct_stock(product.getProduct_stock()-product.getProduct_num() );
            product.setOrder_id(orderid);
        }
//        if(UserManage.getInstance().getUserInfo(context).getAddress()!=null
//                &&UserManage.getInstance().getUserInfo(context).getAddress().get(0)!=null)
//        {
//            userAddress =UserManage.getInstance().getUserInfo(context).getAddress().get(0);
//        }
        addresssection =findViewById(R.id.addresssection);
        nulladdress = findViewById(R.id.nulladdress);
        leaveorder = findViewById(R.id.leaveorder);
        leaveorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userAddress != null) { //选择地址后才可以保存订单 没有的话直接返回

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("是否保存订单？");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            System.out.println("zzz"+orderid);
                            //插入订单
                            Order order = new Order();
                            order.setOrder_id(orderid);
                            order.setUser_id(UserManage.getInstance().getUserInfo(context).getId());
                            order.setStore_id(StoreActivity.store.getStore_id());
                            order.setUser_address(userAddress.getUser_address());
                            order.setUser_name(userAddress.getUser_name());
                            order.setUser_tel(userAddress.getUser_tel());
                            order.setOrder_state(1);
                            order.setOrder_price(totalcount);
                            order.setOrder_date(sdf.format(date));

                            order.setOrderProducts(products);
                            Result result = DatabaseUtil.insert(order, HttpAddress.get(HttpAddress.order(), "insert"));
                            if(result.getCode()!=200)
                            {
                                Toast.makeText(context, "保存订单失败", Toast.LENGTH_SHORT).show();
                            }else{

                                Toast.makeText(context, "保存订单成功", Toast.LENGTH_SHORT).show();
                                finish();
                            }

                        }
                    });
                    //    设置一个NegativeButton
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();

                        }
                    });
                    builder.show();
                } else {
                    finish();
                }
            }

        });
        payorder = findViewById(R.id.pay);
        payorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(userAddress!=null ){

                    //插入订单
                    Order order = new Order();
                    order.setOrder_id(orderid);

                    System.out.println(order.getOrder_id());
                    order.setUser_id(UserManage.getInstance().getUserInfo(context).getId());
                    order.setStore_id(products.get(0).getStore_id() );
                    order.setUser_address(userAddress.getUser_address());
                    order.setUser_name(userAddress.getUser_name());
                    order.setUser_tel(userAddress.getUser_tel());
                    order.setOrder_state(2);
                    order.setOrder_price(totalcount);
                    order.setOrder_date(sdf.format(date));

                    order.setOrderProducts(products);
                    System.out.println(order);
                    Result result = DatabaseUtil.insert(order, HttpAddress.get(HttpAddress.order(),"insert"));
                    if(result.getCode()!=200)
                    {
                        Toast.makeText(context, "插入订单失败", Toast.LENGTH_SHORT).show();
                    }else{

                        Toast.makeText(context, "购买成功", Toast.LENGTH_SHORT).show();
                        for(Product temp:products)
                        {
                            Result result1 = DatabaseUtil.updateById(temp, HttpAddress.get(HttpAddress.product(),"update"));//更新库存
                        }
                        finish();
                        if(StoreActivity.instance!=null){
                            StoreActivity.instance.finish();
                        }
                        if(GoodDetailActivity.instance!=null){
                            GoodDetailActivity.instance.finish();
                        }

                    }

                }else{
                    Toast.makeText(context, "地址为空，请选择地址", Toast.LENGTH_SHORT).show();
                }

            }
        });
        userinfo = findViewById(R.id.userinfo);
        userinfo.setOnClickListener(new View.OnClickListener() { //选地址
            @Override
            public void onClick(View v) {
                Intent it = new Intent(context,OrderAddressActivity.class);
                startActivity(it);
            }
        });
        time =findViewById(R.id.time);
        date = new Date();
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        time.setText(sdf.format(date));

        orderprice =findViewById(R.id.price);
        DecimalFormat df = new DecimalFormat("0.00");
        totalcount = Double.parseDouble(df.format(totalcount));
        orderprice.setText(String.valueOf(totalcount));

        orderaddress = findViewById(R.id.address);
        orderusername = findViewById(R.id.name);
        ordertel = findViewById(R.id.tel);

//        if(userAddress!=null){
//            orderaddress.setText(String.valueOf(userAddress.getUser_address() ));
//            orderusername.setText(String.valueOf(userAddress.getUser_name()));
//            ordertel.setText(String.valueOf(userAddress.getUser_tel()));
//        }else{
//            addresssection.setVisibility(View.GONE);
//            nulladdress.setVisibility(View.VISIBLE);
//        }


        store = StoreActivity.store;
        recyclerView =findViewById(R.id.recyclerview);


        orderProductAdapter = new OrderProductAdapter(context);
        orderProductAdapter.setProductList(products);
        recyclerView.setAdapter(orderProductAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context,RecyclerView.VERTICAL,false));
    }
}