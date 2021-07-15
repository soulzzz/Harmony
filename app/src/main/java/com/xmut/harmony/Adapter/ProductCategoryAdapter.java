package com.xmut.harmony.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xmut.harmony.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductCategoryAdapter extends RecyclerView.Adapter<ProductCategoryAdapter.ProductCategoryHolder>{

    private List<String> productCategoryList;
    private List<Integer> productposition ;

    private ArrayList<Boolean> selected = new ArrayList<>();

    private int pos;
    private Context context;
    public interface OnItemClickListener {
        void onItemClick(View view, int position,int lefttoright);
    }
//
    private OnItemClickListener mOnItemClickListener;
//
//
    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }


    public void setSelection(int pos) {
        Log.d("TEST", "setSelection: " + pos);
        this.pos = pos;
        notifyDataSetChanged();
    }


    public ProductCategoryAdapter(Context context) {
        this.context = context;
        productCategoryList =new ArrayList<>();
        productposition =new ArrayList<>();

    }
    public void setList(List<String> temp1, List<Integer> temp2)
    {
        if(this.productCategoryList.size()>0 ||this.productposition.size()>0){
            productposition.clear();
            productCategoryList.clear();
        }

        this.productCategoryList.addAll(temp1);
        productposition.addAll(temp2);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductCategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.productcategoty_item,null);
        return new ProductCategoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductCategoryHolder holder, int position) {
        holder.textView.setText(productCategoryList.get(position));
        holder.itemView.setBackgroundResource(this.pos == position ? R.drawable.item_pressed:R.drawable.item_not_pressed);
        holder.textView.setTextColor(this.pos == position ?Color.rgb(241,130,78):Color.rgb(76,76,76));


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(holder.itemView,position,productposition.get(position));
                pos = position;
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return productCategoryList.size();
    }

    static class ProductCategoryHolder extends RecyclerView.ViewHolder{
        RelativeLayout bg;
        TextView textView;
        public ProductCategoryHolder(@NonNull View itemView) {
            super(itemView);
            textView=  itemView.findViewById(R.id.productcategoty_name);
            bg = itemView.findViewById(R.id.category_item_bg);
        }



    }
}
