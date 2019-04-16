package com.example.gzy.test3.pageslice;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.example.gzy.test3.bardata.LocalJsonAnalyzeUtil;
import com.example.gzy.test3.model.SleepInfo;
import com.example.gzy.test3.model.SleepstateBean;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * created by gzy on 2019/4/15.
 * Describle;
 */
public class DataUtil {
    private BarChart barChart;
    private YAxis leftAxis;             //左侧Y轴
    private YAxis rightAxis;            //右侧Y轴
    private XAxis xAxis;                //X轴
    private Legend legend;              //图例
    private LimitLine limitLine;        //限制线

    List<Integer> yValues;
    List<String> xValues;

    View view;


    public List<SleepstateBean> returnlist(Context context) {
        SleepInfo sleepInfo = LocalJsonAnalyzeUtil.JsonToObject(context,
                "sleep.json", SleepInfo.class);
        List<SleepstateBean> sleepstateBeans = sleepInfo.getSleepstate();
        xValues = new ArrayList<>();
        yValues = new ArrayList<>();

        for (SleepstateBean statebean : sleepstateBeans) {
            xValues.add(statebean.getSleeptime());
        }
        Log.i("xvalue", "" + xValues.size());
        return sleepstateBeans;
    }

    // 获取当前目录下所有的json文件
    public static Vector<String> GetJsonFileName(String fileAbsolutePath) {
        Vector<String> vecFile = new Vector<String>();
      //  Log.i("fileAbsolutePath",fileAbsolutePath);

        String[] dataStr = fileAbsolutePath.split("/");
        String fileTruePath = "/sdcard";
        for(int i=4;i<dataStr.length;i++){
            fileTruePath = fileTruePath+"/"+dataStr[i];
        }


        File file = new File(fileTruePath);
        if (!file.exists()) {
          //  Log.i("file",fileAbsolutePath+"asdddassssssss");
            return null;
        }


      File[] subFile = file.listFiles();

        if (Environment.getExternalStorageState().
                equals(Environment.MEDIA_MOUNTED) && subFile!=null) {
            for (int iFileLength = 0; iFileLength < subFile.length; iFileLength++) {
                // 判断是否为文件夹
                if (!subFile[iFileLength].isDirectory()) {
                    String filename = subFile[iFileLength].getName();
                    // 判断是否为json结尾
                    if (filename.trim().toLowerCase().endsWith(".json")) {
                        vecFile.add(fileTruePath+"/"+filename);
                    }
                }
            }
        }
        Log.i("vecfile",""+vecFile.size()+vecFile.get(0));
        return vecFile;
    }

    //用于返回目录下的所有sleepstateBean 的集合
    public List<List<SleepstateBean>> GetSleepList(Context context) {
        List<List<SleepstateBean>> allSleepstateBean = new ArrayList<>();
        String filepath = Environment.getExternalStorageDirectory()
                +"/SleepPartner";
        Vector<String> vecfile = GetJsonFileName(filepath);
        for (String s : vecfile) {
            SleepInfo sleepInfo = LocalJsonAnalyzeUtil.JsonToObjectFromExternal(context,
                    s, SleepInfo.class);
            List<SleepstateBean> sleepstateBeans = sleepInfo.getSleepstate();
            allSleepstateBean.add(sleepstateBeans);
        }
        Log.i("GetSleepList",""+allSleepstateBean.size());
        return allSleepstateBean;
    }


}
