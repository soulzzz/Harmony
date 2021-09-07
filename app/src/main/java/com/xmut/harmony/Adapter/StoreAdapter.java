package com.xmut.harmony.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.xmut.harmony.R;
import com.xmut.harmony.Activity.StoreActivity;
import com.xmut.harmony.Video.utils.LogUtil;
import com.xmut.harmony.Video.utils.StringUtil;
import com.xmut.harmony.entity.Store;
import com.xmut.harmony.util.userutil.UserManage;

import java.util.ArrayList;
import java.util.List;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.StoreHolder>  {
    private static final String TAG = "StoreTag";
    private ArrayList<Store> storeList;
    private Context context;

    public StoreAdapter(Context context) {
        this.context = context;
        storeList =new ArrayList<>();

    }
    public void setStoreListList(List<Store> storeList) {
        if (this.storeList.size() > 0) {
            this.storeList.clear();
        }
        this.storeList.addAll(storeList);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public StoreHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.select_store_item, parent, false);
        return new StoreHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreHolder holder, int position) {
        if (storeList.size() > position && holder != null) {
            Store store = storeList.get(position);
            if (store == null) {
                LogUtil.i(TAG, "current item data is empty.");
                return;
            }

            StringUtil.setTextValue(holder.storename, store.getStore_name());
            StringUtil.setTextValue(holder.intro, store.getStore_des());
            Glide.with(context)
                    .load(store.getStore_img())
                    .placeholder(R.drawable.ic_error)
                    .into(holder.storeimg);


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(UserManage.getInstance().getUserInfo(context).getUserpermission()>=1){
                        Intent it = new Intent(context, StoreActivity.class);
                        it.putExtra("storeid",storeList.get(position).getStore_id());
                        context.startActivity(it);
                    }else{
                        Toast.makeText(context, "您还不是会员，无法浏览商店", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return storeList.size();
    }

    static class StoreHolder extends RecyclerView.ViewHolder{
        TextView storename;
        TextView distance;
        TextView intro;
        ImageView storeimg;

        public StoreHolder(@NonNull View itemView) {
            super(itemView);
            storename = itemView.findViewById(R.id.store_name);
            intro = itemView.findViewById(R.id.store_intro);
            storeimg =itemView.findViewById(R.id.store_icon);

        }
    }
}

