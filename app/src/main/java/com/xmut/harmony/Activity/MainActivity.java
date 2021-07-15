package com.xmut.harmony.Activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.xmut.harmony.Fragment.BrowserFragment;
import com.xmut.harmony.Fragment.MainFragment;
import com.xmut.harmony.Fragment.ProfileFragment;
import com.xmut.harmony.R;
import com.xmut.harmony.entity.User;
import com.xmut.harmony.util.userutil.UserManage;

import static com.xmut.harmony.util.userutil.ConstantValue.LoginUser;


/**
 * Created by Coder-pig on 2015/8/30 0030.
 */
public class MainActivity extends FragmentActivity implements View.OnClickListener {
    public static final int REQUEST_CODE = 1024;
    public static Context mContext;
    public static User curuser  = null;
    //Activity UI Object
    private LinearLayout tab_menu_1;
    private TextView tab_menu_text1;
    private TextView tab_menu_num1;

    private LinearLayout tab_menu_2;
    private TextView tab_menu_text2;
    private TextView tab_menu_num2;

    private LinearLayout tab_menu_3;
    private TextView tab_menu_text3;
    private TextView tab_menu_num3;
    private TextView topb_bar_text;
    private FragmentTransaction mFragmentTransaction;
    private Fragment fg1;
    private Fragment fg2;
    private Fragment fg3;

    UserManage userManage = UserManage.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mContext = MainActivity.this;
        curuser= userManage.getUserInfo(this);
        if(curuser==null)
        {
            curuser = userManage.getPlunge(MainActivity.this,LoginUser);
        }

//        Intent intent = this.getIntent();
//        if(intent.getSerializableExtra("user")!=null)
//        curuser=(User)intent.getSerializableExtra("user");
//        System.out.println("111"+curuser);
        bindViews();
        if(savedInstanceState == null) {
            tab_menu_1.performClick();
       }
        if(curuser.getUserpermission()<1)
        Toast.makeText(mContext, "登陆成功,用户"+curuser.getUsername(), Toast.LENGTH_SHORT).show();
        else if(curuser.getUserpermission()==1){
            Toast.makeText(mContext, "登陆成功,会员"+curuser.getUsername(), Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(mContext, "登陆成功,管理员"+curuser.getUsername(), Toast.LENGTH_SHORT).show();
        }

    }
//    public void onSaveInstanceState(Bundle savedInstanceState) {
//        super.onSaveInstanceState(savedInstanceState);
//        savedInstanceState.putSerializable(ConstantValue.SavedUser, curuser);
//    }

    private void bindViews() {
        tab_menu_1 =  findViewById(R.id.tab_menu_1);
        tab_menu_text1 =  findViewById(R.id.tab_menu_1_text);
        tab_menu_num1 =  findViewById(R.id.tab_menu_1_num);

        tab_menu_2 =  findViewById(R.id.tab_menu_2);
        tab_menu_text2 =  findViewById(R.id.tab_menu_2_text);
        tab_menu_num2 =  findViewById(R.id.tab_menu_2_num);

        tab_menu_3 =  findViewById(R.id.tab_menu_3);
        tab_menu_text3 =  findViewById(R.id.tab_menu_3_text);
        tab_menu_num3 =  findViewById(R.id.tab_menu_3_num);

        tab_menu_1.setOnClickListener(this);
        tab_menu_2.setOnClickListener(this);
        tab_menu_3.setOnClickListener(this);
        //topb_bar_text= findViewById(R.id.topb_bar_text);
    }

    @Override
    public void onClick(View v) {
        mFragmentTransaction =getSupportFragmentManager().beginTransaction();
        switch (v.getId()) {
            case R.id.tab_menu_1:
                setSelected();
                tab_menu_text1.setSelected(true);
                //topb_bar_text.setText(tab_menu_text1.getText().toString());
                if(fg1 ==null){
                    fg1 = new MainFragment();
                    mFragmentTransaction.add(R.id.fragment_content,fg1);
                }
                swithchFragment(mFragmentTransaction,fg1,fg2,fg3);

                break;
            case R.id.tab_menu_2:
                setSelected();
                tab_menu_text2.setSelected(true);
                //topb_bar_text.setText(tab_menu_text2.getText().toString());
                if(fg2 ==null){
                    fg2 = new BrowserFragment();
                    mFragmentTransaction.add(R.id.fragment_content,fg2);
                }
                swithchFragment(mFragmentTransaction,fg2,fg1,fg3);

                break;
            case R.id.tab_menu_3:
                setSelected();
                tab_menu_text3.setSelected(true);
                //topb_bar_text.setText(tab_menu_text3.getText().toString());
                if(fg3 ==null){
                    fg3 = new ProfileFragment();
                    mFragmentTransaction.add(R.id.fragment_content,fg3);
                }
                swithchFragment(mFragmentTransaction,fg3,fg1,fg2);
                break;
        }
    }

    //重置所有文本的选中状态
    private void setSelected() {
        tab_menu_text1.setSelected(false);
        tab_menu_text2.setSelected(false);
        tab_menu_text3.setSelected(false);
        tab_menu_1.setSelected(false);
        tab_menu_2.setSelected(false);
        tab_menu_3.setSelected(false);
        tab_menu_num1.setVisibility(View.INVISIBLE);
        tab_menu_num2.setVisibility(View.INVISIBLE);
        tab_menu_num3.setVisibility(View.INVISIBLE);
    }
    private void swithchFragment(FragmentTransaction mfragmentTransaction,Fragment fg1, Fragment fg2,Fragment fg3) {
        if( fg2!=null && fg3!=null)
            mfragmentTransaction.show(fg1).hide(fg2).hide(fg3);
        else if(fg2!=null && fg3==null)
            mfragmentTransaction.show(fg1).hide(fg2);
        else if(fg2==null && fg3!=null)
            mfragmentTransaction.show(fg1).hide(fg3);
        else
            mfragmentTransaction.show(fg1);
        mfragmentTransaction.setTransition(mfragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();

    }


}