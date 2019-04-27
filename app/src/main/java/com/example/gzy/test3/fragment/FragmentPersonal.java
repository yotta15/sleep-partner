package com.example.gzy.test3.fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.gzy.test3.R;

import com.example.gzy.test3.activity.LoginActivity;
import com.example.gzy.test3.activity.ModifyInfoActivity;
import com.example.gzy.test3.activity.SelectPicPopupWindow;
import com.example.gzy.test3.model.UserModel;
import com.example.gzy.test3.presenter.UserPresenterImpl;
import com.example.gzy.test3.bardata.GifSizeFilter;
import com.example.gzy.test3.bardata.GlideLoadEngine;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.internal.entity.CaptureStrategy;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import de.hdodenhof.circleimageview.CircleImageView;


import static android.app.Activity.RESULT_OK;
import static cn.bmob.v3.Bmob.getApplicationContext;

//调用Bmobobject类中的setValue（key，value）方法，只需要传入key及想要更新的值即可
public class FragmentPersonal extends Fragment implements View.OnClickListener {
    TextView mTVname;
    TextView mTVSetinfo;
    ImageView mIVsetingo, mIVset;
    View view;
    UserPresenterImpl userPresenter;
    CircleImageView mHeadImage;
    ArrayList<String> photos;
    String imagePath;
    BmobFile bmobfile;
    private static final int REQUEST_CODE_CHOOSE = 23;//定义请求码常量

    //未登录提示登录
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.personalfragment, container, false);
        photos = new ArrayList<String>();
        userPresenter = new UserPresenterImpl();


        mTVname = (TextView) view.findViewById(R.id.tv_name);
        mTVname.setText(userPresenter.<String>queryData("username"));
        //TODO 查询本地,没有照片查询bmob，下载保存本地
        mHeadImage = (CircleImageView) view.findViewById(R.id.head_image);
        //  mHeadImage.setImageBitmap(((BitmapDrawable) getResources().getDrawable(R.drawable.info_headimage)).getBitmap());
        //BmobFile bmobfile = (BmobFile) userPresenter.<BmobFile>queryData("User_pic");

        if (userPresenter.isLogin()) {
            UserModel user = BmobUser.getCurrentUser(UserModel.class);
            // user = BmobUser.getCurrentUser(UserModel.class);
            bmobfile = user.getUser_pic();
        } else {
            Log.i("mess", "未登录");
        }
        final File saveFile = new File(Environment.getExternalStorageDirectory()
                + "/" + getApplicationContext().getResources().getString(R.string.default_file_name), "head_image");
        if (bmobfile != null && !saveFile.exists()) {
            //   Log.i("bmobfile",bmobfile.getFilename());

            /**耗时操作，等待下载
             * 我实在太聪明了，哈哈哈哈
             */
            new Thread(new Runnable() {
                @Override
                public void run() {

                    handler.sendEmptyMessage(-1);

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        mHeadImage.setImageBitmap(decodeFile(saveFile));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //  handler.sendEmptyMessage(-2);
                }
            }).start();

        }
        if (saveFile.exists() && userPresenter.isLogin()) {
            try {
                mHeadImage.setImageBitmap(decodeFile(saveFile));

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            mHeadImage.setImageBitmap(((BitmapDrawable) getResources().getDrawable(R.drawable.info_headimage)).getBitmap());
        }

        //   mHeadImage.setImageURI(Uri.parse("file://"+filepath));

        //TODO 相机拍摄的照片是倒的 ，选择取消之后应该是之前的照片
        mHeadImage.setOnClickListener(this);

        mTVSetinfo = (TextView) view.findViewById(R.id.tv_setinfo);
        mTVSetinfo.setOnClickListener(this);
        mIVsetingo = (ImageView) view.findViewById(R.id.iv_setinfo);
        mIVsetingo.setOnClickListener(this);
        mIVset = (ImageView) view.findViewById(R.id.iv_set);
        mIVset.setOnClickListener(this);
        return view;

    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            userPresenter.downloadFile(bmobfile);

        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_setinfo:
            case R.id.iv_setinfo:
                Intent intent1 = new Intent(getActivity(), ModifyInfoActivity.class);
                startActivity(intent1);
                break;
            case R.id.head_image:
                if (!userPresenter.isLogin()) {
                    final AlertDialog.Builder alterDiaglog = new AlertDialog.Builder(getActivity());
                    alterDiaglog.setIcon(R.drawable.zuji);//图标
                    alterDiaglog.setTitle("提示");//文字
                    alterDiaglog.setMessage("请先登入");//提示消息
                    //积极的选择
                    alterDiaglog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(getActivity(), LoginActivity.class));
                        }
                    });
                    //消极的选择
                    alterDiaglog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

                    //显示
                    alterDiaglog.show();


                } else {
                    Matisse.from(getActivity())
                            .choose(MimeType.ofImage(), false) // 选择 mime 的类型
                            .countable(true)
                            .addFilter(new GifSizeFilter())

                            .maxSelectable(1) // 图片选择的最多数量
                            .gridExpectedSize((int) getResources().getDimension(R.dimen.imageSelectDimen))
                            .capture(true)//选择照片时，是否显示拍照
                            .captureStrategy(new CaptureStrategy(true, "com.example.gzy.test3.fileProvider"))
                            //参数1 true表示拍照存储在共有目录，false表示存储在私有目录；参数2与 AndroidManifest中authorities值相同，用于适配7.0系统 必须设置
                            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                            .thumbnailScale(0.85f) // 缩略图的比例
                            .imageEngine(new GlideLoadEngine()) // 使用的图片加载引擎
                            .forResult(REQUEST_CODE_CHOOSE); // 设置作为标记的请求码
                }
                break;
            case R.id.iv_set:
                //TODO
                Intent intent3 = new Intent(getActivity(), SelectPicPopupWindow.class);
                startActivity(intent3);
                break;
        }
    }


    /**
     * 根据路径 转bitmap
     *
     * @param urlpath
     * @return
     */
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
     * 根据 路径 得到 file 得到 bitmap
     *
     * @return
     * @throws IOException
     */
    /**
     * 根据 路径 得到 file 得到 bitmap
     *
     * @return
     * @throws IOException
     */
    public static Bitmap decodeFile(File f) throws IOException {
        Bitmap b = null;
        int IMAGE_MAX_SIZE = 600;


        //Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;

        FileInputStream fis = new FileInputStream(f);
        BitmapFactory.decodeStream(fis, null, o);
        fis.close();

        int scale = 1;
        if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
            scale = (int) Math.pow(2, (int) Math.round(Math.log(IMAGE_MAX_SIZE / (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
        }

        //Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        fis = new FileInputStream(f);
        b = BitmapFactory.decodeStream(fis, null, o2);
        fis.close();
        return b;
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Uri> mSelected;
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_CHOOSE) {
            mSelected = Matisse.obtainResult(data);
            Log.d("Matisse", "mSelected: " + mSelected.get(0).toString());
            mHeadImage.setImageURI(mSelected.get(0));
            try {
                userPresenter.uploadHeadImage(mSelected.get(0));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void showMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("提示");
        builder.setMessage("请先登录");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //TODO 跳转到登录页
                dialogInterface.dismiss();
            }
        });
    }


}