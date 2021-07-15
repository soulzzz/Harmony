package com.xmut.harmony.entity;

import java.io.Serializable;
import java.util.List;

public class User  implements  Serializable {
    private Integer id;

    private String username;

    private String password;

    private String email;

    private Integer age;

    private List<UserAddress> address;

    private String tel;

    private byte[] avatar;

    private String openid;

    private String pushtoken;

    public String getPushtoken() {
        return pushtoken;
    }

    public void setPushtoken(String pushtoken) {
        this.pushtoken = pushtoken;
    }

    public Integer getUserpermission() {
        return userpermission;
    }

    public void setUserpermission(Integer userpermission) {
        this.userpermission = userpermission;
    }

    private Integer userpermission;

    public User(String username, String password, String email, String openid) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.openid = openid;
        this.userpermission  = 0;
    }


    public User( String username , String password,String email) {
        this.password = password;
        this.username = username;
        this.email = email;
        this.userpermission  = 0;

    }
    public User(String  username, String password) {
        this.password = password;
        this.username = username;
        this.userpermission  = 0;

    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public List<UserAddress> getAddress() {
        return address;
    }

    public void setAddress(List<UserAddress> address) {
        this.address = address;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", address='" + address + '\'' +
                ", openid='" + openid + '\'' +
                ", userpermission=" + userpermission +
                '}';
    }
}
