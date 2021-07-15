package com.xmut.harmony.entity;


import java.io.Serializable;
import java.util.List;


public class Store implements Serializable {
    int store_id;

    String store_name;

    String store_img;

    String store_des;

    List<Product> store_products;

    public int getStore_id() {
        return store_id;
    }

    public void setStore_id(int store_id) {
        this.store_id = store_id;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getStore_img() {
        return store_img;
    }

    public void setStore_img(String store_img) {
        this.store_img = store_img;
    }

    public String getStore_des() {
        return store_des;
    }

    public void setStore_des(String store_des) {
        this.store_des = store_des;
    }

    public List<Product> getStore_products() {
        return store_products;
    }

    public void setStore_products(List<Product> store_products) {
        this.store_products = store_products;
    }

    @Override
    public String toString() {
        return "Store{" +
                "store_id=" + store_id +
                ", store_name='" + store_name + '\'' +
                ", store_img='" + store_img + '\'' +
                ", store_des='" + store_des + '\'' +
                ", store_products=" + store_products +
                '}';
    }
}
