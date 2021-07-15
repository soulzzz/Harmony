package com.xmut.harmony.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.xmut.harmony.Activity.AddressActivity;
import com.xmut.harmony.Activity.CheckOrderActivity;
import com.xmut.harmony.Activity.LoginActivity;
import com.xmut.harmony.R;
import com.xmut.harmony.entity.Result;
import com.xmut.harmony.entity.User;
import com.xmut.harmony.util.httputil.DatabaseUtil;
import com.xmut.harmony.util.httputil.http.HttpAddress;
import com.xmut.harmony.util.userutil.AvatarUtil;
import com.xmut.harmony.util.userutil.ConstantValue;
import com.xmut.harmony.util.userutil.DialogUitl;
import com.xmut.harmony.util.userutil.UserManage;

import java.io.IOException;

import static com.xmut.harmony.Activity.MainActivity.curuser;

public class ProfileFragment extends Fragment implements View.OnClickListener {
    TextView username;
    TextView age;
    TextView address;
    TextView email;
    TextView tel;
    Button logoutbt;
    View view;
    Context context;
    UserManage userManage = UserManage.getInstance();
    DialogUitl dialogUitl = DialogUitl.getInstance();
    AvatarUtil avatarUtil = AvatarUtil.getInstance();
    RelativeLayout line1;
    RelativeLayout line2;
    RelativeLayout line3;
    RelativeLayout line4,orderline;

    Dialog dialog1;


    ImageView profile_tel_goto;
    ImageView profile_address_goto;
    ImageView profile_email_goto;
    ImageView profile_image;
    Uri uri;
    byte[] imgbyte;
    Bitmap imgbitmap;

    LinearLayout imglayout ,allorder_goto;
    ImageView img ;
    RelativeLayout waitpurchase,waitsend,waitreceive,succeedorder;



    public ProfileFragment() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        view = inflater.inflate(R.layout.activity_profile,container,false);
        init();
        return view;
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putSerializable(ConstantValue.SavedUser, curuser);
    }

    private void init() {
        allorder_goto =view.findViewById(R.id.allorder_goto);
        waitpurchase = view.findViewById(R.id.waitpurchase);
        waitsend =view.findViewById(R.id.waitsend);
        waitreceive = view.findViewById(R.id.waitreceive);
        succeedorder = view.findViewById(R.id.succeedorder);
        allorder_goto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(context, CheckOrderActivity.class);
                it.putExtra("section",0);
                startActivity(it);
            }
        });
        waitpurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(context, CheckOrderActivity.class);
                it.putExtra("section",1);
                startActivity(it);
            }
        });
        waitsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(context, CheckOrderActivity.class);
                it.putExtra("section",2);
                startActivity(it);
            }
        });
        waitreceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(context, CheckOrderActivity.class);
                it.putExtra("section",3);
                startActivity(it);
            }
        });
        succeedorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(context, CheckOrderActivity.class);
                it.putExtra("section",4);
                startActivity(it);
            }
        });
        line1 = view.findViewById(R.id.line1);
        line2 = view.findViewById(R.id.line2);
        line3 = view.findViewById(R.id.line3);
        line4 = view.findViewById(R.id.line4);
        orderline =view.findViewById(R.id.oderline);
        if(curuser.getUserpermission()<1)
        {
            orderline.setVisibility(View.GONE);
        }else {
            orderline.setVisibility(View.VISIBLE);
        }
        line1.setOnClickListener(this);
        line2.setOnClickListener(this);
        line3.setOnClickListener(this);
        line4.setOnClickListener(this);
        orderline.setOnClickListener(this);

        tel =view.findViewById(R.id.profile_tel);

        profile_image = view.findViewById(R.id.profile_img);
        profile_image.setOnClickListener(this);


        username = view.findViewById(R.id.profile_username);
        age = view.findViewById(R.id.profile_age);
        age.setOnClickListener(this);

        address =view.findViewById(R.id.profile_address);
        email = view.findViewById(R.id.profile_email);
        if(curuser.getAvatar()!=null)
        {
            profile_image.setImageBitmap(AvatarUtil.getPicFromBytes(curuser.getAvatar(),null) );
        }
        username.setText(curuser.getUsername());
        age.setText(curuser.getAge()!=null? curuser.getAge().toString():"NA Age");
