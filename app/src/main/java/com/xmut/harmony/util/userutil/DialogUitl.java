package com.xmut.harmony.util.userutil;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

import com.xmut.harmony.R;

public class DialogUitl {
    private static DialogUitl instance;

    public static DialogUitl getInstance() {
        if (instance == null) {
            instance = new DialogUitl();
        }
        return instance;
    }

    public void setDialogTransparentandCircle(Dialog dialog){
        Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.BottomDialogAnimation);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //设置dialog的背景颜色为透明色,就可以显示圆角了!!
    }

    public void setDialogMatchParent(Context context,Dialog dialog){

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();

        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;// 屏幕宽度（像素）
        int height= dm.heightPixels; // 屏幕高度（像素）
        float density = dm.density;//屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = dm.densityDpi;//屏幕密度dpi（120 / 160 / 240）
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        //这个地方可以用ViewGroup.LayoutParams.MATCH_PARENT属性，各位试试看看有没有效果
        layoutParams.width = width;
        layoutParams.height = height;
        dialog.getWindow().setAttributes(layoutParams);
    }

    public void setDialogWidthAndHeight(Context context,Dialog dialog,int widthDp,int heightDp){


        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;// 屏幕宽度（像素）
        int height= dm.heightPixels; // 屏幕高度（像素）
        float density = dm.density;//屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = dm.densityDpi;//屏幕密度dpi（120 / 160 / 240）
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.width = (int) (widthDp*densityDpi/160);
        layoutParams.height = (int) (heightDp*densityDpi/160);
        dialog.getWindow().setAttributes(layoutParams);
    }
}
