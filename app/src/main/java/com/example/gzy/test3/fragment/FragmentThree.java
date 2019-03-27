package com.example.gzy.test3.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gzy.test3.R;
import com.example.gzy.test3.activity.LoginActivity;
import com.example.gzy.test3.activity.SelectImageActivity;
import com.example.gzy.test3.model.UserModel;
import com.example.gzy.test3.presenter.IUserPresenter;
import com.example.gzy.test3.presenter.UserPresenterImpl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;
//import me.iwf.photopicker.PhotoPicker;
//import me.iwf.photopicker.PhotoPreview;

import static android.app.Activity.RESULT_OK;

//调用Bmobobject类中的setValue（key，value）方法，只需要传入key及想要更新的值即可
public class FragmentThree extends Fragment {
    TextView mTVname, mTVage, mTVsex, mTVweight, mTVheight, mTVemail;
    View view;
    UserPresenterImpl userPresenter;
    CircleImageView mHeadImage;
    ArrayList<String> photos;
    String imagePath;
    //未登录提示登录
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.personalfragment, container, false);
        photos = new ArrayList<String>();
        mHeadImage = (CircleImageView) view.findViewById(R.id.head_image);
        mHeadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),SelectImageActivity.class);
                startActivityForResult(intent,1);

            }
        });
        //回调获得图片路径，并生成bitmap
        if(imagePath!=null){
            Log.i("image",imagePath);
            mHeadImage.setImageBitmap(getBitMBitmap(imagePath));
        }
//        Intent toGallery = new Intent(Intent.ACTION_GET_CONTENT);
//        toGallery.setType("image/*");
//        toGallery.addCategory(Intent.CATEGORY_OPENABLE);
//        startActivityForResult(toGallery, RequestCodes.PICK_PHOTO);


        userPresenter = new UserPresenterImpl();
        if (userPresenter.isLogin()) {

        } else {
            showMessage();
        }
        return view;

    }
    /**
     * 根据路径 转bitmap
     * @param urlpath
     * @return
     */
    public static Bitmap getBitMBitmap(String urlpath) {

        Bitmap map = null;
        try {
            URL url = new URL(urlpath);
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


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_OK){
           imagePath=data.getExtras().getString("head_image");
        }
//        if (resultCode == RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE) {
//            if (data != null) {
//                photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
//                for (int i = 0; i < photos.size(); i++) {
//                    Log.e("1111===", photos.get(i).toString());
//                }
//            }
      //  }
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

    public void queryData() {
        if (userPresenter.isLogin()) {
            UserModel user = BmobUser.getCurrentUser(UserModel.class);
            Snackbar.make(view, "当前用户：" + user.getUsername() + "-" + user.getAge(), Snackbar.LENGTH_LONG).show();
            String username = (String) BmobUser.getObjectByKey("username");
            Integer age = (Integer) BmobUser.getObjectByKey("age");
            Snackbar.make(view, "当前用户属性：" + username + "-" + age, Snackbar.LENGTH_LONG).show();
        } else {
            Snackbar.make(view, "尚未登录，请先登录", Snackbar.LENGTH_LONG).show();
        }
    }
}
