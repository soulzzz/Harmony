package com.xmut.harmony.util.userutil;

public class OrderUtil {
    public static String getStateName(Integer in){
        String out;
        switch (in){
            case 0:
                out = "已取消";
                break;
            case 1:
                out = "待付款";
                break;
            case 2:
                out = "待发货";
                break;
            case 3:
                out = "待收货";
                break;
            case 4:
                out =  "待评价";
                break;
            case 5:
                out =  "已评价";
                break;
            case -2:
                out ="退单中";
                break;
            case -3:
                out = "退单成功";
                break;
            default:
                out =  "出错";
                break;
        }
        return out;
    }
}
