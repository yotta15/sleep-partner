package com.example.gzy.test3.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;

import com.example.gzy.test3.R;
import com.example.gzy.test3.util.SystemHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * created by gzy on 2019/4/19.
 * Describle;
 * 振动是第一要素，语音识别第二要素，音量回调是第三要素,睡觉时间是第四要素
 */
public class WriteDataUtil {
    private static float aweakThreshold = 40;
    private static float lightSleepThreshold = 10;
    private static float deepSleepThreshold = 4;
    private static float staticThreshold = 0.5f;
    private static boolean setAccurate;
    HashMap<Float, Integer> hashMap = new HashMap<>();
    Handler handler = new Handler();
    Runnable runnable;
    private int[] count = new int[4];
    private ConvertJsonUtil convertJsonUtil;
    private String DEFAULT_TIME_FORMAT = "HH:mm";
    private String DEFAULT_DATE_FORMAT = "YYYY-MM-dd";
    int delaymills;
    SQLiteDatabase db;
    String LastUpdateTime;

    public void onCreate(Context context) {
        LastUpdateTime = String.valueOf(System.currentTimeMillis());
        convertJsonUtil = new ConvertJsonUtil();
        SimpleDateFormat dateFormatter = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        SimpleDateFormat timeFormatter = new SimpleDateFormat(DEFAULT_TIME_FORMAT);
        String sleepdate = dateFormatter.format(Calendar.getInstance().getTime());
        String starttime = timeFormatter.format(Calendar.getInstance().getTime());
        convertJsonUtil.praseJson("sleepdate", sleepdate);//写入sleepdate
        convertJsonUtil.praseJson("starttime", starttime);//写入starttime1

        delaymills = SystemHelper.batteryInfo(context);

        db = SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory()
                + "/" + context.getResources().getString(R.string.default_file_name) + "/sensor.db3", null);
        createtable();

        runnable = new Runnable() {
            public void run() {
                // TODO Auto-generated method stub 
                handler.postDelayed(runnable, delaymills);
                SensorDatahand();
            }
        };

