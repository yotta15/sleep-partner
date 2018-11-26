package com.example.gzy.test3.model;

import cn.bmob.v3.BmobObject;

/**
 * created by gzy on 2018/11/20.
 * sleep 和 user 是多对一关系（pointer）
 * 用户的睡眠表，包括字段：操作用户，睡觉时间，起床时间，闹钟时间，是否设置闹钟，闹钟延迟时间
 * Describle;
 */
public class SleepModel extends BmobObject{
    private UserModel user;
    private String sleepTime;
    private String getupTime;
    private String alarmTime;
    private Boolean isSetAlarm;
    private String delayTime;

    public String getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(String delayTime) {
        this.delayTime = delayTime;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public String getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(String sleepTime) {
        this.sleepTime = sleepTime;
    }

    public String getGetupTime() {
        return getupTime;
    }

    public void setGetupTime(String getupTime) {
        this.getupTime = getupTime;
    }

    public String getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(String alarmTime) {
        this.alarmTime = alarmTime;
    }

    public Boolean getSetAlarm() {
        return isSetAlarm;
    }

    public void setSetAlarm(Boolean setAlarm) {
        isSetAlarm = setAlarm;
    }
}
