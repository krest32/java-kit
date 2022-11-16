package com.krest.activiti.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

public class User {
    private Integer id;
    private String name;
    private String loginname;
    private Integer sex;
    private String remark;
    private String pwd;
    private String salt;
    public User(Integer id, String name, String loginname, Integer sex, String remark, String pwd, String salt) {
        this.id = id;
        this.name = name;
        this.loginname = loginname;
        this.sex = sex;
        this.remark = remark;
        this.pwd = pwd;
        this.salt = salt;
    }

    public User() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLoginname() {
        return loginname;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}
