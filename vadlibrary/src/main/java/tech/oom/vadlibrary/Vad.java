package tech.oom.vadlibrary;

/**
 * Vad class based on webrtc  only 48000kbps 32000kbps 16000kbps 8000kbps sample rate buffer are supported
 * Highly recommend you used @see <a href="https://github.com/ideastudios/IdealRecorder">IdealRecorder</a>
 * and set the sample rate you want
 */
public class Vad {

    static {
        System.loadLibrary("oomvad-lib");
    }

    /**
     * default vad mode
     */
    private int mode = 1;

    /**
     * process buffer and return whether is vad is active
     *
     * @param sampleRate should be one of 48000 32000 16000 8000
     * @param audioData  audio data buffer in short
     * @param dataLength the audio data length
     * @return active      true means active  false means inactive(silence)
     */
    public boolean processBuffer(int sampleRate, short[] audioData, int dataLength) {
        if (audioData == null || audioData.length == 0 || dataLength == 0) {
            return false;
        }
        if (sampleRate == 48000 || sampleRate == 32000 || sampleRate == 16000 || sampleRate == 8000) {
            int size10seconds = sampleRate / 100;
            int size20seconds = size10seconds * 2;
            int length = size20seconds;
            int index = dataLength / size20seconds;
            int trueCount = 0;
            int falseCount = 0;
            for (int i = 0; i < index; i++) {
                if (i == index - 1) {
                    length = dataLength - i * size20seconds;
                }
                short[] data20seconds = new short[length];
                System.arraycopy(audioData, i * size20seconds, data20seconds, 0, length);
                boolean vad = process(sampleRate, data20seconds, 0, data20seconds.length);

                if (vad) {
                    trueCount++;
                } else {
                    falseCount++;
                }

                if (trueCount >= falseCount) {
                    return true;
                } else {
                    return false;
                }
            }
            return false;
        } else {
            return false;
        }

    }

    private boolean process(int sampleRate, short[] audioData, int offsetInShort, int readSize) {

        return vad(sampleRate, mode, audioData, offsetInShort, readSize);
    }

    public native boolean vad(int sampleSize, int mode, short[] audioData, int offsetInShort, int readSize);

    /**
     * vad aggressiveness mode A more aggressive (higher mode) VAD is more restrictive in reporting speech.
     *
     * @param mode Aggressiveness mode (0, 1, 2, or 3),
     */
    public void setMode(int mode) {
        if (mode >= 0 && mode <= 3) {
            this.mode = mode;
        }
    }

}
