package com.xmut.harmony.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.xmut.harmony.Activity.GoodDetailActivity;
import com.xmut.harmony.Activity.StoreActivity;
import com.xmut.harmony.R;
import com.xmut.harmony.entity.Product;
import com.xmut.harmony.entity.ProductComment;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder> {
    Context context;
    List<ProductComment> comments;
    public CommentAdapter(Context context) {
        this.context = context;
        comments =new ArrayList<>();
    }
    public void setProductList(List<ProductComment> temp){
        if (this.comments.size() > 0) {
            this.comments.clear();
        }
        this.comments.addAll(temp);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.comment_item,null);
        return new CommentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentHolder holder, int position) {
        holder.ratingBar.setRating((float)comments.get(position).getProduct_score());
        holder.uid.setText(String.valueOf(comments.get(position).getUser_name()));
        holder.time.setText(String.valueOf(comments.get(position).getCreatetime()));
        holder.comment.setText(comments.get(position).getProduct_comment());
    }


    @Override
    public int getItemCount() {

        return comments.get(0).getOrder_id()==null?0:comments.size();
    }

    static class CommentHolder extends RecyclerView.ViewHolder{
        TextView uid,time,comment;
        RatingBar ratingBar;
        public CommentHolder(@NonNull View itemView) {
            super(itemView);
            uid = itemView.findViewById(R.id.uid);
            time = itemView.findViewById(R.id.time);
            comment = itemView.findViewById(R.id.comment);
            ratingBar = itemView.findViewById(R.id.ratingbar);
        }
    }
}
