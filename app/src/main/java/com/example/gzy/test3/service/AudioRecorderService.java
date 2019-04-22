package com.example.gzy.test3.service;


import android.annotation.SuppressLint;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Binder;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;

import android.util.Log;


import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.baidu.speech.asr.SpeechConstant;
import com.example.gzy.test3.activity.ContentActivity;
import com.google.gson.JsonObject;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * created by gzy on 2019/4/8.
 * Describle;
 */
public class AudioRecorderService extends Service implements EventListener {
    private EventManager asr;

    private boolean logTime = true;
    private MyBinder myBinder = new MyBinder();
    protected boolean enableOffline = false; // 测试离线命令词，需要改成true
    private ArrayList<String> timearray = new ArrayList<>();
    private int current = -1;
    Handler handler = new Handler();
    Runnable runnable;
    double highestdb = 1;
    Callback callback;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 基于sdk集成1.1 初始化EventManager对象
        asr = EventManagerFactory.create(this, "asr");
        // 基于sdk集成1.3 注册自己的输出事件类
        asr.registerListener(this); //  EventListener 中 onEvent方法
        if (enableOffline) {
            loadOfflineEngine(); // 测试离线命令词请开启, 测试 ASR_OFFLINE_ENGINE_GRAMMER_FILE_PATH 参数时开启
        }
    }

    //打点测试，高低性能处理,音量检测，录音向前回溯1.5s
    private void loopRecord() {
        runnable = new Runnable() {
            public void run() {
                // TODO Auto-generated method stub  
                start();
                handler.postDelayed(this, 65000);
            }
        };
       handler.postDelayed(runnable, 2000);
    }

    public static interface Callback {
        void onDataChange(String data);
    }

    /**
     * 基于SDK集成2.2 发送开始事件
     */
    @SuppressLint("HandlerLeak")
    private void start() {
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        String event = null;
        event = SpeechConstant.ASR_START; // 替换成测试的event
        if (enableOffline) {
            params.put(SpeechConstant.DECODER, 2);
        }
        // 基于SDK集成2.1 设置识别参数
        params.put(SpeechConstant.ACCEPT_AUDIO_VOLUME, true);//语音音量回调，这里设置为true，开启后有CALLBACK_EVENT_ASR_VOLUME事件回调
        // params.put(SpeechConstant.NLU, "enable");
        // params.put(SpeechConstant.VAD_ENDPOINT_TIMEOUT, 800); // 开启长语音。尾点检测 3000ms
        // params.put(SpeechConstant.IN_FILE, "res:///com/baidu/android/voicedemo/16k_test.pcm");
        // params.put(SpeechConstant.VAD, SpeechConstant.VAD_DNN);
        // params.put(SpeechConstant.PID, 1537); // 中文输入法模型，有逗号
        //   params.put(SpeechConstant.ACCEPT_AUDIO_DATA, true);//需要音频数据回调
        timearray.add(String.valueOf(Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + "_" + ++current));
        //  params.put(SpeechConstant.OUT_FILE, Environment.getExternalStorageDirectory().getPath() + "/AudioRecord/" + timearray.get(current) + ".pcm");

        // 请先使用如‘在线识别’界面测试和生成识别参数。 params同ActivityRecog类中myRecognizer.start(params);
        // 复制此段可以自动检测错误
        (new AutoCheck(getApplicationContext(), new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == 100) {
                    AutoCheck autoCheck = (AutoCheck) msg.obj;
                    synchronized (autoCheck) {
                        String message = autoCheck.obtainErrorMessage(); // autoCheck.obtainAllMessage();
                        //     txtLog.append(message + "\n");
                        ; // 可以用下面一行替代，在logcat中查看代码
                        // Log.w("AutoCheckMessage", message);
                    }
                }
            }
        }, enableOffline)).checkAsr(params);
        String json = null; // 可以替换成自己的json
        json = new JSONObject(params).toString(); // 这里可以替换成你需要测试的json
        asr.send(event, json, null, 0, 0);
        //printLog("输入参数：" + json);
    }


    /**
     * 点击停止按钮
     * 基于SDK集成4.1 发送停止事件
     */
    private void stop() {
        //  printLog("停止识别：ASR_STOP");

        handler.removeCallbacks(runnable);
        asr.send(SpeechConstant.ASR_STOP, null, null, 0, 0); //
    }

    /**
     * @param name   回调事件
     * @param params 回调参数
     * @param data
     * @param offset
     * @param length 缓存临时数据，三者一起，生效部分为 data[offset] 开始，长度为length
     */
    @Override
    public void onEvent(String name, String params, byte[] data, int offset, int length) {
        String logTxt = "name: " + name;
        if (params != null && !params.isEmpty()) {
            logTxt += " ;params :" + params;
        }
        //检测到识别结果,梦话检测
        if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL)) {
            if (params != null && params.contains("\"nlu_result\"")) {
                if (length > 0 && data.length > 0) {
                    // logTxt += ", 语义解析结果：" + new String(data, offset, length);
                    Log.i("asa", " 语义解析结果：" + new String(data, offset, length));
                    //暂停，休眠 ，重新启动

                    //  onPause();
                    //   Log.i("CALLBACK_PARTIAL",logTxt);
                }
            }
        } else if (data != null) {
            logTxt += " ;data length=" + data.length;
        }
        //最终识别结束，呼吸声检测
        if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_FINISH)) {
            // Log.i("finish", "Aasd");
            //  asr.send(SpeechConstant.ASR_CANCEL, "{}", null, 0, 0);
//            onPause();
            //    isRecording=false;
//            setTimeout(3000);
//            start();
            //   stop();
        }
        //语音音量回调,r
        if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_VOLUME)) {
            if (params != null) {
                try {
                    //   Log.i("tetstt",params);
                    JSONObject jsonObject = new JSONObject(params);
                    int volume = Integer.parseInt(jsonObject.get("volume").toString());
                    //todo 计算最高值和平均值
                    double ratio = (double) volume / 3;
                    double db = 0;// 分贝
                    if (ratio > 1)
                        db = 20 * Math.log10(ratio);
                    if (highestdb < db)
                        highestdb = db;
                    Log.d("CALLBACK_VOLUME", "分贝值：" + db);

                    //    Log.i("CALLBACK_VOLUME", jsonObject.get("volume").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        //语音音频回调，文件转换pcm->wav
        if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_AUDIO)) {
            convertPcm2Wav(Environment.getExternalStorageDirectory().getPath() + "/AudioRecord/" + timearray.get(current) + ".pcm"
                    , Environment.getExternalStorageDirectory().getPath() + "/AudioRecord/" + timearray.get(current) + ".wav",
                    16000, 1, 16);
            //  Log.i("EVENT_ASR_AUDIO","out_put");
            // Log.v()
        }
    }


    /**
     * enableOffline设为true时，在onCreate中调用
     * 基于SDK离线命令词1.4 加载离线资源(离线时使用)
     */
    private void loadOfflineEngine() {
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        params.put(SpeechConstant.DECODER, 2);
        params.put(SpeechConstant.ASR_OFFLINE_ENGINE_GRAMMER_FILE_PATH, "assets://baidu_speech_grammar.bsg");
        asr.send(SpeechConstant.ASR_KWS_LOAD_ENGINE, new JSONObject(params).toString(), null, 0, 0);
    }

    /**
     * enableOffline为true时，在onDestory中调用，与loadOfflineEngine对应
     * 基于SDK集成5.1 卸载离线资源步骤(离线时使用)
     */
    private void unloadOfflineEngine() {
        asr.send(SpeechConstant.ASR_KWS_UNLOAD_ENGINE, null, null, 0, 0); //
    }


    protected void onPause() {
        // super.onPause();
        asr.send(SpeechConstant.ASR_CANCEL, "{}", null, 0, 0);
        Log.i("ActivityMiniRecog", "On pause");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 基于SDK集成4.2 发送取消事件
        asr.send(SpeechConstant.ASR_CANCEL, "{}", null, 0, 0);
        if (enableOffline) {
            unloadOfflineEngine(); // 测试离线命令词请开启, 测试 ASR_OFFLINE_ENGINE_GRAMMER_FILE_PATH 参数时开启
        }

        // 基于SDK集成5.2 退出事件管理器
        // 必须与registerListener成对出现，否则可能造成内存泄露
        asr.unregisterListener(this);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // Log.i("binder","seeesssssssssss");
        return new MyBinder();
    }

    /**
     * 创建一个内部类 提供一个方法，可以间接调用服务的方法
     */
    public class MyBinder extends Binder {
        public AudioRecorderService getservice() {
            return AudioRecorderService.this;
        }

        public void stopRecord() {
            stop();
        }

        public void startRecord() {
            loopRecord();
        }
    }

    /**
     * PCM文件转WAV文件
     *
     * @param inPcmFilePath  输入PCM文件路径
     * @param outWavFilePath 输出WAV文件路径
     * @param sampleRate     采样率，例如44100
     * @param channels       声道数 单声道：1或双声道：2
     * @param bitNum         采样位数，8或16
     *                       音频格式为pcm，16000采样率，16bit，单声道，小端序；
     */
    public static void convertPcm2Wav(String inPcmFilePath, String outWavFilePath, int sampleRate,
                                      int channels, int bitNum) {

        FileInputStream in = null;
        FileOutputStream out = null;
        byte[] data = new byte[1024];

        try {
            //采样字节byte率
            long byteRate = sampleRate * channels * bitNum / 8;

            in = new FileInputStream(inPcmFilePath);
            out = new FileOutputStream(outWavFilePath);

            //PCM文件大小
            long totalAudioLen = in.getChannel().size();

            //总大小，由于不包括RIFF和WAV，所以是44 - 8 = 36，在加上PCM文件大小
            long totalDataLen = totalAudioLen + 36;

            writeWaveFileHeader(out, totalAudioLen, totalDataLen, sampleRate, channels, byteRate);

            int length = 0;
            while ((length = in.read(data)) > 0) {
                out.write(data, 0, length);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 输出WAV文件
     *
     * @param out           WAV输出文件流
     * @param totalAudioLen 整个音频PCM数据大小
     * @param totalDataLen  整个数据大小
     * @param sampleRate    采样率
     * @param channels      声道数
     * @param byteRate      采样字节byte率
     * @throws IOException
     */
    private static void writeWaveFileHeader(FileOutputStream out, long totalAudioLen,
                                            long totalDataLen, int sampleRate, int channels, long byteRate) throws IOException {
        byte[] header = new byte[44];
        header[0] = 'R'; // RIFF
        header[1] = 'I';
        header[2] = 'F';
        header[3] = 'F';
        header[4] = (byte) (totalDataLen & 0xff);//数据大小
        header[5] = (byte) ((totalDataLen >> 8) & 0xff);
        header[6] = (byte) ((totalDataLen >> 16) & 0xff);
        header[7] = (byte) ((totalDataLen >> 24) & 0xff);
        header[8] = 'W';//WAVE
        header[9] = 'A';
        header[10] = 'V';
        header[11] = 'E';
        //FMT Chunk
        header[12] = 'f'; // 'fmt '
        header[13] = 'm';
        header[14] = 't';
        header[15] = ' ';//过渡字节
        //数据大小
        header[16] = 16; // 4 bytes: size of 'fmt ' chunk
        header[17] = 0;
        header[18] = 0;
        header[19] = 0;
        //编码方式 10H为PCM编码格式
        header[20] = 1; // format = 1
        header[21] = 0;
        //通道数
        header[22] = (byte) channels;
        header[23] = 0;
        //采样率，每个通道的播放速度
        header[24] = (byte) (sampleRate & 0xff);
        header[25] = (byte) ((sampleRate >> 8) & 0xff);
        header[26] = (byte) ((sampleRate >> 16) & 0xff);
        header[27] = (byte) ((sampleRate >> 24) & 0xff);
        //音频数据传送速率,采样率*通道数*采样深度/8
        header[28] = (byte) (byteRate & 0xff);
        header[29] = (byte) ((byteRate >> 8) & 0xff);
        header[30] = (byte) ((byteRate >> 16) & 0xff);
        header[31] = (byte) ((byteRate >> 24) & 0xff);
        // 确定系统一次要处理多少个这样字节的数据，确定缓冲区，通道数*采样位数
        header[32] = (byte) (channels * 16 / 8);
        header[33] = 0;
        //每个样本的数据位数
        header[34] = 16;
        header[35] = 0;
        //Data chunk
        header[36] = 'd';//data
        header[37] = 'a';
        header[38] = 't';
        header[39] = 'a';
        header[40] = (byte) (totalAudioLen & 0xff);
        header[41] = (byte) ((totalAudioLen >> 8) & 0xff);
        header[42] = (byte) ((totalAudioLen >> 16) & 0xff);
        header[43] = (byte) ((totalAudioLen >> 24) & 0xff);
        out.write(header, 0, 44);
    }
}
