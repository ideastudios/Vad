package tech.oom.volume;

import android.media.AudioFormat;
import android.util.Log;

/**
 * 基于音量大小来判断是否没有语音活动的工具类,主要用于静音停止的判断
 * 调用{@link VolumeVad#processBuffer(short[])} 为true 表示当前判断没有检测到语音活动.可以停止录音
 * false表示有检测到语音活动
 * 通过音量检测vad的逻辑
 */
public class VolumeVad {
    private static final String TAG = "VolumeVad";
    /**
     * 说话时的最小分贝
     */
    private static final float TALKING_DB = 60f;
    /**
     * 正常背景分贝
     */
    private static final float NORMAL_BG_DB = 48f;
    private static volatile VolumeVad instance;
    private int lessQuarterContinuousTime;
    private long lessNormalContinuousTime;
    private int lessRangeContinuousTime;
    private float maxVolume;
    private float minVolume;
    /**
     * 连续达到静音停止时长
     */
    private int timeMillis = 1000;
    /**
     * 音量连续小于最大音量和最小音量相差的四分之一的时长
     */
    private int lessQuarterTimeMillis = 500;
    private RecordConfig recordConfig;

    private VolumeVad() {

    }

    public static VolumeVad getInstance() {
        if (instance == null) {
            synchronized (VolumeVad.class) {
                if (instance == null) {
                    instance = new VolumeVad();
                }
            }
        }
        return instance;
    }

    public void setRecordConfig(RecordConfig recordConfig) {
        this.recordConfig = recordConfig;
    }

    private int getChannel() {
        if (recordConfig.getChannelConfig() == AudioFormat.CHANNEL_IN_MONO) {
            return 1;
        } else {
            return 2;
        }
    }

    private int getbSamples() {
        if (recordConfig.getAudioFormat() == AudioFormat.ENCODING_PCM_16BIT) {
            return 16;
        } else {
            return 8;
        }
    }

    private int getSampleRate() {
        return recordConfig.getSampleRate();
    }

    /**
     * @param wave 语音buffer
     * @return 当前是否静音 true表示没有检测到语音活动，false表示有检测到语音活动
     */
    public boolean processBuffer(short[] wave) {
        if (recordConfig == null) {
            Log.e("VolumeVad", "record config is null");
            throw new NullPointerException("record config is null");
        }
        //1s为1000ms 1 short = 2*byte
        long currentLengthInMillis = 1000 * 2 * wave.length / (getSampleRate() * getbSamples() * getChannel() / 8);
        float volume = calculateVolume(wave);
        if (minVolume == 0 && maxVolume == 0) {
            minVolume = volume;
            maxVolume = volume;
            return false;
        }
        //如果音量大小小于正常办公室背景音量大小
        if (volume < NORMAL_BG_DB) {
            lessNormalContinuousTime += currentLengthInMillis;
        } else {
            lessNormalContinuousTime = 0;
        }


        float quarter = (maxVolume - minVolume) / 4;
        //如果当前音量小于区间四分之一
        if (quarter >= 1 && volume < minVolume + quarter) {
            lessQuarterContinuousTime += currentLengthInMillis;
        } else {
            lessQuarterContinuousTime = 0;
        }

        maxVolume = Math.max(maxVolume, volume);
        minVolume = Math.min(minVolume, volume);

        //如果最大音量未超过正常说话音量大小，并且最大音量与最小音量相差不超过10db
        if (maxVolume <= TALKING_DB && maxVolume - minVolume <= 10) {
            lessRangeContinuousTime += currentLengthInMillis;
        } else {
            lessRangeContinuousTime = 0;
        }

        if (lessQuarterContinuousTime >= lessQuarterTimeMillis || lessNormalContinuousTime >= timeMillis || lessRangeContinuousTime >= timeMillis) {
            reset();
            return true;
        }

        return false;
    }

    public void reset() {
        lessQuarterContinuousTime = 0;
        lessNormalContinuousTime = 0;
        maxVolume = 0;
        minVolume = 0;
        lessRangeContinuousTime = 0;
    }

    /**
     * @param buffer 语音buffer
     * @return 当前是否静音 true表示没有检测到语音活动，false表示有检测到语音活动
     */
    public boolean processBuffer(byte[] buffer) {
        short[] shorts = BytesTransUtil.getInstance().Bytes2Shorts(buffer);
        return processBuffer(shorts);
    }

    /**
     * 设置时长阈值，当判断连续静音总时长大于timeInMillis时，processBuffer 返回true
     *
     * @param timeInMillis
     */
    public void setTimeMillis(int timeInMillis) {
        this.timeMillis = timeInMillis;
    }

    /**
     * 设置阈值，当音量连续小于最大音量与最小音量之差的四分之一 的时长大于lessQuarterTimeMillis时, processBuffer返回true
     *
     * @param lessQuarterTimeMillis
     */
    public void setLessQuarterTimeMillis(int lessQuarterTimeMillis) {
        this.lessQuarterTimeMillis = lessQuarterTimeMillis;
    }

    /**
     * 计算当前音量大小
     *
     * @param wave 语音buffer
     * @return 当前音量大小
     */
    private float calculateVolume(short[] wave) {
        long v = 0;
        // 将 buffer 内容取出，进行平方和运算
        for (int i = 0; i < wave.length; i++) {
            v += wave[i] * wave[i];
        }
        // 平方和除以数据总长度，得到音量大小。
        double mean = v / (double) wave.length;
        double volume = 10 * Math.log10(mean);
        return (float) volume;
    }

}
