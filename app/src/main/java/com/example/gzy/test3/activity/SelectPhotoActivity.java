package com.example.gzy.test3.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.GridView;

import com.foamtrace.photopicker.ImageCaptureManager;
import com.foamtrace.photopicker.PhotoPickerActivity;
import com.foamtrace.photopicker.PhotoPreviewActivity;
import com.foamtrace.photopicker.SelectModel;
import com.foamtrace.photopicker.intent.PhotoPickerIntent;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * created by gzy on 2019/3/28.
 * Describle;
 */
public class SelectPhotoActivity extends AppCompatActivity{

    private ArrayList<String> imagePaths = null;
    private GridView gv;
    private ImageCaptureManager captureManager; // 相机拍照处理类
    private static final int REQUEST_CAMERA_CODE = 11;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imagePaths = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
        PhotoPickerIntent intent = new PhotoPickerIntent(SelectPhotoActivity.this);
        intent.setSelectModel(SelectModel.SINGLE);
        intent.setShowCarema(true);
        startActivityForResult(intent, REQUEST_CAMERA_CODE);
    }
    private File createImageFile() throws IOException {

        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (!storageDir.exists()) {
            if (!storageDir.mkdir()) {
                throw new IOException();
            }
        }
        //  ClipData.Item.getUri()
        File image = new File(storageDir, imageFileName + ".jpg");
        // Save a file: path for use with ACTION_VIEW intents
        String  mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


    /**
     android 7.0已上遇到 android.os.FileUriExposedException: file:///storage/emulated.. exposed beyond app through Intent.getData()
     为了提高私有目录的安全性，防止应用信息的泄漏，从 Android 7.0 开始，应用私有目录的访问权限被做限制。
     具体表现为，开发人员不能够再简单地通过 file:// URI 访问其他应用的私有目录文件或者让其他应用访问自己的私有目录文件。
     */
    public Intent dispatchTakePictureIntent() throws IOException {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = createImageFile();
            // Continue only if the File was successfully created
            //需要传递三个参数。第二个参数便是 Manifest 文件中注册 FileProvider 时设置的 authorities 属性值，
            // 第三个参数为要共享的文件，并且这个文件一定位于第二步我们在 path 文件中添加的子目录里面。
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        FileProvider.getUriForFile(getApplicationContext(),
                                "com.example.gzy.test3.fileProvider", createImageFile()));
            }
        }
        return takePictureIntent;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){

                case RESULT_CANCELED:
                    Log.i("button","CANCELED");

                    Intent intent3= new Intent();
                    Bundle bundle=new Bundle();
                    bundle.putString("head_image","CANCELD");
                    intent3.putExtras(bundle);
                 //   Log.i("selectImage",bundle.getString("head_image"));
                    SelectPhotoActivity.this.setResult(RESULT_OK,intent3);
                    SelectPhotoActivity.this.finish();
                // 选择照片
                case REQUEST_CAMERA_CODE:

                    loadAdpater(data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT));

                    break;
                //浏览照片
                case 22:
                    loadAdpater(data.getStringArrayListExtra(PhotoPreviewActivity.EXTRA_RESULT));
                    break;
                // 调用相机拍照
                case ImageCaptureManager.REQUEST_TAKE_PHOTO:

                    if(captureManager.getCurrentPhotoPath() != null) {
                        captureManager.galleryAddPic();

                        ArrayList<String> paths = new ArrayList<>();
                        paths.add(captureManager.getCurrentPhotoPath());
                        loadAdpater(paths);
                    }
                    break;

            }
        }
        Intent intent3= new Intent();
        Bundle bundle=new Bundle();
        bundle.putString("head_image",returnPicture());
        intent3.putExtras(bundle);
       // Log.i("selectImage",bundle.getString("head_image"));
        SelectPhotoActivity.this.setResult(RESULT_OK,intent3);
        SelectPhotoActivity.this.finish();
    }
    public String  returnPicture(){

        String imagePath;
        if(!imagePaths.isEmpty()){
            imagePath= imagePaths.get(0);

        }else{
            return "";
        }
        return imagePath;
    }
    private void loadAdpater(ArrayList<String> paths){
        if(imagePaths == null){
            imagePaths = new ArrayList<>();
        }
        imagePaths.clear();
        imagePaths.addAll(paths);
//        if(gridAdapter == null){
//            gridAdapter = new GridAdapter(imagePaths);
//            gv.setAdapter(gridAdapter);
//        }else {
//            gridAdapter.notifyDataSetChanged();
//        }
    }
}