//        address.setText(curuser.getAddress()!=null? curuser.getAddress():"NA Address");
        tel.setText(curuser.getTel()!=null? curuser.getTel():"NA Number");
        email.setText(curuser.getEmail());
//        logoutbt=  view.findViewById(R.id.logout);
//        logoutbt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                UserManage userManage = UserManage.getInstance();
//                userManage.clearUserInfo(context);
//                Intent it = new Intent(context,LoginActivity.class);
//                startActivity(it);
//                getActivity().finish();
//            }
//        });
    }


    @Override
    public void onClick(View v) {
        dialog1 = new Dialog(context);
        View contentView1 = LayoutInflater.from(context).inflate(
                R.layout.changemessage, null);
        dialog1.setContentView(contentView1);
        TextView title = contentView1.findViewById(R.id.change_title);
        ImageView cancelbt = contentView1.findViewById(R.id.cancel_change_bt);
        Button successbt = contentView1.findViewById(R.id.success_change_bt);
        EditText changedmessage = contentView1.findViewById(R.id.changed_message);
         imglayout = contentView1.findViewById(R.id.changed_img_layout);
        img = contentView1.findViewById(R.id.changed_img);
        dialogUitl.setDialogTransparentandCircle(dialog1);
        dialogUitl.setDialogMatchParent(context,dialog1);
        switch (v.getId()){
            case R.id.profile_age:
                title.setText("设置年龄");
                changedmessage.setSingleLine(true);
                changedmessage.setHint("请输入年龄");
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
                            curuser.setAge(Integer.parseInt( changedmessage.getText().toString()) );
                            Result result = DatabaseUtil.updateById(curuser,
                                    HttpAddress.get(HttpAddress.user(),"update"));
                            System.out.println(result.toString());
                            if(result.getCode()!=200)
                            {
                                Toast.makeText(context, "修改失败", Toast.LENGTH_SHORT).show();
                            }else{
                                User user = DatabaseUtil.getEntity(result, User.class);
                                userManage.saveUserInfo(context,user);
                                Toast.makeText(context, "修改成功", Toast.LENGTH_SHORT).show();
                                age.setText(changedmessage.getText().toString().trim());
                                dialog1.dismiss();
                            }

                        }
                    }
                });
//                dialogUitl.setDialogMatchParent(context,dialog1);
                dialog1.show();
                break;

            case R.id.line3:
                title.setText("设置邮箱");
                changedmessage.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                changedmessage.setText(email.getText().toString().trim());
                changedmessage.setSelection(changedmessage.getText().toString().length());
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
                        }else if(!changedmessage.getText().toString().contains("@") || !changedmessage.getText().toString().contains(".com")){
                            Toast.makeText(context, "输入邮箱格式错误", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            User tempuser = curuser;
                            tempuser.setEmail(changedmessage.getText().toString().trim());
                            Result result = DatabaseUtil.updateById(tempuser,
                                    HttpAddress.get(HttpAddress.user(),"update"));
                            System.out.println(result.toString());
                            if(result.getCode()!=200)
                            {
                                Toast.makeText(context, "修改失败", Toast.LENGTH_SHORT).show();
                            }else{
                                tempuser = DatabaseUtil.getEntity(result, User.class);
                                curuser = tempuser;
                                userManage.saveUserInfo(context,curuser);
                                Toast.makeText(context, "修改成功", Toast.LENGTH_SHORT).show();
                                email.setText(changedmessage.getText().toString().trim());
                                dialog1.dismiss();
                            }

                        }
                    }
                });
                dialog1.show();
                break;

            case R.id.line2:
                Intent it = new Intent(context, AddressActivity.class);
                startActivity(it);
