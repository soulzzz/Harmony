package com.xmut.harmony.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.xmut.harmony.Activity.GoodDetailActivity;
import com.xmut.harmony.Activity.StoreActivity;
import com.xmut.harmony.R;
import com.xmut.harmony.entity.Product;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GoodsAdapter extends RecyclerView.Adapter<GoodsAdapter.GoodsHolder> {
    Context context;
    List<Product> productList;
    public GoodsAdapter(Context context) {
        this.context = context;
        productList =new ArrayList<>();
    }
    public void setProductList(List<Product> temp){
        if (this.productList.size() > 0) {
            this.productList.clear();
        }
        this.productList.addAll(temp);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GoodsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.goods_item,null);
        return new GoodsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GoodsHolder holder, int position) {
        holder.goodsprice.setText(String.valueOf(productList.get(position).getProduct_price()) );
        holder.goodsname.setText(productList.get(position).getProduct_name());
        holder.goodstore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(context,StoreActivity.class);
                it.putExtra("storeid",productList.get(position).getStore_id());
                context.startActivity(it);
            }
        });
        Glide.with(context)
                .load(productList.get(position).getProduct_img())
                .placeholder(R.drawable.ic_error)
                .into(holder.img);
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(context, GoodDetailActivity.class);
                it.putExtra("goods", productList.get(position));
                context.startActivity(it);
            }
        });
//        for(StoreActivity.store)
//        holder.goodstore.setText();
    }


    @Override
    public int getItemCount() {
        return productList.size();
    }
    static class GoodsHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView goodsname,goodsprice,goodstore;
        public GoodsHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.goods_img);
            goodsname=  itemView.findViewById(R.id.goodsname);
            goodsprice = itemView.findViewById(R.id.num);
            goodstore = itemView.findViewById(R.id.store_name);
        }
    }
}
