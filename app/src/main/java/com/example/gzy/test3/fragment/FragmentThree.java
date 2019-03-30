package com.example.gzy.test3.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.Key;
import com.example.gzy.test3.R;
import com.example.gzy.test3.activity.LoginActivity;
import com.example.gzy.test3.activity.ModifyInfoActivity;
import com.example.gzy.test3.activity.SelectImageActivity;
import com.example.gzy.test3.activity.SelectPhotoActivity;
import com.example.gzy.test3.model.UserModel;
import com.example.gzy.test3.presenter.IUserPresenter;
import com.example.gzy.test3.presenter.UserPresenterImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.UploadFileListener;
import de.hdodenhof.circleimageview.CircleImageView;
//import me.iwf.photopicker.PhotoPicker;
//import me.iwf.photopicker.PhotoPreview;

import static android.app.Activity.RESULT_OK;
import static cn.bmob.v3.Bmob.getApplicationContext;

//调用Bmobobject类中的setValue（key，value）方法，只需要传入key及想要更新的值即可
public class FragmentThree extends Fragment implements View.OnClickListener {
    TextView mTVname, mTVage, mTVsex, mTVweight, mTVheight, mTVemail;
    TextView mTVSetinfo;
    ImageView mIVsetingo;
    View view;
    UserPresenterImpl userPresenter;
    CircleImageView mHeadImage;
    ArrayList<String> photos;
    String imagePath;


    //未登录提示登录
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.personalfragment, container, false);
        photos = new ArrayList<String>();
        userPresenter = new UserPresenterImpl();
        if (userPresenter.isLogin()) {

        } else {
            showMessage();
        }

        UserModel user = BmobUser.getCurrentUser(UserModel.class);
        mTVname=(TextView) view.findViewById(R.id.tv_name);
        mTVname.setText(userPresenter.<String>queryData("username"));
        //TODO 查询本地,没有照片查询bmob，下载保存本地
        mHeadImage = (CircleImageView) view.findViewById(R.id.head_image);
        //  mHeadImage.setImageBitmap(((BitmapDrawable) getResources().getDrawable(R.drawable.info_headimage)).getBitmap());
      //  BmobFile bmobfile = userPresenter.<BmobFile>queryData("User_pic");
        BmobFile bmobfile=null;
        if(userPresenter.isLogin()){
             user = BmobUser.getCurrentUser(UserModel.class);
             bmobfile =user.getUser_pic();
        }else{
            Log.i("mess","未登录");
        }

        if(bmobfile!=null){
            Log.i("bmobfile",bmobfile.getFilename());
            userPresenter.downloadFile(bmobfile);
            String filepath = Environment.getExternalStorageDirectory()
                    + getApplicationContext().getResources().getString(R.string.default_file_name) + bmobfile.getFilename();
            mHeadImage.setImageBitmap(decodeFile(filepath));
        }




        //TODO 相机拍摄的照片是倒的 ，选择取消之后应该是之前的照片
        mHeadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), SelectPhotoActivity.class);
                startActivityForResult(intent, 1);
                //回调获得图片路径，并生成bitmap
//                if(imagePath!=null){
//                    Log.i("image",imagePath);
//                    mHeadImage.setImageBitmap(getBitMBitmap(imagePath));
//                }

            }
        });
        mTVSetinfo = (TextView) view.findViewById(R.id.tv_setinfo);
        mTVSetinfo.setOnClickListener(this);
        mIVsetingo = (ImageView) view.findViewById(R.id.iv_setinfo);
        mIVsetingo.setOnClickListener(this);


        return view;

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_setinfo:
            case R.id.iv_setinfo:
                Intent intent = new Intent(getActivity(), ModifyInfoActivity.class);
                startActivity(intent);
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
    public static Bitmap decodeFile(String filePath) {
        Bitmap b = null;
        int IMAGE_MAX_SIZE = 600;
        File f = new File(filePath);
        if (f == null) {
            return null;
        }
        //Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BitmapFactory.decodeStream(fis, null, o);
        try {
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int scale = 1;
        if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
            scale = (int) Math.pow(2, (int) Math.round(Math.log(IMAGE_MAX_SIZE / (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
        }
        //Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        try {
            fis = new FileInputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        b = BitmapFactory.decodeStream(fis, null, o2);
        try {
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return b;
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data.getExtras().getString("head_image").equals("CANCELD") ||
                    data.getExtras().getString("head_image").equals("")) {
                mHeadImage.setImageBitmap(((BitmapDrawable) getResources().getDrawable(R.drawable.info_headimage)).getBitmap());
            } else {
                imagePath = data.getExtras().getString("head_image");
                Log.i("test", imagePath);
                mHeadImage.setImageBitmap(getBitMBitmap(imagePath));
                userPresenter.uploadHeadImage(imagePath);
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