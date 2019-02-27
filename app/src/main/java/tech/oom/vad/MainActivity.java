package tech.oom.vad;

import android.content.DialogInterface;
import android.media.AudioFormat;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.util.List;

import tech.oom.idealrecorder.IdealRecorder;
import tech.oom.idealrecorder.StatusListener;
import tech.oom.vadlibrary.Vad;

public class MainActivity extends AppCompatActivity {

    private Vad vad = new Vad();

    private IdealRecorder idealRecorder;

    private IdealRecorder.RecordConfig recordConfig;
    private RationaleListener rationaleListener = new RationaleListener() {
        @Override
        public void showRequestPermissionRationale(int requestCode, final Rationale rationale) {
            com.yanzhenjie.alertdialog.AlertDialog.newBuilder(MainActivity.this)
                    .setTitle("Reminder")
                    .setMessage("I need recorder permission and read file permission")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            rationale.resume();
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    rationale.cancel();
                }
            }).create().show();
        }
    };
    private StatusListener statusListener = new StatusListener() {
        @Override
        public void onStartRecording() {
            Toast.makeText(MainActivity.this, "recorder started", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRecordData(short[] data, int length) {
            if (length == 0 || data.length != length) {

                return;
            }
            boolean mSpeaking = vad.processBuffer(48000, data, length);

            if (mSpeaking) {
                Log.d("Vad", "---Active Voice---");
            } else {
                Log.d("Vad", "---Non-active Voice---");
            }


        }

        @Override
        public void onVoiceVolume(int volume) {

        }

        @Override
        public void onRecordError(int code, String errorMsg) {

        }

        @Override
        public void onFileSaveFailed(String error) {
        }

        @Override
        public void onFileSaveSuccess(String fileUri) {
        }

        @Override
        public void onStopRecording() {
            Toast.makeText(MainActivity.this, "recorder stopped", Toast.LENGTH_SHORT).show();
        }

    };
    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {

            if (requestCode == 100) {
                record();
            }
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {

            if (requestCode == 100) {
                Toast.makeText(MainActivity.this, "I couldn't do it without  permission", Toast.LENGTH_SHORT).show();
            }
            if (AndPermission.hasAlwaysDeniedPermission(MainActivity.this, deniedPermissions)) {
                AndPermission.defaultSettingDialog(MainActivity.this, 300).show();
            }
        }

    };
    private Button start, stop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start = findViewById(R.id.button1);
        stop = findViewById(R.id.button2);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readyRecord();
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopRecord();
            }
        });
        IdealRecorder.getInstance().init(this);
        idealRecorder = IdealRecorder.getInstance();
        recordConfig = new IdealRecorder.RecordConfig(MediaRecorder.AudioSource.MIC, 48000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
    }

    /**
     * ready record request permission
     */
    private void readyRecord() {

        AndPermission.with(this)
                .requestCode(100)
                .permission(Permission.MICROPHONE, Permission.STORAGE)
                .rationale(rationaleListener).callback(listener).start();

    }

    /**
     * start record
     */
    private void record() {
        //如果需要保存录音文件  设置好保存路径就会自动保存  也可以通过onRecordData 回调自己保存  不设置 不会保存录音
        //设置录音配置 最长录音时长 以及音量回调的时间间隔
        idealRecorder.setRecordConfig(recordConfig).setMaxRecordTime(6000).setVolumeInterval(100);
        //设置录音时各种状态的监听
        idealRecorder.setStatusListener(statusListener);
        idealRecorder.start(); //开始录音

    }


    /**
     * stop record
     */
    private void stopRecord() {
        //停止录音
        idealRecorder.stop();
    }
}
