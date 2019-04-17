package com.example.gzy.test3.pageslice;

import com.example.gzy.test3.model.SleepInfo;
import com.example.gzy.test3.model.SleepstateBean;

import java.io.Serializable;
import java.util.List;

/**
 * created by gzy on 2019/4/15.
 * Describle;
 */
public class DataBean implements Serializable {
    public String name;
    public SleepInfo sleepInfo;

    public SleepInfo getSleepInfo() {
        return sleepInfo;
    }

    public void setSleepInfo(SleepInfo sleepInfo) {
        this.sleepInfo = sleepInfo;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
