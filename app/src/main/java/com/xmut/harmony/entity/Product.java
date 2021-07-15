package com.xmut.harmony.entity;



import androidx.annotation.NonNull;

import java.io.Serializable;


public class Product implements Serializable,Cloneable,Comparable<Product>
{
    int product_id;

    String product_name;



    String product_img;

    double product_price;

    boolean isfirst = false;

    int belongto = -1;

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

    String product_category;

    String product_des;

    int product_stock;


    public int getProduct_id() {
        return product_id;
    }


    public void setProduct_id(int product_id) {
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
}
