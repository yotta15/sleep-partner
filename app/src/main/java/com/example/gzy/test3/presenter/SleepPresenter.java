package com.example.gzy.test3.presenter;

import android.support.design.widget.Snackbar;
import android.util.Log;

import com.example.gzy.test3.model.SleepModel;
import com.example.gzy.test3.model.UserModel;

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
    private UserPresenterImpl userPresenter=new UserPresenterImpl();

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
     *  object 查询对象
     */
    public void queryPostAuthor() {

        if (userPresenter.isLogin()) {
            BmobQuery<SleepModel> query = new BmobQuery<>();
            query.addWhereEqualTo("user", BmobUser.getCurrentUser(UserModel.class));
            query.order("-updatedAt");
            //包含作者信息
            query.include("user");
            query.findObjects(new FindListener<SleepModel>() {

                @Override
                public void done(List<SleepModel> object, BmobException e) {

                    if (e == null) {
                        Log.e("BMOB", "查询成功");
//                       sleepModels=object;
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
}