package com.example.gzy.test3.model;

import cn.bmob.v3.BmobObject;

/**
 * created by gzy on 2018/11/20.
 * sleep 和 user 是多对一关系（pointer）
 * 用户的睡眠表，包括字段：操作用户，睡觉时间，起床时间，闹钟时间，是否设置闹钟，
 * Describle;
 */
public class SleepModel extends BmobObject{
    private UserModel user;
    private String starttime;
    private String endtime;
    //private String alarmTime;

    private String dreamland;
    private String beforesleep;
    private String noise;
    private float score;

    public String getDreamland() {
        return dreamland;
    }

    public void setDreamland(String dreamland) {
        this.dreamland = dreamland;
    }

    public String getBeforesleep() {
        return beforesleep;
    }

    public void setBeforesleep(String beforesleep) {
        this.beforesleep = beforesleep;
    }

    public String getNoise() {
        return noise;
    }

    public void setNoise(String noise) {
        this.noise = noise;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

}
