package com.xmut.harmony.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xmut.harmony.Activity.OrderActivity;
import com.xmut.harmony.Activity.OrderAddressActivity;
import com.xmut.harmony.Activity.OrderChangeAddressActivity;
import com.xmut.harmony.R;
import com.xmut.harmony.entity.Result;
import com.xmut.harmony.entity.User;
import com.xmut.harmony.entity.UserAddress;
import com.xmut.harmony.util.httputil.DatabaseUtil;
import com.xmut.harmony.util.httputil.http.HttpAddress;
import com.xmut.harmony.util.userutil.UserManage;

import java.util.ArrayList;
import java.util.List;

public class AddressChooseAdapter extends RecyclerView.Adapter<AddressChooseAdapter.Myholder> {

    UserManage userManage = UserManage.getInstance();
    List<UserAddress> addressList;
    Context context;
    public AddressChooseAdapter(Context context) {
        this.context = context;
        addressList = new ArrayList<>();
        addressList=userManage.getUserInfo(context).getAddress();
    }



    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.address_item,null);
        return new Myholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Myholder holder, int position) {
        holder.address_name.setText(addressList.get(position).getUser_name());
        holder.address_tel.setText(addressList.get(position).getUser_tel());
        holder.address.setText(addressList.get(position).getUser_address());
        holder.deletebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Result result = DatabaseUtil.deleteById(HttpAddress.get(HttpAddress.user(),"deleteuseraddress",addressList.get(position).getAddress_id() ) );
                if(result.getCode()!=200)
                {
                    Toast.makeText(context, "删除地址失败", Toast.LENGTH_SHORT).show();
                }else{

                    addressList.remove(position);

                    User tempuser= userManage.getUserInfo(context);
                    tempuser.setAddress(addressList);
                    userManage.saveUserInfo(context,tempuser);

                    notifyDataSetChanged();
                }

            }
        });
        holder.editbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(context, OrderChangeAddressActivity.class);
                it.putExtra("address",addressList.get(position) );
                context.startActivity(it);
            }
        });
        holder.selectsetion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(context, OrderActivity.class);
                it.putExtra("useraddress",addressList.get(position));
                if(OrderAddressActivity.instance!=null ){
                    OrderAddressActivity.instance.finish();
                }
                if(OrderActivity.instance!=null){
                    OrderActivity.instance.finish();
                }

                context.startActivity(it);

            }
        });
    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    static class Myholder extends RecyclerView.ViewHolder{
        TextView address_name,address_tel,address,deletebt;
        ImageView address_select,editbt,addressicon;
        RelativeLayout selectsetion;
        public Myholder(@NonNull View itemView) {
            super(itemView);
            address_name = itemView.findViewById(R.id.address_name);
            address_tel = itemView.findViewById(R.id.address_tel);
            address = itemView.findViewById(R.id.address_);
            selectsetion = itemView.findViewById(R.id.selectsetion);
//            address_select = itemView.findViewById(R.id.addressselected);
//            address_select.setVisibility(View.VISIBLE);
            deletebt =itemView.findViewById(R.id.deletebt);
            editbt = itemView.findViewById(R.id.editbt);
            addressicon =itemView.findViewById(R.id.addressicon);
            addressicon.setVisibility(View.VISIBLE);
        }
    }
}
