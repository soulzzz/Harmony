package com.xmut.harmony.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.xmut.harmony.Adapter.ProductAdapter;
import com.xmut.harmony.Adapter.ProductCategoryAdapter;
import com.xmut.harmony.Fragment.MainFragment;
import com.xmut.harmony.R;
import com.xmut.harmony.entity.Product;
import com.xmut.harmony.entity.Store;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class StoreActivity extends AppCompatActivity {
    public static Store store; //从MainFragment获取的商店，静态用于其他活动调用
    private ImageView leavestore;
    private List<Product> products = new ArrayList<>();
    private List<String> categoryList = new ArrayList<>();
    private List<Integer> productposition = new ArrayList<>();

    private RecyclerView productrecycler,categoryrecycler;
    private Context context;
    private TextView price;
    private TextView store_name,store_des;
    private ImageView store_img;
    public static Map<Product, Integer> myorder;
    static StoreActivity instance;
    DecimalFormat df = new DecimalFormat("0.00");
    int cposition = -1;
    static double totalPrice;
    TextView gotopay;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        instance =this;
        context = this;
        Intent it = getIntent();
        int storeid = it.getIntExtra("storeid",0);
        for(Store temp : MainFragment.stores)
        {
            if(String.valueOf(temp.getStore_id()).equals(String.valueOf(storeid)) )
            {
                store = temp;
                break;
            }
        }
        //对List中特定属性进行归类排序
        if(store.getStore_products() !=null && store.getStore_products().get(0).getProduct_id()!=null){
            List<Product> list  =store.getStore_products();

            for(Product product:list){ //过滤未上架商品
                if(product.getIsshow()!=1){
                    list.remove(product);
                }
            }
            Map<String,List<Product> > stringListMap = list.stream().collect(Collectors.groupingBy(Product::getProduct_category));
            products.clear();
            //将map集合转为set集合遍历
            Set<Map.Entry<String, List<Product>>> entries = stringListMap.entrySet();
            for(Map.Entry m : entries){
                System.out.println(m);
                products.addAll((List<Product>)m.getValue() ) ;
            }
        }else{
            products.clear();
        }




//        Map<String,List<Product> > map = new HashMap<>();
//        for(Product p:temp){
//            String key = p.getProduct_category();
//            if(map.containsKey(key)){
//                map.get(key).add(p);
//            }else{
//                List<Product> productlist = new ArrayList<>();
//                productlist.add(p);
//                map.put(key,productlist);
//            }
//        }
//        Collection<List<Product>> values = map.values();
//        for (List<Product> value : values) {
//            products.addAll(value);
//        }

        for(int i=0,j=-1;i<products.size();i++)
        {

            if(!categoryList.contains(products.get(i).getProduct_category()) ){
                categoryList.add(products.get(i).getProduct_category() );
                productposition.add(i);
                j++;
                products.get(i).setIsfirst(true);
            }
            products.get(i).setBelongto(j);
        }
        init();

    }

    private void init() {


        gotopay =findViewById(R.id.gotopay);
        gotopay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myorder.isEmpty()){
                    Toast.makeText(context, "未选商品", Toast.LENGTH_SHORT).show();
                }
                else {
                    List<Product> temp = new ArrayList<>();
                    for (Map.Entry<Product, Integer> entry : myorder.entrySet()) {
                        entry.getKey().setProduct_num(entry.getValue()) ;
                        temp.add(entry.getKey() );
                    }

                    Intent it = new Intent(context,OrderActivity.class);
                    it.putExtra("list",(Serializable)temp);
                    startActivity(it);
                }

            }
        });
        leavestore =findViewById(R.id.leavestore);
        leavestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        myorder = new HashMap<Product,Integer>();
        price = findViewById(R.id.totalprice);
        store_name = findViewById(R.id.store_name);
        store_des = findViewById(R.id.store_des);
        store_img = findViewById(R.id.store_img);

        store_name.setText(store.getStore_name());
        store_des.setText(store.getStore_des());
        Glide.with(this).load(store.getStore_img()).placeholder(R.drawable.ic_error).into(store_img);

        productrecycler = findViewById(R.id.productrecycler);
        categoryrecycler = findViewById(R.id.categoryrecycler);

        ProductAdapter productAdapter = new ProductAdapter(context);

        productAdapter.setProductList(products);
        productrecycler.setAdapter(productAdapter);
        productrecycler.setHasFixedSize(true);
        LinearLayoutManager manager1 = new LinearLayoutManager(context,RecyclerView.VERTICAL,false);
        productrecycler.setLayoutManager(manager1);


        ProductCategoryAdapter productCategoryAdapter = new ProductCategoryAdapter(context);
        productCategoryAdapter.setList(categoryList,productposition);

        categoryrecycler.setAdapter(productCategoryAdapter);
        categoryrecycler.setHasFixedSize(true);
        categoryrecycler.setLayoutManager(new LinearLayoutManager(context,RecyclerView.VERTICAL,false));
        productCategoryAdapter.setOnItemClickListener(new ProductCategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, int lefttoright) {
                manager1.scrollToPositionWithOffset(lefttoright,0);
            }
        });
        productrecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int firstVisibleItemPosition =manager1.findFirstCompletelyVisibleItemPosition();
//                System.out.println("状态1  "+ "第一个可视商品:"+firstVisibleItemPosition +" 属于分类"+ products.get(firstVisibleItemPosition).getBelongto());
                if(firstVisibleItemPosition!=-1){

                    productCategoryAdapter.setSelection(products.get(firstVisibleItemPosition).getBelongto());
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstVisibleItemPosition =manager1.findFirstCompletelyVisibleItemPosition();
                productCategoryAdapter.setSelection(products.get(firstVisibleItemPosition).getBelongto());
//                System.out.println("状态2  "+ "第一个可视商品:"+firstVisibleItemPosition +" 属于分类"+ products.get(firstVisibleItemPosition).getBelongto());

            }
        });
        //监听数量
        productAdapter.setonNumChangeListener(new ProductAdapter.onNumChangeListener() {
            @Override
            public void onNumChange(View view, int position,int num) {
                    myorder.put(products.get(position),num); //会覆盖
                    if(num ==0){
                        myorder.remove(products.get(position));
                    }
                calculatePrice(myorder);
                price.setText(String.valueOf(totalPrice));


            }
        });


    }

    private void calculatePrice(Map<Product, Integer> a){
         totalPrice=0;
        for (Map.Entry< Product, Integer> entry : a.entrySet()) {
            totalPrice += (entry.getKey().getProduct_price() *entry.getValue());
        }
        totalPrice = Double.parseDouble(df.format(totalPrice));

    }

}