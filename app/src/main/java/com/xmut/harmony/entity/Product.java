package com.xmut.harmony.entity;



import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.List;


public class Product implements Cloneable,Comparable<Product>,Serializable
{
    private int store_id; //Order

    private String product_id;

    private String product_name;

    private String product_img;

    private double product_price;

    private boolean isfirst = false;

    private int belongto = -1;

    private String product_category;

    private String product_des;

    private int product_stock;

    private boolean isShow;

    private List<ProductComment> productCommentList;


    private String order_id;

    public int getStore_id() {
        return store_id;
    }

    public void setStore_id(int store_id) {
        this.store_id = store_id;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public List<ProductComment> getProductCommentList() {
        return productCommentList;
    }

    public void setProductCommentList(List<ProductComment> productCommentList) {
        this.productCommentList = productCommentList;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public int getProduct_num() {
        return product_num;
    }

    public void setProduct_num(int product_num) {
        this.product_num = product_num;
    }

    private int product_num;


    private int isshow;

    public int getIsshow() {
        return isshow;
    }

    public void setIsshow(int isshow) {
        this.isshow = isshow;
    }

    public String getProduct_id() {
        return product_id;
    }


    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_img() {
        return product_img;
    }

    public void setProduct_img(String product_img) {
        this.product_img = product_img;
    }

    public double getProduct_price() {
        return product_price;
    }

    public void setProduct_price(double product_price) {
        this.product_price = product_price;
    }

    public String getProduct_category() {
        return product_category;
    }

    public void setProduct_category(String product_category) {
        this.product_category = product_category;
    }

    public String getProduct_des() {
        return product_des;
    }

    public void setProduct_des(String product_des) {
        this.product_des = product_des;
    }

    public int getProduct_stock() {
        return product_stock;
    }

    public void setProduct_stock(int product_stock) {
        this.product_stock = product_stock;
    }

    @Override
    public String toString() {
        return "Product{" +
                "product_id=" + product_id +
                ", product_category='" + product_category + '\'' +
                '}';
    }

    @Override
    public int compareTo(Product o) {
        return this.getProduct_category().compareTo(o.getProduct_category());
    }
    public int getBelongto() {
    return belongto;
}

    public void setBelongto(int belongto) {
        this.belongto = belongto;
    }

    public boolean isIsfirst() {
        return isfirst;
    }

    public void setIsfirst(boolean isfirst) {
        this.isfirst = isfirst;
    }
    @NonNull
    @Override
    public Object clone() throws CloneNotSupportedException {
        Product  p=null;
        try{
            p = (Product)super.clone();
        }catch (CloneNotSupportedException e)
        {
            e.printStackTrace();
        }
        return p;
    }
}