//                title.setText("设置地址");
//                changedmessage.setInputType(InputType.TYPE_CLASS_TEXT);
//                if(userManage.isEmpty(curuser.getAddress())){
//                    changedmessage.setHint("请输入地址");
//                }
//                else{
//                    changedmessage.setText(address.getText().toString().replace(" ","") );
//                    changedmessage.setSelection(changedmessage.getText().toString().length());
//                }
//                cancelbt.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog1.dismiss();
//                    }
//                });
//                successbt.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if(UserManage.isEmpty(changedmessage.getText().toString()))
//                        {
//                            Toast.makeText(context, "输入信息为空", Toast.LENGTH_SHORT).show();
//                        }
//                        else{
//                            User tempuser = curuser;
//                            tempuser.setAddress(changedmessage.getText().toString().trim());
//                            Result result = DatabaseUtil.updateById(tempuser,
//                                    HttpAddress.get(HttpAddress.user(),"update"));
//                            System.out.println(result.toString());
//                            if(result.getCode()!=200)
//                            {
//                                Toast.makeText(context, "修改失败", Toast.LENGTH_SHORT).show();
//                            }else{
//                                tempuser = DatabaseUtil.getEntity(result, User.class);
//                                curuser = tempuser;
//                                userManage.saveUserInfo(context,curuser);
//                                Toast.makeText(context, "修改成功", Toast.LENGTH_SHORT).show();
//                                address.setText(changedmessage.getText().toString().trim());
//                                dialog1.dismiss();
//                            }
//
//                        }
//                    }
//                });
//                dialog1.show();
                break;

            case R.id.profile_img:
                title.setText("设置头像");
                changedmessage.setVisibility(View.GONE);
                imglayout.setVisibility(View.VISIBLE);
                if(curuser.getAvatar()!=null)
                {
                    img.setImageBitmap(AvatarUtil.getPicFromBytes(curuser.getAvatar(),null) );
                }
//                img.setImageBitmap();
                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent("android.intent.action.GET_CONTENT");
                        //把所有照片显示出来
                        intent.setType("image/*");
                        startActivityForResult(intent,123);

                    }

                });

                cancelbt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog1.dismiss();
                    }
                });
                successbt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            if(imgbyte!=null) {
                                User tempuser =curuser;
                                tempuser.setAvatar(imgbyte);

                                Result result = DatabaseUtil.updateById(tempuser,
                                        HttpAddress.get(HttpAddress.user(), "update"));
                                System.out.println(result.toString());
                                if (result.getCode() != 200) {
                                    Toast.makeText(context, "修改失败", Toast.LENGTH_SHORT).show();
                                } else {
                                    tempuser = DatabaseUtil.getEntity(result, User.class);
                                    curuser = tempuser;
                                    userManage.saveUserInfo(context, curuser);
                                    Toast.makeText(context, "修改成功", Toast.LENGTH_SHORT).show();
                                    dialog1.dismiss();
                                    profile_image.setImageBitmap(AvatarUtil.getPicFromBytes(imgbyte,null));
                                }
                            }else{
                                Toast.makeText(context, "未选择头像", Toast.LENGTH_SHORT).show();
                            }

                        }
                });
                dialog1.show();

                break;
            case R.id.line1:
                title.setText("设置手机号");
                changedmessage.setInputType(InputType.TYPE_CLASS_NUMBER);
                if(userManage.isEmpty(curuser.getTel())){
                    changedmessage.setHint("请输入手机号");
                }
                else{
                    changedmessage.setText(tel.getText().toString().replace(" ","") );
                    changedmessage.setSelection(changedmessage.getText().toString().length());
                }
                cancelbt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog1.dismiss();
                    }
                });
                successbt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(UserManage.isEmpty(changedmessage.getText().toString()))
                        {
                            Toast.makeText(context, "输入信息为空", Toast.LENGTH_SHORT).show();
                        }else if(changedmessage.getText().toString().length()<11)
                        {
                            Toast.makeText(context, "手机号不能少于11位", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            User tempuser = curuser;
                            tempuser.setTel(changedmessage.getText().toString().trim());
                            Result result = DatabaseUtil.updateById(tempuser,
                                    HttpAddress.get(HttpAddress.user(),"update"));
                            System.out.println(result.toString());
                            if(result.getCode()!=200)
                            {
                                Toast.makeText(context, "修改失败", Toast.LENGTH_SHORT).show();
                            }else{
                                tempuser = DatabaseUtil.getEntity(result, User.class);
                                curuser =tempuser;
                                userManage.saveUserInfo(context,curuser);
                                Toast.makeText(context, "修改成功", Toast.LENGTH_SHORT).show();
                                tel.setText(changedmessage.getText().toString().trim());
                                dialog1.dismiss();
                            }

                        }
                    }
                });
                dialog1.show();

                break;
            case R.id.line4:
                title.setText("设置");
                changedmessage.setVisibility(View.GONE);
                LinearLayout upgradelayout = contentView1.findViewById(R.id.upgradevip);
                LinearLayout linearLayout = contentView1.findViewById(R.id.logoutlayout);
                if(curuser.getUserpermission()<1)
                {
                    upgradelayout.setVisibility(View.VISIBLE);
                }
                linearLayout.setVisibility(View.VISIBLE);
                successbt.setVisibility(View.GONE);
                cancelbt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog1.dismiss();
                    }
                });
                upgradelayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        User tempuser = curuser;
                        Toast.makeText(context, "升级了会员", Toast.LENGTH_SHORT).show();
                        tempuser.setUserpermission(1);
                        Result result = DatabaseUtil.updateById(tempuser,
                                HttpAddress.get(HttpAddress.user(),"update"));
                        System.out.println(result.toString());
                        if(result.getCode()!=200)
                        {
                            Toast.makeText(context, "升级失败", Toast.LENGTH_SHORT).show();
                        }else{
                            tempuser = DatabaseUtil.getEntity(result, User.class);
                            curuser =tempuser;
                            userManage.saveUserInfo(context,curuser);
                            Toast.makeText(context, "升级成功", Toast.LENGTH_SHORT).show();
                            dialog1.dismiss();
                            if(curuser.getUserpermission()<1)
                            {
                                orderline.setVisibility(View.GONE);
                            }else{
                                orderline.setVisibility(View.VISIBLE);
                            }
                        }

                    }
                });
                linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UserManage userManage = UserManage.getInstance();
                        userManage.clearUserInfo(context);
                        Intent it = new Intent(context, LoginActivity.class);
                        startActivity(it);
                        getActivity().finish();
                    }
                });
