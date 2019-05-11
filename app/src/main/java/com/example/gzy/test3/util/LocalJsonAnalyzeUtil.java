package com.example.gzy.test3.util;

/**
 * created by gzy on 2019/4/1.
 * Describle;获取本地Json文件并 解析
 */
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class LocalJsonAnalyzeUtil {
    public static String getUserStandard(Context context, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        HashMap<String,Integer> hashMap=new HashMap();
        //获得assets资源管理器
        AssetManager assetManager = context.getAssets();
        int  hour=0,min=0;
        //使用IO流读取json文件内容
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName), "utf-8"));
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                //String[] str=line.split("\t");
                hour+=Integer.parseInt(((line.split("\t"))[1].split(":"))[0]);
                min+=Integer.parseInt(((line.split("\t"))[1].split(":"))[1]);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return hour+":"+min ;
    }
    public static String getStandard(Context context, String fileName) {
        HashMap<String,Integer> hashMap=new HashMap();
        //获得assets资源管理器
        AssetManager assetManager = context.getAssets();
        //使用IO流读取json文件内容
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName), "utf-8"));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] str=line.split("\t");
                hashMap.put(str[0], Integer.parseInt(str[1]));
              //  stringBuilder.append(line);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        int value=0;
        String key="";
        for (Map.Entry<String, Integer> entry : hashMap.entrySet()) {
            if(value<= entry.getValue()){
                key=entry.getKey();
                value=entry.getValue();
            }
        }

        return key;
    }

    public static String getJson(Context context, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        //获得assets资源管理器
        AssetManager assetManager = context.getAssets();
        //使用IO流读取json文件内容
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName), "utf-8"));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
    public static String getJsonFromExternal(Context context, String fileName)  {
        StringBuilder stringBuilder = new StringBuilder();
        //获得assets资源管理器

        //使用IO流读取json文件内容
        try {
            FileInputStream f = new FileInputStream(fileName);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                    f, "utf-8"));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public static <T> T JsonToObject(String json, Class<T> type) {
        Gson gson = new Gson();
        return gson.fromJson(json, type);
    }

    public static <T> T JsonToObject(Context context, String fileName, Class<T> type) {
        Gson gson = new Gson();
        return gson.fromJson(getJson(context, fileName), type);
    }
    public static <T> T JsonToObjectFromExternal(Context context, String fileName, Class<T> type) {
        Gson gson = new Gson();
        return gson.fromJson(getJsonFromExternal(context, fileName), type);
    }
}
