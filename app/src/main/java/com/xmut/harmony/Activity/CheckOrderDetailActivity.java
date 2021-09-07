package com.xmut.harmony.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xmut.harmony.Adapter.CheckOrderDetailAdapter;
import com.xmut.harmony.Adapter.OrderAdapter;
import com.xmut.harmony.R;
import com.xmut.harmony.entity.Order;
import com.xmut.harmony.entity.Result;
import com.xmut.harmony.entity.User;
import com.xmut.harmony.util.httputil.DatabaseUtil;
import com.xmut.harmony.util.httputil.http.HttpAddress;
import com.xmut.harmony.util.userutil.DialogUitl;
import com.xmut.harmony.util.userutil.OrderUtil;
import com.xmut.harmony.util.userutil.UserManage;

import java.util.ArrayList;
import java.util.List;

import static com.xmut.harmony.Activity.MainActivity.curuser;

public class CheckOrderDetailActivity extends AppCompatActivity {
    ImageView leaveorder;
    RecyclerView recyclerview;
    TextView time,name1,tel1,add1,state,price,request;
    Order order;
    List<Order> templist=new ArrayList<>();
    DialogUitl dialogUitl =DialogUitl.getInstance();
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_order_detail);
        context =this;
        Intent it = getIntent();
        order = (Order) it.getSerializableExtra("order");
        if(order!=null){
            templist.add(order);
        }

        init();

    }

    private void init() {
        price = findViewById(R.id.price);
        recyclerview = findViewById(R.id.recyclerview);
        time = findViewById(R.id.time);
        name1 =findViewById(R.id.name1);
        tel1 =findViewById(R.id.tel1);
        add1 = findViewById(R.id.add1);
        state =findViewById(R.id.state);
        request = findViewById(R.id.request);
        price.setText(String.valueOf(order.getOrder_price()) );
        switch (order.getOrder_state()){
            case 0:
                break;
            case 1:
                request.setVisibility(View.VISIBLE);
                request.setText("取消订单");
                request.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        order.setOrder_state(0);
                        Result result = DatabaseUtil.updateById(order,HttpAddress.get(HttpAddress.order(),"update"));
                        if(result.getCode()== 200){
                            Toast.makeText(context, "取消成功", Toast.LENGTH_SHORT).show();
                            int page=  getIntent().getIntExtra("page",1);
                            Intent it =getIntent();
                            it.putExtra("page",page);
                            setResult(RESULT_OK,it);
                            finish();
                        }else{
                            order.setOrder_state(1);
                            Toast.makeText(context, "取消失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                request.setVisibility(View.VISIBLE);
                request.setText("申请退款");
                request.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Dialog dialog1 = new Dialog(context);
                        View contentView1 = LayoutInflater.from(context).inflate(
                                R.layout.changemessage, null);
                        dialog1.setContentView(contentView1);
                        TextView title = contentView1.findViewById(R.id.change_title);
                        ImageView cancelbt = contentView1.findViewById(R.id.cancel_change_bt);
                        Button successbt = contentView1.findViewById(R.id.success_change_bt);
                        EditText changedmessage = contentView1.findViewById(R.id.changed_message);
                        dialogUitl.setDialogTransparentandCircle(dialog1);
                        dialogUitl.setDialogMatchParent(context,dialog1);
                        title.setText("填写原因");
                        changedmessage.setSingleLine(true);
                        changedmessage.setHint("请输入退单原因");
                        cancelbt.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog1.dismiss();
                                    }
                                });
                                successbt.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if(UserManage.isEmpty(changedmessage.getText().toString()) )
                                        {
                                            Toast.makeText(context, "输入信息为空", Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            order.setOrder_reason(changedmessage.getText().toString());
                                            order.setOrder_state(-2);
                                            Result result = DatabaseUtil.updateById(order,HttpAddress.get(HttpAddress.order(),"update"));
                                            if(result.getCode()!=200)
                                            {
                                                Toast.makeText(context, "提交申请失败", Toast.LENGTH_SHORT).show();
                                            }else{
                                                Toast.makeText(context, "提交申请成功", Toast.LENGTH_SHORT).show();
                                                dialog1.dismiss();
                                                int page=  getIntent().getIntExtra("page",1);
                                                Intent it =getIntent();
                                                it.putExtra("page",page);
                                                setResult(RESULT_OK,it);
                                                finish();
                                            }

                                        }
                                    }
                                });
                                dialog1.show();

                    }
                });
                break;
            case 5:
                break;
            default:
                break;
        }
        state.setText(OrderUtil.getStateName(order.getOrder_state()));

        state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (order.getOrder_state() == 0) { //已取消
                    Toast.makeText(context, "订单已取消，可查看详情", Toast.LENGTH_SHORT).show();

                } else if (order.getOrder_state() == 1) { //待付款
                    order.setOrder_state(2);
                    Result result = DatabaseUtil.updateById(order, HttpAddress.get(HttpAddress.order(), "update"));
                    if (result.getCode() != 200) {
                        Toast.makeText(context, "订单:" + result.getMsg(), Toast.LENGTH_SHORT).show();
                        order.setOrder_state(1);
                    } else {
                        Toast.makeText(context, "付款成功", Toast.LENGTH_SHORT).show();
                        state.setText(OrderUtil.getStateName(order.getOrder_state()));
                        request.setVisibility(View.GONE);
                    }

                } else if (order.getOrder_state() == 2) { //待发货
                    Toast.makeText(context, "请耐心等待发货", Toast.LENGTH_SHORT).show();

                } else if (order.getOrder_state() == 3) { //待收货
                    order.setOrder_state(4);
                    Result result = DatabaseUtil.updateById(order, HttpAddress.get(HttpAddress.order(), "update"));
                    if (result.getCode() != 200) {
                        Toast.makeText(context, "订单:" + result.getMsg(), Toast.LENGTH_SHORT).show();
                        order.setOrder_state(3);
                    } else {
                        state.setText(OrderUtil.getStateName(order.getOrder_state()));
                    }

                } else if (order.getOrder_state() == 4) { //待评价
                    Toast.makeText(context, "评价", Toast.LENGTH_SHORT).show();
                    Intent it = new Intent(context, CommentActivity.class);
                    it.putExtra("order", order);
                    startActivityForResult(it, 4);
                }
            }
        });
        time.setText(order.getOrder_date());
        name1.setText(order.getUser_name());
        tel1.setText(order.getUser_tel());
        add1.setText(order.getUser_address());

        leaveorder = findViewById(R.id.leaveorder);
        leaveorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int page=  getIntent().getIntExtra("page",1);
                Intent it =getIntent();
                it.putExtra("page",page);
                setResult(RESULT_OK,it);
                finish();
            }
        });
        CheckOrderDetailAdapter adapter = new CheckOrderDetailAdapter(context);
        adapter.setOrderList(templist);
        recyclerview.setAdapter(adapter);
        recyclerview.setLayoutManager(new LinearLayoutManager(context,RecyclerView.VERTICAL,false) );

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent it) {
        super.onActivityResult(requestCode, resultCode, it);
        if (requestCode == 4 && resultCode == RESULT_OK) {
            state.setText(OrderUtil.getStateName(it.getIntExtra("state",1)));
        }
    }
}