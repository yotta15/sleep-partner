package com.example.gzy.test3.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.gzy.test3.R;
import com.example.gzy.test3.UI.Toastview;

import java.io.File;
import java.io.IOException;

/**
 * created by gzy on 2019/3/7.
 * Describle;
 */
public class RecordingService extends  Service{
    private String mFileName;
    private String mFilePath;
    private int count=0;
    private MediaRecorder mRecorder;
    private long mStartingTimeMillis;
    private long mElapsedMillis;

    private static final String TAG = "AudioRecord";
    static final int SAMPLE_RATE_IN_HZ = 8000;
    static final int BUFFER_SIZE = AudioRecord.getMinBufferSize(SAMPLE_RATE_IN_HZ, AudioFormat.CHANNEL_IN_DEFAULT, AudioFormat.ENCODING_PCM_16BIT);
    AudioRecord mAudioRecord;
    boolean isGetVoiceRun;
    Object mLock;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mLock = new Object();
        startRecording();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (mRecorder != null) {
            stopRecording();
        }
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // 开始录音
    public void startRecording() {
        setFileNameAndPath();

        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);//输入口为麦克风
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4); //录音文件保存的格式，这里保存为 mp4
        mRecorder.setOutputFile(mFilePath); // 设置录音文件的保存路径
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mRecorder.setAudioChannels(1);
        // 设置录音文件的清晰度
        mRecorder.setAudioSamplingRate(44100);
        mRecorder.setAudioEncodingBitRate(192000);

        try {
            mRecorder.prepare();
            mRecorder.start();
            mStartingTimeMillis = System.currentTimeMillis();
        } catch (IOException e) {

            Log.e("recoder", "prepare() failed");
        }
    }

    // 设置录音文件的名字和保存路径
    public void setFileNameAndPath() {
        File f;
        Toast.makeText(getApplicationContext(),"file is ready",Toast.LENGTH_LONG).show();
        do {
            count++;
            mFileName = getString(R.string.default_file_name)
                    + "_" + (System.currentTimeMillis()) + ".mp4";
            mFilePath = Environment.getExternalStorageDirectory().getPath();
            mFilePath += "/12/" + mFileName;
            f = new File(mFilePath);
        } while (f.exists() && !f.isDirectory());
    }

    // 停止录音
    public void stopRecording() {
        mRecorder.stop();
        mElapsedMillis = (System.currentTimeMillis() - mStartingTimeMillis);
        mRecorder.release();
        //存入数据
        getSharedPreferences("sp_name_audio", MODE_PRIVATE)
                .edit()
                .putString("audio_path", mFilePath)
                .putLong("elpased", mElapsedMillis)
                .apply();

//        if (mIncrementTimerTask != null) {
//            mIncrementTimerTask.cancel();
//            mIncrementTimerTask = null;
//        }

        mRecorder = null;
    }
    public void getNoiseLevel() {
        if (isGetVoiceRun) {
            Log.e(TAG, "还在录着呢");
            return;
        }
        mAudioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                SAMPLE_RATE_IN_HZ, AudioFormat.CHANNEL_IN_DEFAULT,
                AudioFormat.ENCODING_PCM_16BIT, BUFFER_SIZE);
        if (mAudioRecord == null) {
            Log.e("sound", "mAudioRecord初始化失败");
        }
        isGetVoiceRun = true;

        new Thread(new Runnable() {
            @Override
            public void run() {
                mAudioRecord.startRecording();
                short[] buffer = new short[BUFFER_SIZE];
                while (isGetVoiceRun) {
                    //r是实际读取的数据长度，一般而言r会小于buffersize
                    int r = mAudioRecord.read(buffer, 0, BUFFER_SIZE);
                    long v = 0;
                    // 将 buffer 内容取出，进行平方和运算
                    for (int i = 0; i < buffer.length; i++) {
                        v += buffer[i] * buffer[i];
                    }
                    // 平方和除以数据总长度，得到音量大小。
                    double mean = v / (double) r;
                    double volume = 10 * Math.log10(mean);

                    Log.d(TAG, "分贝值:" + volume);
                    // 大概一秒十次
                    synchronized (mLock) {
                        try {
                            mLock.wait(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                mAudioRecord.stop();
                mAudioRecord.release();
                mAudioRecord = null;
            }
        }).start();
    }

    /**
     * 停止录音
     */
    public void stopRecord(){
        mAudioRecord.stop();
        mAudioRecord.release();
    }
}
