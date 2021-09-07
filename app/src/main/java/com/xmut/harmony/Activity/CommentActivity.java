package com.xmut.harmony.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.xmut.harmony.R;
import com.xmut.harmony.entity.Order;
import com.xmut.harmony.entity.Product;
import com.xmut.harmony.entity.ProductComment;
import com.xmut.harmony.entity.Result;
import com.xmut.harmony.util.httputil.DatabaseUtil;
import com.xmut.harmony.util.httputil.http.HttpAddress;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CommentActivity extends AppCompatActivity {
    Order order ;
    SimpleDateFormat sdf;
    ImageView leave;
    String strDateFormat = "yyyy-MM-dd HH:mm:ss";
    ProductComment productComment = new ProductComment();
    RatingBar ratingBar;
    TextView comment,submit;
    Result result;
    Integer page;
    Date date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        sdf = new SimpleDateFormat(strDateFormat);
        order = (Order)getIntent().getSerializableExtra("order");
        page = getIntent().getIntExtra("page",1);
        Init();

    }

    private void Init() {
        leave = findViewById(R.id.leave);
        leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        submit = findViewById(R.id.submit);
        ratingBar = findViewById(R.id.ratingbar);
        comment = findViewById(R.id.comment);

        productComment.setUser_id(MainActivity.curuser.getId());
        productComment.setUser_name(MainActivity.curuser.getUsername());
        System.out.println(MainActivity.curuser.getUsername());


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(MainActivity.curuser.getUsername());
                date= new Date();
                productComment.setCreatetime(sdf.format(date));
                productComment.setOrder_id(order.getOrder_id());
                productComment.setProduct_score( ratingBar.getRating() );
                productComment.setProduct_comment(comment.getText().toString());
                for(Product product:order.getOrderProducts()){
                    productComment.setProduct_id(product.getProduct_id());
                    System.out.println(productComment);
                     result = DatabaseUtil.insert(productComment, HttpAddress.get(HttpAddress.product(),"insertcomment") );
                }
                if(result.getCode()==200){
                    order.setOrder_state(5);
                    result = DatabaseUtil.updateById(order,HttpAddress.get(HttpAddress.order(),"update"));
                    if(result.getCode()==200){
                        Toast.makeText(CommentActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
                        Intent it = getIntent();
                        it.putExtra("state",order.getOrder_state());
                        it.putExtra("page",page);
                        setResult(RESULT_OK,it);
                        finish();
                    }
                }

            }
        });


    }
}