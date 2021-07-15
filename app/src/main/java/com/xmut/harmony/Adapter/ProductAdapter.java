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
import com.xmut.harmony.Video.utils.StringUtil;
import com.xmut.harmony.entity.Product;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHodler> {
    List<Product> productList ;
    private Context context;
    public interface onNumChangeListener {
        void onNumChange(View view, int position,int num);
    }

    private ProductAdapter.onNumChangeListener monNumChangeListener;


    public void setonNumChangeListener( ProductAdapter.onNumChangeListener monNumChangeListener) {
        this.monNumChangeListener = monNumChangeListener;
    }

    @NonNull
    @Override
    public ProductHodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.product_item,null);
        return new ProductHodler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHodler holder, int position) {
        if(productList.get(position).isIsfirst()){
            holder.categoryname.setVisibility(View.VISIBLE);
            holder.categoryname.setText(productList.get(position).getProduct_category());
        }else{
            holder.categoryname.setVisibility(View.GONE);
//            holder.categoryname.setText(productList.get(position).getProduct_category());
        }

        holder.product_name.setText(productList.get(position).getProduct_name());
        Glide.with(context).load(productList.get(position).getProduct_img()).placeholder(R.drawable.ic_error).into(holder.product_img);
        holder.product_price.setText(String.valueOf(productList.get(position).getProduct_price()));
        holder.product_des.setText(productList.get(position).getProduct_des());
        if(holder.num !=0){
            holder.product_num.setVisibility(View.VISIBLE);
            holder.subtract_bt.setVisibility(View.VISIBLE);
            holder.product_num.setText(String.valueOf(holder.num));
        }else {
            holder.product_num.setVisibility(View.GONE);
            holder.subtract_bt.setVisibility(View.GONE);
            holder.product_num.setText(String.valueOf(holder.num));
        }
        holder.add_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.num = Math.min(++holder.num,productList.get(position).getProduct_stock());
                if(holder.num !=0){
                    holder.product_num.setVisibility(View.VISIBLE);
                    holder.subtract_bt.setVisibility(View.VISIBLE);
                    holder.product_num.setText(String.valueOf(holder.num));
                }else {
                    holder.product_num.setVisibility(View.GONE);
                    holder.subtract_bt.setVisibility(View.GONE);
                    holder.product_num.setText(String.valueOf(holder.num));
                }

                monNumChangeListener.onNumChange(holder.itemView,position,holder.num);
            }
        });
        holder.subtract_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.num--;
                if(holder.num !=0){
                    holder.product_num.setVisibility(View.VISIBLE);
                    holder.subtract_bt.setVisibility(View.VISIBLE);
                    holder.product_num.setText(String.valueOf(holder.num));
                }else {
                    holder.product_num.setVisibility(View.GONE);
                    holder.subtract_bt.setVisibility(View.GONE);
                    holder.product_num.setText(String.valueOf(holder.num));
                }

                monNumChangeListener.onNumChange(holder.itemView,position,holder.num);
            }
        });


    }

    public ProductAdapter(Context context) {
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

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ProductHodler extends RecyclerView.ViewHolder{
        TextView product_name,product_des,product_price,product_num,categoryname;
        ImageView product_img,add_bt,subtract_bt;
        int num =0;
        public ProductHodler(@NonNull View itemView) {
            super(itemView);
            product_name = itemView.findViewById(R.id.product_name);
            product_des = itemView.findViewById(R.id.product_des);
            product_price = itemView.findViewById(R.id.product_price);
            product_num = itemView.findViewById(R.id.product_num);

            product_img =itemView.findViewById(R.id.product_img);
            add_bt = itemView.findViewById(R.id.add_bt);
            subtract_bt = itemView.findViewById(R.id.subtract_bt);

            categoryname = itemView.findViewById(R.id.categoryname);
        }
    }
}
