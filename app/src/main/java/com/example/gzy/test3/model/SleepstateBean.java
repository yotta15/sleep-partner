package com.example.gzy.test3.model;

import java.io.Serializable;

/**
 * created by gzy on 2019/4/4.
 * Describle;
 */
public class SleepstateBean implements Serializable{
    /**
     * fvalue : 8
     * sleeptime : 22:.06:30
     */

    private int fvalue;
    private String sleeptime;

    public int getFvalue() {
        return fvalue;
    }

    public void setFvalue(int fvalue) {
        this.fvalue = fvalue;
    }

    public String getSleeptime() {
        return sleeptime;
    }

    public void setSleeptime(String sleeptime) {
        this.sleeptime = sleeptime;
    }
}
