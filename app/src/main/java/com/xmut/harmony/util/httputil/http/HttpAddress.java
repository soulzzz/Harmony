package com.xmut.harmony.util.httputil.http;

import java.io.Serializable;

public class HttpAddress {
    private static String[] args;
    final private static String userAddress="user";
    final private static String videoAddress="video";
    final private static String storeAddress = "store";
    final private static String orderAddress = "order";
    final private static String productAddress="product";
    public static String user(){
        return userAddress;
    }
    public static String video(){
        return videoAddress;
    }
    public static String store(){return storeAddress;}
    public static String order(){return orderAddress;}
    public static String product(){return productAddress;}

    /**
     *
     * @param address 首地址 例如：“user”
     * @param method 地址中的方法  例如： “insert”
     * @return
     */
    public static String[] get(String address,String method){
        switch (method){
            case "login":args=getLoginAddress();
                break;
            case "loginwithoutavatar":args=getLoginwithoutavatar();
                break;
            case "insert":args=getInsertAddress(address);
                break;
            case "update":args=getUpdateAddress(address);
                break;
            case "list":args=getListAddress(address);
                break;
            case "insertuseraddress":args = getInsertUserAddress(address);
                break;
            case "updateuseraddress":args = getUpdateUserAddress(address);
                break;
            case "insertcomment":args=getInsertComment(address);
                break;

        }
        return args;
    }



    /**
     *采用方法重载，分别处理两种情况，带id和不带id
     * @param address 首地址 例如：“user”
     * @param method 地址中的方法  例如： “delete”
     * @param id id则为相应参数  delete后的id参数
     * @return
     */
    public static String[] get(String address,String method,Serializable id){
        switch (method){
            case "delete":args=getDeleteAddress(address,id);
                break;
            case "line":args=getLineAddress(address,id);
                break;
            case "openid": args=getOpenid(address,id);
                break;
            case "deleteuseraddress":args = getDeleteUserAddress(address,id);
                    break;
            case "listUserAddress":args =getListUserAddress(address,id);
            break;
            case "list":args = getUserOrder(address,id);
            break;
            case "lists":args=getListsAddress(address,id);
                break;
        }
        return args;
    }

    private static String[] getUserOrder(String address, Serializable id) {
        args =new String[]{address,"list",String.valueOf(id)};
        return args;
    }
    private static String[] getListsAddress(String address, Serializable id) {
        args =new String[]{address,"lists",String.valueOf(id)};
        return args;
    }

    private static String[] getLoginAddress(){
        args=new String[]{userAddress,"login"};
        return args;
    }

    private static String[] getLoginwithoutavatar(){
        args=new String[]{userAddress,"loginwithoutavatar"};
        return args;
    }
    private static String[] getInsertAddress(String address){
        args=new String[]{address,"insert"};
        return args;
    }
    private static String[] getInsertComment(String address){
        args=new String[]{address,"insertcomment"};
        return args;
    }
    private static String[] getInsertUserAddress(String address){
        args=new String[]{address,"insertUserAddress"};
        return args;
    }
    private static String[] getDeleteAddress(String address, Serializable id){
        args=new String[]{address,"delete", String.valueOf(id)};
        return args;
    }
    private static String[] getUpdateAddress(String address){
        args=new String[]{address,"update"};
        return args;
    }
    private static String[] getLineAddress(String address, Serializable id){
        args=new String[]{address,"line", String.valueOf(id)};
        return args;
    }
    private static String[] getListAddress(String address){
        args=new String[]{address,"list"};
        return args;
    }
    private static String[] getListsAddress(String address){
        args=new String[]{address,"lists"};
        return args;
    }
    private static String[] getOpenid(String address,Serializable openid) {
        args=new String[]{address,"openid",openid.toString()};
        return args;
    }
    private static String[] getDeleteUserAddress(String address, Serializable id){
        args=new String[]{address,"deleteUserAddress", String.valueOf(id)};
        return args;
    }

    private static String[] getUpdateUserAddress(String address){
        args=new String[]{address,"updateUserAddress"};
        return args;
    }
    private static String[] getListUserAddress(String address, Serializable id){
        args=new String[]{address,"listUserAddress", String.valueOf(id)};
        return args;
    }

    }
