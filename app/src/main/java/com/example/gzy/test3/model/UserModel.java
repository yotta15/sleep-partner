package com.example.gzy.test3.model;

import cn.bmob.v3.BmobObject;

public class UserModel  extends BmobObject{
    private String name;
    private String passwd;
    private String sex;
    private String age;
    private String area;
    private String qq;
    private String grade;
    private String height;
    private String weight;

    public int checkUserValidity(String name,String passwd){
        return  0;
    }
    public UserModel(String name, String passwd) {
        this.name = name;
        this.passwd = passwd;
    }

    public UserModel(String name, String passwd, String sex, String age, String area, String qq, String grade, String height, String weight) {
        this.name = name;
        this.passwd = passwd;
        this.sex = sex;
        this.age = age;
        this.area = area;
        this.qq = qq;
        this.grade = grade;
        this.height = height;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }




}
