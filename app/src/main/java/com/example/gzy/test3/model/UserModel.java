package com.example.gzy.test3.model;

/**
 * CREATED BY GZY
 * 基础的用户表，包含字段:姓名，电话，密码，身高体重，性别，邮箱，地区以及用户等级
 */

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

public class UserModel  extends BmobUser {
    private BmobFile User_pic;
    private String sex;
    private Integer age;
    private String area;
    private Integer grade;
    private Integer height;
    private Integer  weight;



    public  UserModel(){ }


    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }


    public BmobFile getUser_pic() {
        return User_pic;
    }

    public void setUser_pic(BmobFile user_pic) {
        User_pic = user_pic;
    }
}
