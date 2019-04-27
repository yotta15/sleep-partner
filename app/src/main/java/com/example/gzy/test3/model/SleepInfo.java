package com.example.gzy.test3.model;

import java.util.List;

/**
 * created by gzy on 2019/4/3.
 * Describle;
 * 利用  GsonFormat  将json ->  class
 *  对应的字段名 (时间二十四小时)
 *   sleepstate:
 *     sleepdate 睡觉日期
 *     starttime 开始睡觉时间
 *     endtime   结束睡觉时间
 *     turnover   反转次数
 *     dreamtalk  梦话次数（梦话本地存储）
 *     beforesleep  睡前状态(枚举类型：兴奋、平淡、运动、压力、饥饿)
 *     noise     环境噪音
 *     score     总得分
 *     sleepstate:  {
 *         fvalue   睡眠值 （枚举类型：未检测到 5红 /醒 8黄/浅睡 10 紫/ 深睡 15绿   ）
 *         sleeptime    睡眠时间
 *     }
 */
public class SleepInfo {
    //todo  补充：闹钟、梦境
    /**
     * sleepdate : 2018-01-03
     * starttime : 22:05:30
     * endtime : 07:20:30
     * turnover : 5
     * dreamtalk : 2
     * beforesleep : excited
     * noise : 25db
     * score : 75
     * sleepstate : [{"fvalue":8,"sleeptime":"22:.06:30"},{"fvalue":8,"sleeptime":"22:.07:30"},{"fvalue":10,"sleeptime":"22:.08:30"},{"fvalue":10,"sleeptime":"22:.09:30"},{"fvalue":15,"sleeptime":"22:.10:30"},{"fvalue":15,"sleeptime":"22:.11:30"}]
     */

    private String sleepdate;
    private String starttime;
    private String endtime;
    private int turnover;
    private int dreamtalk;
    private String beforesleep;
    private String noise;
    private float score;
    private List<SleepstateBean> sleepstate;

    public String getSleepdate() {
        return sleepdate;
    }

    public void setSleepdate(String sleepdate) {
        this.sleepdate = sleepdate;
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

    public int getTurnover() {
        return turnover;
    }

    public void setTurnover(int turnover) {
        this.turnover = turnover;
    }

    public int getDreamtalk() {
        return dreamtalk;
    }

    public void setDreamtalk(int dreamtalk) {
        this.dreamtalk = dreamtalk;
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

    public List<SleepstateBean> getSleepstate() {
        return sleepstate;
    }

    public void setSleepstate(List<SleepstateBean> sleepstate) {
        this.sleepstate = sleepstate;
    }
}
