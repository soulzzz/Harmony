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
import com.xmut.harmony.entity.Product;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class OrderProductAdapter extends RecyclerView.Adapter<OrderProductAdapter.holder> {
    Context context;
    List<Product> productList;
    DecimalFormat df = new DecimalFormat("0.00");
    public OrderProductAdapter(Context context) {
        this.context = context;
        productList =new ArrayList<>();
    }
    public void setProductList(List<Product> input){
        if (this.productList.size() > 0) {
            this.productList.clear();
        }
        this.productList.addAll(input);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = View.inflate(context, R.layout.order_product_item,null);
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {
        holder.product_num.setText(String.valueOf(productList.get(position).getProduct_num()));
        Glide.with(context).load(productList.get(position).getProduct_img()).placeholder(R.drawable.ic_error).into(holder.product_img);
        holder.product_name.setText(productList.get(position).getProduct_name());
        holder.product_price.setText(String.valueOf(Double.parseDouble(df.format( (productList.get(position).getProduct_price() )))));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class holder extends RecyclerView.ViewHolder{
        TextView product_name,product_num,product_price;
        ImageView product_img;
        public holder(@NonNull View itemView) {
            super(itemView);
            product_name = itemView.findViewById(R.id.product_name);
            product_num = itemView.findViewById(R.id.num);
            product_price = itemView.findViewById(R.id.product_price);
            product_img = itemView.findViewById(R.id.product_img);
        }
    }
}
