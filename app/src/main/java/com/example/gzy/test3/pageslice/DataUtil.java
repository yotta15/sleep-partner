package com.example.gzy.test3.pageslice;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.example.gzy.test3.util.LocalJsonAnalyzeUtil;
import com.example.gzy.test3.model.SleepInfo;
import com.example.gzy.test3.model.SleepstateBean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * created by gzy on 2019/4/15.
 * Describle;
 */
public class DataUtil {


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
            return null;
        }

 //TODO 当前目录下没有json文件，返回一个样例
      File[] subFile = file.listFiles();

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) && subFile!=null) {
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
     //   Log.i("vecfile",""+vecFile.size()+vecFile.get(0));
        return vecFile;
    }
    //用于返回当前目录下所有的 sleepInfo 实例
   public List<SleepInfo> GetAllSleepInfo(Context context){
       List<SleepInfo>  allSleepInfo = new ArrayList<>();
       String filepath = Environment.getExternalStorageDirectory()
               +"/SleepPartner";
       Vector<String> vecfile = GetJsonFileName(filepath);
       if(0== vecfile.size()){
           SleepInfo sleepInfo = LocalJsonAnalyzeUtil.JsonToObject(context,"sleep.json",SleepInfo.class);
           allSleepInfo.add(sleepInfo);

       }else {
           for (String s : vecfile) {
               SleepInfo sleepInfo = LocalJsonAnalyzeUtil.JsonToObjectFromExternal(context,
                       s, SleepInfo.class);
               allSleepInfo.add(sleepInfo);
           }
       }
      // Log.i("GetSleepList",""+allSleepstateBean.size());
       return allSleepInfo;
   }
    //用于返回目录下的所有sleepstateBean 的集合
    public List<List<SleepstateBean>> GetSleepList(Context context) {
        List<List<SleepstateBean>> allSleepstateBean = new ArrayList<>();
        String filepath = Environment.getExternalStorageDirectory()
                +"/SleepPartner";
        Vector<String> vecfile = GetJsonFileName(filepath);
        if(0== vecfile.size()){

                SleepInfo sleepInfo = LocalJsonAnalyzeUtil.JsonToObject(context,"sleep.json",SleepInfo.class);
                List<SleepstateBean> sleepstateBeans = sleepInfo.getSleepstate();
                allSleepstateBean.add(sleepstateBeans);

        }else {
            for (String s : vecfile) {
                SleepInfo sleepInfo = LocalJsonAnalyzeUtil.JsonToObjectFromExternal(context,
                        s, SleepInfo.class);
                List<SleepstateBean> sleepstateBeans = sleepInfo.getSleepstate();
                allSleepstateBean.add(sleepstateBeans);
            }
        }
        Log.i("GetSleepList",""+allSleepstateBean.size());
        return allSleepstateBean;
    }


}
