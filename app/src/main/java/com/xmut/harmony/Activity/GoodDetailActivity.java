package com.xmut.harmony.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xmut.harmony.Adapter.CommentAdapter;
import com.xmut.harmony.Adapter.RecommendAdapter;
import com.xmut.harmony.Fragment.MainFragment;
import com.xmut.harmony.R;
import com.xmut.harmony.entity.Product;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class GoodDetailActivity extends AppCompatActivity {
    Context context;
    Product product ;
    int num = 1;
    ImageView img,leave,add_bt,sub_bt;
    TextView name,price,gotopay,product_num;
    RecyclerView recyclerView,recyclerView2;
    static GoodDetailActivity instance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.good_total_layout);
        instance = this;
        context = this;
        product= (Product) getIntent().getSerializableExtra("goods");
        Init();
    }

    private void Init() {
        add_bt = findViewById(R.id.add_bt);
        sub_bt = findViewById(R.id.subtract_bt);
        product_num = findViewById(R.id.product_num);
        add_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num = Math.min(++num,product.getProduct_stock());
                if(num==1){
                    sub_bt.setVisibility(View.GONE);

                }else {
                    sub_bt.setVisibility(View.VISIBLE);
                    product_num.setText(String.valueOf(num));
                }
                product_num.setText(String.valueOf(num));
            }
        });
        sub_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num = Math.max(--num,1);
                if(num==1){
                    sub_bt.setVisibility(View.GONE);

                }else {
                    sub_bt.setVisibility(View.VISIBLE);
                    product_num.setText(String.valueOf(num));
                }
                product_num.setText(String.valueOf(num));
            }
        });
        leave = findViewById(R.id.leave);
        img=findViewById(R.id.goods_img);
        name = findViewById(R.id.goodsname);
        price = findViewById(R.id.goodsprice);
        gotopay = findViewById(R.id.gotopay);
        recyclerView =findViewById(R.id.comments);
        recyclerView2 = findViewById(R.id.recommend);
        List<Product> temp = new ArrayList<>();
        RecommendAdapter recommendAdapter = new RecommendAdapter(context);
        int i=0;
        for(Product p: MainFragment.products){
            if(p.getProduct_category().equals(product.getProduct_category()) && !p.getProduct_id().equals(product.getProduct_id())){
                temp.add(p);
                i++;
                if(i>=6) break;
            }
        }
        System.out.println("Size:"+temp.size());
        recommendAdapter.setProductList(temp);

        leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        CommentAdapter commentAdapter =new CommentAdapter(context);
        commentAdapter.setProductList(product.getProductCommentList());
        recyclerView2.setAdapter(recommendAdapter);
        recyclerView2.setLayoutManager(new GridLayoutManager(context,3));

        recyclerView.setAdapter(commentAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        Glide.with(this).load(product.getProduct_img()).placeholder(R.drawable.ic_error).into(img);
        name.setText(product.getProduct_name());
        price.setText(String.valueOf(product.getProduct_price() ));
        gotopay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Product> productList = new ArrayList<>();
                product.setProduct_num(num);
                productList.add(product);
                Intent it = new Intent(context,OrderActivity.class);
                it.putExtra("list",(Serializable) productList);
                startActivity(it);
            }
        });
    }
}