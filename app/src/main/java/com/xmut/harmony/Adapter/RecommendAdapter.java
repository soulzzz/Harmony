package com.xmut.harmony.Adapter;

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
import com.xmut.harmony.R;
import com.xmut.harmony.entity.Product;
import com.xmut.harmony.entity.Result;
import com.xmut.harmony.util.httputil.DatabaseUtil;
import com.xmut.harmony.util.httputil.http.HttpAddress;

import java.util.ArrayList;
import java.util.List;

public class RecommendAdapter extends RecyclerView.Adapter<RecommendAdapter.holder> {
    Context context;
    List<Product> products ;
    public RecommendAdapter(Context context) {
        this.context = context;
        products =new ArrayList<>();
    }
    public void setProductList(List<Product> temp){
        if (this.products.size() > 0) {
            this.products.clear();
        }
        this.products.addAll(temp);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public RecommendAdapter.holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = View.inflate(context,R.layout.recommend_item,null);
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendAdapter.holder holder, int position) {
        Glide.with(context).load(products.get(position).getProduct_img()).placeholder(R.drawable.ic_error).into(holder.img);
        holder.name.setText(products.get(position).getProduct_name() );

        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(context, GoodDetailActivity.class);
                it.putExtra("goods",products.get(position));
                context.startActivity(it);
//                Result result = DatabaseUtil.selectLineById(HttpAddress.get(HttpAddress.product(),"line",products.get(position).getProduct_id()));
//                if(result.getCode()==200){
//                    Product product = DatabaseUtil.getEntity(result,Product.class);
//                    System.out.println(product);
//                    it.putExtra("goods",product);
//                    context.startActivity(it);
//                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return products.size()>6?6:products.size();
    }


    static class holder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView name;
        public holder(@NonNull View itemView) {
            super(itemView);
            img  = itemView.findViewById(R.id.goods_img);
            name = itemView.findViewById(R.id.goodsname);
        }
}
}