package tech.oom.volume;

import android.media.AudioFormat;
import android.media.MediaRecorder;

public class RecordConfig {
    public static final int SAMPLE_RATE_44K_HZ = 44100;
    public static final int SAMPLE_RATE_22K_HZ = 22050;
    public static final int SAMPLE_RATE_16K_HZ = 16000;
    public static final int SAMPLE_RATE_11K_HZ = 11025;
    public static final int SAMPLE_RATE_8K_HZ = 8000;
    private int audioSource = MediaRecorder.AudioSource.MIC;
    private int sampleRate = SAMPLE_RATE_16K_HZ;
    private int channelConfig = AudioFormat.CHANNEL_IN_MONO;
    private int audioFormat = AudioFormat.ENCODING_PCM_16BIT;

    /**
     * 录音配置的构造方法
     *
     * @param audioSource   the recording source.
     *                      See {@link MediaRecorder.AudioSource} for the recording source definitions.
     *                      recommend {@link MediaRecorder.AudioSource#MIC}
     * @param sampleRate    the sample rate expressed in Hertz. {@link RecordConfig#SAMPLE_RATE_44K_HZ} is Recommended ,
     *                      {@link RecordConfig#SAMPLE_RATE_22K_HZ},{@link RecordConfig#SAMPLE_RATE_16K_HZ},
     *                      {@link RecordConfig#SAMPLE_RATE_11K_HZ},{@link RecordConfig#SAMPLE_RATE_8K_HZ}
     * @param channelConfig describes the configuration of the audio channels.
     *                      See {@link AudioFormat#CHANNEL_IN_MONO} and
     *                      {@link AudioFormat#CHANNEL_IN_STEREO}.  {@link AudioFormat#CHANNEL_IN_MONO} is guaranteed
     *                      to work on all devices.
     * @param audioFormat   the format in which the audio data is to be returned.
     *                      See {@link AudioFormat#ENCODING_PCM_8BIT}, {@link AudioFormat#ENCODING_PCM_16BIT},
     *                      and {@link AudioFormat#ENCODING_PCM_FLOAT}.
     */
    public RecordConfig(int audioSource, int sampleRate, int channelConfig, int audioFormat) {
        this.audioSource = audioSource;
        this.sampleRate = sampleRate;
        this.channelConfig = channelConfig;
        this.audioFormat = audioFormat;
    }

    /**
     * 录音配置的构造方法
     */
    public RecordConfig() {

    }

    public int getAudioSource() {
        return audioSource;
    }

    /**
     * @param audioSource the recording source. 设置录音的来源
     *                    See {@link MediaRecorder.AudioSource} for the recording source definitions.
     *                    recommend {@link MediaRecorder.AudioSource#MIC}
     */
    public RecordConfig setAudioSource(int audioSource) {
        this.audioSource = audioSource;
        return this;
    }

    public int getSampleRate() {
        return sampleRate;
    }

    /**
     * @param sampleRate set the sample rate  it can be set设置采样率，可以是以下的任何一个  {@link RecordConfig#SAMPLE_RATE_44K_HZ}
     *                   {@link RecordConfig#SAMPLE_RATE_22K_HZ},{@link RecordConfig#SAMPLE_RATE_16K_HZ},
     *                   {@link RecordConfig#SAMPLE_RATE_11K_HZ},{@link RecordConfig#SAMPLE_RATE_8K_HZ}
     */
    public RecordConfig setSampleRate(int sampleRate) {
        this.sampleRate = sampleRate;
        return this;
    }

    public int getChannelConfig() {
        return channelConfig;
    }

    /**
     * @param channelConfig set the configuration of the audio channels.设置当前录音的通道数，可以是
     *                      {@link AudioFormat#CHANNEL_IN_MONO} 单通道也可以是
     *                      {@link AudioFormat#CHANNEL_IN_STEREO}双通道.
     */
    public RecordConfig setChannelConfig(int channelConfig) {
        this.channelConfig = channelConfig;
        return this;
    }

    public int getAudioFormat() {
        return audioFormat;
    }

    /**
     * @param audioFormat set the format in which the audio data is to be returned. 设置当前录音的采样位数 可以是
     *                    {@link AudioFormat#ENCODING_PCM_8BIT} 8位 或者{@link AudioFormat#ENCODING_PCM_16BIT} 16位
     */
    public RecordConfig setAudioFormat(int audioFormat) {
        this.audioFormat = audioFormat;
        return this;
    }

}
