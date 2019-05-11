package com.example.gzy.test3.presenter;

import android.os.Environment;
import android.util.Log;

import com.example.gzy.test3.model.SleepModel;
import com.example.gzy.test3.model.UserModel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * created by gzy on 2019/4/26.
 * Describle;
 */
public class SleepPresenter {
    private UserPresenterImpl userPresenter = new UserPresenterImpl();
    List<SleepModel> AllSleepinfoList = new ArrayList<>();
    List<SleepModel> PresentSleepinfoList = new ArrayList<>();
    int page = 0;

    /**
     * 添加一对一关联，当前用户睡眠
     */
    public void saveSleep(SleepModel sleepModel) {
        if (userPresenter.isLogin()) {
            sleepModel.setUser(BmobUser.getCurrentUser(UserModel.class));
            sleepModel.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        Log.e("BMOB", "保存成功");
                        // Snackbar.make(mFabAddPost, "发布帖子成功：" + s, Snackbar.LENGTH_LONG).show();
                    } else {
                        Log.e("BMOB", e.toString());
                        //  Snackbar.make(mFabAddPost, e.getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            Log.e("BMOB", "请先登录");
            // Snackbar.make(mFabAddPost, "请先登录", Snackbar.LENGTH_LONG).show();
        }
    }

    /**
     * 查询一对一关联，查询当前用户所有睡眠表
     * object 查询对象
     */
    public void querySleepUser(int page) {
        final int p=page;
        if (userPresenter.isLogin()) {
            BmobQuery<SleepModel> query = new BmobQuery<>();
            query.addWhereEqualTo("user", BmobUser.getCurrentUser(UserModel.class));
            query.order("-updatedAt");
            //包含作者信息
            query.include("user");
            query.setLimit(500);
            query.setSkip(page * 500);
            query.findObjects(new FindListener<SleepModel>() {

                @Override
                public void done(List<SleepModel> object, BmobException e) {

                    if (e == null&&object.size()==500) {
                        Log.e("BMOB", object.size() + " ");
                        PresentSleepinfoList.addAll(object);
                        querySleepUser(p + 1);
//                       sleepModels=object;
                    } else if (object.size() < 500) {
                        return;
                    }else {
                        Log.e("BMOB", e.toString());
                        //  Snackbar.make(mFabAddPost, e.getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                }

            });
        } else {
            Log.e("BMOB", "请先登录");
            //  Snackbar.make(mFabAddPost, "请先登录", Snackbar.LENGTH_LONG).show();
        }

    }

    public void findAll() {
        querySleep(page);
        querySleepUser(page);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(60000);
                    writeAllListToFile(AllSleepinfoList);
                    writeListToFile(PresentSleepinfoList);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 查询用户所有睡眠表
     * object 查询对象
     */
    public void  querySleep(int page) {
        final int p = page;
       // final Boolean[] flag = {false};
        if (userPresenter.isLogin()) {
            BmobQuery<SleepModel> query = new BmobQuery<>();
            // query.addWhereEqualTo("user", BmobUser.getCurrentUser(UserModel.class));
            query.order("-updatedAt");
            query.setLimit(500);
            query.setSkip(page * 500);
            //包含作者信息
            query.include("user");
            query.findObjects(new FindListener<SleepModel>() {

                @Override
                public void done(List<SleepModel> object, BmobException e) {

                    if (e == null && object.size() == 500) {
                        Log.e("BMOB", object.size() + "  ");
                        AllSleepinfoList.addAll(object);
                        querySleep(p + 1);

//                       sleepModels=object;
                    } else if (object.size() < 500) {
                      //    flag[0] =true;
                    } else {
                        Log.e("BMOB", e.toString());
                        //  Snackbar.make(mFabAddPost, e.getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                }

            });
        } else {
            Log.e("BMOB", "请先登录");
            //  Snackbar.make(mFabAddPost, "请先登录", Snackbar.LENGTH_LONG).show();
        }
    }

    /**
     * 单个用户按照时间统计，时间跨度为一周、一月、一年
     *
     * @param list
     * @throws IOException
     */
    public void writeListToFile(List<SleepModel> list) throws IOException {
        File StFile = new File(Environment.getExternalStorageDirectory()
                + "/" + "SleepPartner", "SleepUser.txt");

        if (!StFile.exists()) {
            StFile.createNewFile();
        }else{
            StFile.delete();
            StFile.createNewFile();
        }
//TODO
        BufferedWriter bw = null;

        try {
            bw = new BufferedWriter(new FileWriter(StFile));

            for (int i = 0; i < list.size(); i++) {
                bw.write(list.get(i).getUser().getUsername() + "," + list.get(i).getSleepdate() + "," +
                        list.get(i).getStarttime() + "," + list.get(i).getTotalsleep() + "," + list.get(i).getEndtime());
                bw.newLine();

            }
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 统计所有用户starttime和endtime，
     *
     * @param list
     * @throws IOException
     */
    public void writeAllListToFile(List<SleepModel> list) throws IOException {
        File StFile = new File(Environment.getExternalStorageDirectory()
                + "/" + "SleepPartner", "SleepUsersStarttime.txt");
        File EtFile = new File(Environment.getExternalStorageDirectory()
                + "/" + "SleepPartner", "SleepUsersEndtime.txt");
        if (!StFile.exists() && !EtFile.exists()) {
            StFile.createNewFile();
            EtFile.createNewFile();
        }else{
            StFile.delete();
            EtFile.delete();
            StFile.createNewFile();
            EtFile.createNewFile();
        }
//TODO
        BufferedWriter bw = null;
        BufferedWriter bw1 = null;
        try {
            bw = new BufferedWriter(new FileWriter(StFile));
            bw1 = new BufferedWriter(new FileWriter(EtFile));
            for (int i = 0; i < list.size(); i++) {
                bw.write(list.get(i).getStarttime() + "\t");
                bw1.write(list.get(i).getEndtime() + "\t");
                if (i % 10 == 0) {
                    bw.newLine();
                    bw1.newLine();
                }
            }
            bw.close();
            bw1.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}