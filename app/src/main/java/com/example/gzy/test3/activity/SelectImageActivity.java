package com.example.gzy.test3.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.gzy.test3.BuildConfig;
import com.example.gzy.test3.R;
import com.foamtrace.photopicker.ImageCaptureManager;
import com.foamtrace.photopicker.PhotoPickerActivity;
import com.foamtrace.photopicker.PhotoPreviewActivity;
import com.foamtrace.photopicker.SelectModel;
import com.foamtrace.photopicker.intent.PhotoPickerIntent;
import com.foamtrace.photopicker.intent.PhotoPreviewIntent;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.app.job.JobScheduler.RESULT_SUCCESS;


public class SelectImageActivity extends AppCompatActivity implements View.OnClickListener {
    private int columnWidth;
    private ArrayList<String> imagePaths = null;
   // private GridAdapter gridAdapter;
    private GridView gv;
    private ImageCaptureManager captureManager; // 相机拍照处理类
    private static final int REQUEST_CAMERA_CODE = 11;
    private static final int RESULT_SUCCEED = 12;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
        PhotoPickerIntent intent = new PhotoPickerIntent(SelectImageActivity.this);
        intent.setSelectModel(SelectModel.SINGLE);
        intent.setShowCarema(true);
        startActivityForResult(intent, REQUEST_CAMERA_CODE);


//        setContentView(R.layout.selectimage);
//        Button danxuan = (Button) findViewById(R.id.danxuan);
//        Button duoxuan = (Button) findViewById(R.id.duoxuan);
//        Button paizhao = (Button) findViewById(R.id.paizhao);
//        gv = (GridView) findViewById(R.id.gv);
//
//
//        //得到GridView中每个ImageView宽高
//        int cols = getResources().getDisplayMetrics().widthPixels / getResources().getDisplayMetrics().densityDpi;
//        cols = cols < 3 ? 3 : cols;
//        gv.setNumColumns(cols);
//        int screenWidth = getResources().getDisplayMetrics().widthPixels;
//        int columnSpace = getResources().getDimensionPixelOffset(R.dimen.space_size);
//        columnWidth = (screenWidth - columnSpace * (cols-1)) / cols;
//
//
//        danxuan.setOnClickListener(this);
//        duoxuan.setOnClickListener(this);
//        paizhao.setOnClickListener(this);
//        //GridView item点击事件（浏览照片）
//        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                PhotoPreviewIntent intent = new PhotoPreviewIntent(SelectImageActivity.this);
//                intent.setCurrentItem(position);
//                intent.setPhotoPaths(imagePaths);
//                startActivityForResult(intent, 22);
//            }
//        });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            //单选图片
            case R.id.danxuan:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                    StrictMode.setVmPolicy(builder.build());
                }
                PhotoPickerIntent intent = new PhotoPickerIntent(SelectImageActivity.this);
                intent.setSelectModel(SelectModel.SINGLE);
                intent.setShowCarema(true);
                startActivityForResult(intent, REQUEST_CAMERA_CODE);

                break;
            //多选图片
            case R.id.duoxuan:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                    StrictMode.setVmPolicy(builder.build());
                }
                PhotoPickerIntent intent1 = new PhotoPickerIntent(SelectImageActivity.this);
                intent1.setSelectModel(SelectModel.MULTI);
                intent1.setShowCarema(true); // 是否显示拍照
                intent1.setMaxTotal(9); // 最多选择照片数量，默认为9
                intent1.setSelectedPaths(imagePaths); // 已选中的照片地址， 用于回显选中状态
                startActivityForResult(intent1, REQUEST_CAMERA_CODE);
                break;
            //拍照
            case R.id.paizhao:
                try {

                    if(captureManager == null){
                        captureManager = new ImageCaptureManager(SelectImageActivity.this);
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                        StrictMode.setVmPolicy(builder.build());
                        Intent intent3 = dispatchTakePictureIntent();

                        startActivityForResult(intent3, ImageCaptureManager.REQUEST_TAKE_PHOTO);
                    }else{
                        Intent intent3 = dispatchTakePictureIntent();

                        startActivityForResult(intent3, ImageCaptureManager.REQUEST_TAKE_PHOTO);
                    }

                } catch (IOException e) {
                    Toast.makeText(SelectImageActivity.this, com.foamtrace.photopicker.R.string.msg_no_camera, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                break;
        }
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
                        FileProvider.getUriForFile(getApplicationContext(), "com.example.gzy.test3.fileProvider", createImageFile()));
            }
        }
        return takePictureIntent;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
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
        Log.i("selectImage",bundle.getString("head_image"));
        SelectImageActivity.this.setResult(RESULT_OK,intent3);
        SelectImageActivity.this.finish();
    }
   public String  returnPicture(){
      String imagePath;
         if(!imagePaths.isEmpty()){
           imagePath= imagePaths.get(0);

         }else{
             return null;
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
//    private class GridAdapter extends BaseAdapter {
//        private ArrayList<String> listUrls;
//
//        public GridAdapter(ArrayList<String> listUrls) {
//            this.listUrls = listUrls;
//        }
//
//        @Override
//        public int getCount() {
//            return listUrls.size();
//        }
//
//        @Override
//        public String getItem(int position) {
//            return listUrls.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            ImageView imageView;
//            if(convertView == null){
//                convertView = getLayoutInflater().inflate(R.layout.item_image, null);
//                imageView = (ImageView) convertView.findViewById(R.id.imageView);
//                convertView.setTag(imageView);
//                // 重置ImageView宽高
//                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(columnWidth, columnWidth);
//                imageView.setLayoutParams(params);
//            }else {
//                imageView = (ImageView) convertView.getTag();
//            }
//            //框架里自带glide
//            Glide.with(SelectImageActivity.this)
//                    .load(new File(getItem(position)))
//                    .placeholder(R.mipmap.default_error)
//                    .error(R.mipmap.default_error)
//                    .centerCrop()
//                    .crossFade()
//                    .into(imageView);
//            return convertView;
//        }
//    }

}