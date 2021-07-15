package com.xmut.harmony.entity;


import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Order implements Serializable ,Comparable<Order>{
    private String order_id;

    private int user_id;

    private int store_id;

    private String user_address;

    private String user_name;

    private String user_tel;

    private String order_state;

    private double order_price;

    private String order_date;

    private List<OrderProduct> orderProducts;

    public List<OrderProduct> getOrderProducts() {
        return orderProducts;
    }

    public void setOrderProducts(List<OrderProduct> orderProducts) {
        this.orderProducts = orderProducts;
    }



    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getStore_id() {
        return store_id;
    }

    public void setStore_id(int store_id) {
        this.store_id = store_id;
    }

    public String getUser_address() {
        return user_address;
    }

    public void setUser_address(String user_address) {
        this.user_address = user_address;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_tel() {
        return user_tel;
    }

    public void setUser_tel(String user_tel) {
        this.user_tel = user_tel;
    }

    public String getOrder_state() {
        return order_state;
    }

    public void setOrder_state(String order_state) {
        this.order_state = order_state;
    }

    public double getOrder_price() {
        return order_price;
    }

    public void setOrder_price(double order_price) {
        this.order_price = order_price;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    @Override
    public String toString() {
        return "Order{" +
                "order_id='" + order_id + '\'' +
                ", user_id=" + user_id +
                ", store_id=" + store_id +
                ", user_address='" + user_address + '\'' +
                ", user_name='" + user_name + '\'' +
                ", user_tel='" + user_tel + '\'' +
                ", order_state='" + order_state + '\'' +
                ", order_price=" + order_price +
                ", order_date=" + order_date +
                ", orderProducts=" + orderProducts +
                '}';
    }

    @Override
    public int compareTo(Order o) {
        return this.getOrder_date().compareTo(o.getOrder_date());
    }
}
