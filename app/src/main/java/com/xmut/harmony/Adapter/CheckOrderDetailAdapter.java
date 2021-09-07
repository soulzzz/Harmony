package com.xmut.harmony.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xmut.harmony.Fragment.MainFragment;
import com.xmut.harmony.Activity.StoreActivity;
import com.xmut.harmony.R;
import com.xmut.harmony.entity.Order;
import com.xmut.harmony.entity.Product;
import com.xmut.harmony.entity.Store;

import java.util.ArrayList;
import java.util.List;

public class CheckOrderDetailAdapter extends RecyclerView.Adapter<CheckOrderDetailAdapter.Orderholder>{
    Context context;

    List<Order> orderList;
    List<Product> orderProducts;
    List<Store> store;

    public CheckOrderDetailAdapter(Context context) {
        this.context = context;
        orderList = new ArrayList<>();
        store =new ArrayList<>();
        orderProducts =new ArrayList<>();
        store = MainFragment.stores;
        System.out.println("商"+store.size());

    }

    public void setOrderList(List<Order> orderList) {
        if(this.orderList.size()>0){
            this.orderList.clear();
        }
        this.orderList.addAll(orderList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Orderholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.check_order_detial_item,null);
        return new Orderholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Orderholder holder, int position) {
        System.out.println("进入2");
        for(Store temp:store){
            if(temp.getStore_id()==orderList.get(position).getStore_id())
            {
                orderProducts = orderList.get(position).getOrderProducts();
                holder.storename.setText(temp.getStore_name());
                break;
            }
        }
        holder.storename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(context, StoreActivity.class);
                it.putExtra("storeid",orderList.get(position).getStore_id());
                context.startActivity(it);
            }
        });



        OrderProductAdapter2 orderProductAdapter2 =new OrderProductAdapter2(context);
        orderProductAdapter2.setOrderProductList(orderProducts);
        holder.recyclerView.setAdapter(orderProductAdapter2);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context,RecyclerView.VERTICAL,false));

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    static class Orderholder extends RecyclerView.ViewHolder{
        TextView storename;
        RelativeLayout ordersection;
        RecyclerView recyclerView;
        public Orderholder(@NonNull View itemView) {
            super(itemView);
            storename = itemView.findViewById(R.id.store_name);


            recyclerView =itemView.findViewById(R.id.orderproductrecyclerview);
            ordersection = itemView.findViewById(R.id.ordersection);


        }
    }
}
