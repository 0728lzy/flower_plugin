package com.qingchu.wangmiao.utils;

import android.annotation.SuppressLint;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

public class AudioRecordUtil {

    private static final String TAG = "AudioRecordUtil";
    private AudioRecord audioRecord;
    private int bufferSize;
    private int readSize;
    private boolean isStart;

    private int sample_rate = 44100;

    // 新增字段：是否检测到声音
    private volatile boolean hasVoice = false;

    @SuppressLint("MissingPermission")
    public AudioRecordUtil() {
        bufferSize = AudioRecord.getMinBufferSize(
                sample_rate,
                AudioFormat.CHANNEL_IN_STEREO,
                AudioFormat.ENCODING_PCM_16BIT);
        audioRecord = new AudioRecord(
                MediaRecorder.AudioSource.MIC,
                sample_rate,
                AudioFormat.CHANNEL_IN_STEREO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSize
        );
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
     * 开始录音
     */
    public void startRecord() {
        new Thread(() -> {
            isStart = true;
            hasVoice = false; // 每次开始时重置
            audioRecord.startRecording();

            byte[] audioData = new byte[bufferSize];
            if (onRecordListener != null) {
                onRecordListener.isStart();
            }

            while (isStart) {
                readSize = audioRecord.read(audioData, 0, bufferSize);
                if (readSize > 0) {
                    // 检测是否有声音
                    if (detectVoice(audioData, readSize)) {
                        hasVoice = true;
                    }

                    if (onRecordListener != null) {
                        onRecordListener.readByte(audioData, readSize);
                    }
                }
            }

            // 释放资源
            audioRecord.stop();
            audioRecord.release();
            audioRecord = null;

            if (onCompleteListener != null) {
                onCompleteListener.onComplete();
            }
        }).start();
    }
    public boolean stopRecord() {
        isStart = false;
        return hasVoice;
    }
    private boolean detectVoice(byte[] buffer, int size) {
        long totalEnergy = 0;
        // 16bit PCM => 每两个字节一个采样
        for (int i = 0; i < size; i += 2) {
            short sample = (short) ((buffer[i] & 0xFF) | (buffer[i + 1] << 8));
            totalEnergy += Math.abs(sample);
        }
        double average = totalEnergy / (size / 2.0);
        boolean hasSound = average > 1200;
        if (hasSound) {
            Log.d(TAG, "检测到声音, 平均幅度=" + average);
        }
        return hasSound;
    }
}
