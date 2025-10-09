package com.qingchu.wangmiao.utils;


import android.annotation.SuppressLint;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

public class AudioRecordUtil {

    private static final String TAG = "AudioRecordUtil";
    private AudioRecord audioRecord;
    private int bufferSize;
    private int readSize;
    private boolean isStart;

    private int sample_rate = 44100;

    @SuppressLint("MissingPermission")
    public AudioRecordUtil() {
        bufferSize = AudioRecord.getMinBufferSize(
                sample_rate,
                AudioFormat.CHANNEL_IN_STEREO,
                AudioFormat.ENCODING_PCM_16BIT);
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                sample_rate,
                AudioFormat.CHANNEL_IN_STEREO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSize);
    }

    private OnRecordListener onRecordListener;

    private OnCompleteListener onCompleteListener;

    public void setOnRecordListener(OnRecordListener onRecordListener) {
        this.onRecordListener = onRecordListener;
    }

    public void setOnCompleteListener(OnCompleteListener onCompleteListener) {
        this.onCompleteListener = onCompleteListener;
    }

    public interface OnRecordListener {
        void isStart();

        void readByte(byte[] audioData, int size);
    }

    public interface OnCompleteListener {
        void onComplete();
    }

    /**
     * 开始
     */
    public void startRecord() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                isStart = true;
                audioRecord.startRecording();
                byte[] audioData = new byte[bufferSize];
                if (onRecordListener != null) {
                    onRecordListener.isStart();
                }
                while (isStart) {
                    readSize = audioRecord.read(audioData, 0, bufferSize);
                    if (onRecordListener != null) {
                        onRecordListener.readByte(audioData, readSize);
                    }
                }
                // 释放
                audioRecord.stop();
                audioRecord.release();
                audioRecord = null;
                if (onCompleteListener != null) {
                    onCompleteListener.onComplete();
                }
            }
        }).start();
    }

    /**
     * 停止
     */
    public void stopRecord() {
        isStart = false;
    }
}
