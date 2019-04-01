package com.example.gzy.test3.presenter;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.Toast;

import com.example.gzy.test3.R;
import com.example.gzy.test3.model.UserModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FetchUserInfoListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

import static cn.bmob.v3.Bmob.getApplicationContext;
import static cn.bmob.v3.Bmob.getFilesDir;

/**
 * created by gzy on 2019/3/27.
 * Describle;
 */
public class UserPresenterImpl implements IUserPresenter {
    public boolean isLogin() {
        UserModel user = BmobUser.getCurrentUser(UserModel.class);
        if (user != null) {
            return true;
        } else {
            return false;
        }

    }

    //根据key 返回value，泛型方法
    public <T> T queryData(String key) {
        T value;
        if (isLogin()) {
            UserModel user = BmobUser.getCurrentUser(UserModel.class);
            // Snackbar.make(view, "当前用户：" + user.getUsername() + "-" + user.getAge(), Snackbar.LENGTH_LONG).show();
            //String username = (String) BmobUser.getObjectByKey("username");
            value = (T) BmobUser.getObjectByKey(key);
            //  Snackbar.make(view, "当前用户属性：" + username + "-" + age, Snackbar.LENGTH_LONG).show();
        } else {
            value = null;
            //Snackbar.make(view, "尚未登录，请先登录", Snackbar.LENGTH_LONG).show();
        }
        return value;
    }

    public void downloadFile(BmobFile file) {
        //允许设置下载文件的存储路径，默认下载文件的目录为：context.getApplicationContext().getCacheDir()+"/bmob/"

        File saveFile = new File(Environment.getExternalStorageDirectory()
                +"/"+ getApplicationContext().getResources().getString(R.string.default_file_name), "head_image");
      //  File saveFile = new File(Environment.getExternalStorageDirectory(), file.getFilename());
        if(!saveFile.exists()){
            saveFile.mkdirs();
        }
        file.download(saveFile, new DownloadFileListener() {

            @Override
            public void onStart() {
                // toast("开始下载...");
            }

            @Override
            public void done(String savePath, BmobException e) {
                if (e == null) {
                    Log.i("message", "下载成功,保存路径" + savePath);
                    //  toast("下载成功,保存路径:"+savePath);
                } else {
                    Log.i("error", "下载失败：" + e.getErrorCode() + "," + e.getMessage());
                    //toast("下载失败："+e.getErrorCode()+","+e.getMessage());
                }
            }

            @Override
            public void onProgress(Integer value, long newworkSpeed) {
             //   Log.i("bmob", "下载进度：" + value + "," + newworkSpeed);
            }

        });
    }

    public void uploadHeadImage(String urlpath) {

        final BmobFile bmobFile = new BmobFile(new File(urlpath));

        bmobFile.uploadblock(new UploadFileListener() {

            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Log.i("method","used");
                    saveFile(bmobFile);
                } else {
                }
            }
        });
    }

    private void saveFile(BmobFile file) {
        UserModel user =new UserModel();
        UserModel user1 = BmobUser.getCurrentUser(UserModel.class);
        user.setUser_pic(file);
        user.update(user1.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Log.i("upload", "success");
                    // Toast.makeText(MainActivity.this,"上传成功",Toast.LENGTH_SHORT).show();
                } else {
                    Log.i("fail", e.getMessage());
                    // Toast.makeText(MainActivity.this,"上传失败",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public static Bitmap getBitMBitmap(String urlpath) {

        Bitmap map = null;
        try {
            URL url = new URL("file://" + urlpath);
            URLConnection conn = url.openConnection();
            conn.connect();
            InputStream in;
            in = conn.getInputStream();
            map = BitmapFactory.decodeStream(in);
            // TODO Auto-generated catch block
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 同步控制台数据到缓存中
     *
     * @param
     */
    private void fetchUserInfo() {
        BmobUser.fetchUserInfo(new FetchUserInfoListener<BmobUser>() {
            @Override
            public void done(BmobUser user, BmobException e) {
                if (e == null) {
                    final UserModel myUser = BmobUser.getCurrentUser(UserModel.class);
                    // Snackbar.make(view, "更新用户本地缓存信息成功："+myUser.getUsername()+"-"+myUser.getAge(), Snackbar.LENGTH_LONG).show();
                } else {
                    Log.e("error", e.getMessage());
                    //Snackbar.make(view, "更新用户本地缓存信息失败：" + e.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }
}