//                successbt.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if(UserManage.isEmpty(changedmessage.getText().toString()))
//                        {
//                            Toast.makeText(context, "输入信息为空", Toast.LENGTH_SHORT).show();
//                        }else if(changedmessage.getText().toString().length()<11)
//                        {
//                            Toast.makeText(context, "手机号不能少于11位", Toast.LENGTH_SHORT).show();
//                        }
//                        else{
//                            User tempuser = curuser;
//                            tempuser.setTel(changedmessage.getText().toString().trim());
//                            Result result = DatabaseUtil.updateById(tempuser,
//                                    HttpAddress.get(HttpAddress.user(),"update"));
//                            System.out.println(result.toString());
//                            if(result.getCode()!=200)
//                            {
//                                Toast.makeText(context, "修改失败", Toast.LENGTH_SHORT).show();
//                            }else{
//                                tempuser = DatabaseUtil.getEntity(result,User.class);
//                                curuser =tempuser;
//                                userManage.saveUserInfo(context,curuser);
//                                Toast.makeText(context, "修改成功", Toast.LENGTH_SHORT).show();
//                                tel.setText(changedmessage.getText().toString().trim());
//                                dialog1.dismiss();
//                            }
//
//                        }
//                    }
//                });

                dialog1.show();
                break;
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(dialog1!=null &&dialog1.isShowing())
        {
            dialog1.dismiss();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //当选择完相片，就会回到这里，然后相片的相关信息会保存在data中，后面想办法取出来
        if (requestCode==123){
            if(data!=null){
                //通过getData方法取得它的图片地址，后面的操作都是对这个地址的解析
                uri=data.getData();
                if(uri!=null)
                {
//                    img.setImageURI(uri);
                    try {
                        imgbitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //图片缩放
                    imgbitmap = AvatarUtil.zoomBitmap( imgbitmap, AvatarUtil.dp2px(context,100), AvatarUtil.dp2px(context,100));

                    //圆角显示
                    imgbitmap = AvatarUtil.bimapRound( imgbitmap ,100);

                    img.setImageBitmap(imgbitmap);

                    // 再转为byte quality是图片压缩度 100为无损
                    imgbyte = AvatarUtil.Bitmap2Bytes( imgbitmap,80);



                }
                else {
                    Toast.makeText(context, "获取图片失败", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                uri =null;
                Toast.makeText(context, "返回信息失败", Toast.LENGTH_SHORT).show();
            }


        }
    }
}