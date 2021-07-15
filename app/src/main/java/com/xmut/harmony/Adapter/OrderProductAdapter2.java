package com.xmut.harmony.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.xmut.harmony.R;
import com.xmut.harmony.entity.OrderProduct;
import com.xmut.harmony.entity.Product;

import java.util.ArrayList;
import java.util.List;

public class OrderProductAdapter2 extends RecyclerView.Adapter<OrderProductAdapter2.holder> {
    Context context;
    List<OrderProduct> orderProducts;
    public OrderProductAdapter2(Context context) {
        this.context = context;
        orderProducts =new ArrayList<>();
    }
    public void setOrderProductList(List<OrderProduct> input){
        if (this.orderProducts.size() > 0) {
            this.orderProducts.clear();
        }
        this.orderProducts.addAll(input);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = View.inflate(context, R.layout.orderproduct_item,null);
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {
        holder.product_num.setText(String.valueOf(orderProducts.get(position).getProduct_num()));
        Glide.with(context).load(orderProducts.get(position).getProduct_img()).placeholder(R.drawable.ic_error).into(holder.product_img);
        holder.product_name.setText(orderProducts.get(position).getProduct_name());
        holder.product_price.setText(String.valueOf(orderProducts.get(position).getProduct_price()*orderProducts.get(position).getProduct_num()) );
    }

    @Override
    public int getItemCount() {
        return orderProducts.size();
    }

    static class holder extends RecyclerView.ViewHolder{
        TextView product_name,product_num,product_price;
        ImageView product_img;
        public holder(@NonNull View itemView) {
            super(itemView);
            product_name = itemView.findViewById(R.id.product_name);
            product_num = itemView.findViewById(R.id.product_num);
            product_price = itemView.findViewById(R.id.product_price);
            product_img = itemView.findViewById(R.id.product_icon);
        }
    }
}
