package com.xmut.harmony.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xmut.harmony.Activity.CheckOrderDetailActivity;
import com.xmut.harmony.Activity.CommentActivity;
import com.xmut.harmony.Activity.LoginActivity;
import com.xmut.harmony.Activity.MainActivity;
import com.xmut.harmony.Fragment.MainFragment;
import com.xmut.harmony.Activity.StoreActivity;
import com.xmut.harmony.R;
import com.xmut.harmony.entity.Order;
import com.xmut.harmony.entity.Product;
import com.xmut.harmony.entity.Result;
import com.xmut.harmony.entity.Store;
import com.xmut.harmony.util.httputil.DatabaseUtil;
import com.xmut.harmony.util.httputil.http.HttpAddress;
import com.xmut.harmony.util.userutil.ConstantValue;
import com.xmut.harmony.util.userutil.OrderUtil;

import java.util.ArrayList;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.Orderholder>{
    Context context;

    List<Order> orderList;
    List<Product> orderProducts;
    List<Store> store;
    int page = -1;
    public OrderAdapter(Context context) {
        this.context = context;
        orderList = new ArrayList<>();
         store =new ArrayList<>();
        orderProducts =new ArrayList<>();
        store = MainFragment.stores;

    }

    public void setOrderList(List<Order> orderList) {
        if(this.orderList.size()>0){
            this.orderList.clear();
        }
        this.orderList.addAll(orderList);
        notifyDataSetChanged();
    }
    public void setPage(int page){
        this.page = page;
    }

    @NonNull
    @Override
    public Orderholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.order_item,null);
        return new Orderholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Orderholder holder, int position) {
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

        holder.state.setText( OrderUtil.getStateName(orderList.get((position)).getOrder_state() ) );
        holder.state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(orderList.get((position)).getOrder_state()  ==0){ //已取消
                    Toast.makeText(context,"订单已取消，可查看详情",Toast.LENGTH_SHORT).show();

                }else if(orderList.get((position)).getOrder_state()==1){ //待付款
                    orderList.get((position)).setOrder_state(2);
                    Result result= DatabaseUtil.updateById(orderList.get((position)),HttpAddress.get(HttpAddress.order(),"update"));
                    if( result.getCode()!=200 )
                    {
                        Toast.makeText(context,"订单:"+result.getMsg(),Toast.LENGTH_SHORT).show();
                        orderList.get((position)).setOrder_state(1);
                    }
                    else{
                        if(page !=1)
                        orderList.remove(position);
                        notifyDataSetChanged();
                    }

                }else if(orderList.get((position)).getOrder_state() == 2){ //待发货
                    Toast.makeText(context,"请耐心等待发货",Toast.LENGTH_SHORT).show();

                }else if(orderList.get((position)).getOrder_state() ==3 ){ //待收货
                    orderList.get((position)).setOrder_state(4);
                    Result result= DatabaseUtil.updateById(orderList.get((position)),HttpAddress.get(HttpAddress.order(),"update"));
                    if( result.getCode()!=200 )
                    {
                        Toast.makeText(context,"订单:"+result.getMsg(),Toast.LENGTH_SHORT).show();
                        orderList.get((position)).setOrder_state(3);
                    }
                    else{
                        if(page !=1)
                        orderList.remove(position);
                        Toast.makeText(context,"收货成功:",Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged();
                    }

                }else if(orderList.get((position)).getOrder_state() ==4){ //待评价
                    Toast.makeText(context,"评价",Toast.LENGTH_SHORT).show();
                    Intent it =new Intent(context, CommentActivity.class);
                    it.putExtra("order",orderList.get((position)) );
                    it.putExtra("page",page);
                    ((Activity)context).startActivityForResult(it,3);

                }

            }
        });
        holder.orderprice.setText(String.valueOf(orderList.get(position).getOrder_price()));

        OrderProductAdapter2 orderProductAdapter2 =new OrderProductAdapter2(context);
        orderProductAdapter2.setOrderProductList(orderProducts);
        holder.recyclerView.setAdapter(orderProductAdapter2);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context,RecyclerView.VERTICAL,false));
        holder.detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(context, CheckOrderDetailActivity.class);
                it.putExtra("order",orderList.get(position));
                it.putExtra("page",page);
                ((Activity)context).startActivityForResult(it,3);
            }
        });

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }


    static class Orderholder extends RecyclerView.ViewHolder{
        TextView storename,state,orderprice,detail;
        RelativeLayout ordersection;
        RecyclerView recyclerView;
        public Orderholder(@NonNull View itemView) {
            super(itemView);
            storename = itemView.findViewById(R.id.store_name);
            state =itemView.findViewById(R.id.state);
            orderprice =itemView.findViewById(R.id.orderprice);
            detail = itemView.findViewById(R.id.detail);
            recyclerView =itemView.findViewById(R.id.orderproductrecyclerview);
            ordersection = itemView.findViewById(R.id.ordersection);


        }
    }
}