        handler.postDelayed(runnable,5000);

    }
    public void createtable(){
        String sql="select count(*)  from sqlite_master where type='table' and name = 'sensor_info'";
        if(0==querydata(sql,null).getCount()){
            String createSensorTable = "create table if not exists sensor_info(time primary key,delta )";
            db.execSQL(createSensorTable);
        }else {
            deletedata("sensor_info",null,null);
        }
        String sql2="select count(*)  from sqlite_master where type='table' and name = 'audio_info'";
        if(0==querydata(sql2,null).getCount()) {
            String createAudioTable = "create table if not exists audio_info(time primary key,data )";
            db.execSQL(createAudioTable);
        }else {
            deletedata("audio_info",null,null);
        }
        String sql3="select count(*)  from sqlite_master where type='table' and name = 'volume_info'";
        int i=querydata(sql3,null).getCount();
        if(0 == i) {
            String createVolumeTable = "create table if not exists  volume_info(time  primary  key , data )";
            db.execSQL(createVolumeTable);
        }else {
            deletedata("volume_info",null,null);
        }
    }
    public void countMap(HashMap<String, Integer> hashMap) {
//        hashMap.put("awake", count[0]);
//        hashMap.put("lightSleep", count[1]);
//        hashMap.put("deepSleep", count[2]);
//        hashMap.put("static", count[3]);

        // hashMap.clear();
    }

    //sql处理，
    public void SensorDatahand() {
        //总数
        String sql = "select * from sensor_info where time > ? ";
        Cursor cursor = querydata(sql, new String[]{LastUpdateTime});
        //大于 aweakThreshold
        String sql1 = "select * from sensor_info where time > ? and delta > ?";
        Cursor cursor1 = querydata(sql1, new String[]{LastUpdateTime, String.valueOf(aweakThreshold)});

        //介于lightSleepThreshold 和 aweakThreshold
        String sql2 = "select * from sensor_info where time > ? and delta between ? and ?";
        Cursor cursor2 = querydata(sql2, new String[]{LastUpdateTime, String.valueOf(lightSleepThreshold), String.valueOf(aweakThreshold)});

        //介于deepSleepThreshold 和 lightSleepThreshold
        String sql3 = "select * from sensor_info where time > ? and delta between ? and ?";
        Cursor cursor3 = querydata(sql3, new String[]{LastUpdateTime, String.valueOf(deepSleepThreshold), String.valueOf(lightSleepThreshold)});

        //介于staticThreshold 和deepSleepThreshod
        String sql4 = "select * from sensor_info where time > ? and delta between ? and ?";
        Cursor cursor4 = querydata(sql4, new String[]{LastUpdateTime, String.valueOf(staticThreshold), String.valueOf(deepSleepThreshold)});

        //小于staticThreshold
        String sql5 = "select * from sensor_info where time > ? and delta < ?";
        Cursor cursor5 = querydata(sql5, new String[]{LastUpdateTime, String.valueOf(staticThreshold)});

        // 总数
        String sql6="select * from audio_info where time > ? ";
        Cursor cursor6 = querydata(sql6, new String[]{LastUpdateTime});

        //String[] keywords=new String[]{"呼","嗯",""};
        // 总数 ,
        String sql8="select * from volume_info where time > ? order by data desc "; //降序排列
        Cursor cursor8=querydata(sql8,new String[]{LastUpdateTime});

        //预设的关键词
      //  String sql7="select * from audio_info where time > ? and data"

        if (0 == cursor.getCount() ) {
            convertJsonUtil.praseJson("sleepstate",SleepConstant.UNDETECTED);

        }
        if (cursor1.getCount() != 0) {
            convertJsonUtil.praseJson("sleepstate", SleepConstant.SLEEP_AWAKE);
           // Log.v("awake11111111",cursor1.getCount()+"");
            //todo 醒着,接着判断各占比
        }else {
//            float cu1=(float) cursor1.getCount()/(float) cursor.getCount();
//           hashMap.put((float) cursor2.getCount()/(float) cursor.getCount(),SleepConstant.SLEEP_LIGHT);
            float cu2=(float) cursor2.getCount()/(float) cursor.getCount();
//            hashMap.put((float) cursor2.getCount()/(float) cursor.getCount(),SleepConstant.SLEEP_LIGHT);
            float cu3=(float) cursor3.getCount()/(float) cursor.getCount();
            float cu4=(float) cursor4.getCount()/(float) cursor.getCount();
            float cu5=(float) cursor5.getCount()/(float) cursor.getCount();

             cursor8.moveToFirst();
             String maxvloume=cursor8.getString(1);
             cursor8.moveToLast();
             String minvloume=cursor8.getString(1);
             float cu6;//深睡概率
             float cu7;//浅睡概率
             if(Double.parseDouble(maxvloume)-Double.parseDouble(minvloume)< 8d){
                 cu6=0.8f;
                 cu7=0.2f;
             }else {
                 cu6=0.2f;
                 cu7=0.8f;
             }
             //通过降噪处理，只会识别文字
            if( cursor6.getCount()>0){
                 cu6+=0.4f;
                 cu7+=0.6f;
            }else {
                 cu6+= 0.6f;
                 cu7+= 0.4f;
            }

            float awake=cu2*0.6f+cu3*0.4f;
            float lightsleep=cu2*0.4f+cu3*0.4f+cu4*0.2f+cu5*0.6f+cu7;
            float deepsleep=cu3*0.2f+cu4*0.8f+cu5*0.4f+cu6;
             if(awake >=lightsleep&&awake>deepsleep){
                 convertJsonUtil.praseJson("sleepstate", SleepConstant.SLEEP_AWAKE);
             }else if(lightsleep>=deepsleep &&lightsleep>awake){
                 convertJsonUtil.praseJson("sleepstate", SleepConstant.SLEEP_LIGHT);
             }else {
                 convertJsonUtil.praseJson("sleepstate", SleepConstant.SLEEP_DEEP);
             }
           //
        }

        LastUpdateTime = String.valueOf(System.currentTimeMillis());

    }

    private Cursor querydata(String sql, String[] selectionArgs) {
        return db.rawQuery(sql, selectionArgs);
    }

    //插入返回添加的行号，发生错误返回-1
    private Boolean insertdata(String table, ContentValues contentValues) {
        if(db.isOpen()){
            if(db.insert(table, null, contentValues)>0){
                return true;
            }else {
                return  false;
            }
        }else {
            return false;
        }
    }

    //受此删除语句影响的记录的行数
    private int deletedata(String table, String whereclause, String[] whereargs) {
        return db.delete(table, whereclause, whereargs);
    }

    /**
     * 根据手机加速度传感器获得的值进行计算
     *
     * @param delta 震动值
     */
    public void SensorData(String delta) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("time", String.valueOf(System.currentTimeMillis()));
        contentValues.put("delta", delta);
        insertdata("sensor_info", contentValues);

    }

    /**
     * 语音解析结果回调
     *
     * @param data 解析结果
     */
    public void audioData(String data) {
        Log.i("audiodata1111111", data);
        ContentValues contentValues = new ContentValues();
        contentValues.put("time", String.valueOf(System.currentTimeMillis()));
        contentValues.put("data", data);
        insertdata("audio_info", contentValues);
    }

    /**
     * 语音音调 回调
     *
     * 睡眠呼吸暂停
     * 最长呼吸暂停时间
     * 呼吸暂停总时间
     * 平均呼吸暂停时间
     *
     * @param data
     */
    public void volumeData(double data) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("time", String.valueOf(System.currentTimeMillis()));
        contentValues.put("data", String.valueOf(data));
        insertdata("volume_info", contentValues);
    }



    public void ondestroy() {
        handler.removeCallbacks(runnable);
        SimpleDateFormat timeFormatter = new SimpleDateFormat(DEFAULT_TIME_FORMAT);
        convertJsonUtil.praseJson("endtime",timeFormatter.format(Calendar.getInstance().getTime()));
        convertJsonUtil.endRecording();
        if (db != null && db.isOpen()) {
            deletedata("sensor_info",null,null);
            deletedata("audio_info",null,null);
            deletedata("volume_info",null,null);
            db.close();
        }
    }
}
