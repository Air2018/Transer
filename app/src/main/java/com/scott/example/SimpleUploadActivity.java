package com.scott.example;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.scott.annotionprocessor.ITask;
import com.scott.example.utils.Contacts;
import com.scott.example.utils.TaskUtils;
import com.scott.transer.HandlerParamNames;
import com.scott.transer.SimpleTaskHandlerListenner;
import com.scott.transer.TaskBuilder;
import com.scott.transer.handler.BaseTaskHandler;
import com.scott.transer.handler.DefaultHttpUploadHandler;
import com.scott.transer.handler.ITaskHandler;
import com.scott.transer.utils.Debugger;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.scott.example.utils.TaskUtils.getFileSize;

public class SimpleUploadActivity extends AppCompatActivity {

    @BindView(R.id.tv_name)
    TextView tvName;

    @BindView(R.id.tv_complete_length)
    TextView tvCompleteLength;

    @BindView(R.id.tv_all_length)
    TextView tvAllLength;

    @BindView(R.id.progress_length)
    ProgressBar progressLength;

    @BindView(R.id.tv_md5)
    TextView tvMd5;

    @BindView(R.id.tv_md5_new)
    TextView tvNewMd5;

    @BindView(R.id.tv_equals)
    TextView tvEquals;

    @BindView(R.id.tv_speed)
    TextView tvSpeed;

    private ITaskHandler mHandler;

    final String URL = "http://" + Contacts.TEST_HOST + "/WebDemo/UploadManager";
    final String FILE_PATH = Environment.getExternalStorageDirectory().toString() + File.separator + "test.zip";
    final String TAG = SimpleUploadActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_only_download);

        ButterKnife.bind(this);

        mHandler = new DefaultHttpUploadHandler();
        Map<String,String> params = new HashMap<>();
        params.put("path","test.zip");
        params.put(HandlerParamNames.PARAM_SPEED_LIMITED, BaseTaskHandler.SPEED_LISMT.SPEED_100KB + "");

        final ITask task = new TaskBuilder()
                .setName("test.zip")
                .setTaskId("1233444")
                .setSessionId("123123123131")
                .setDataSource(FILE_PATH)
                .setDestSource(URL)
                .build();
        mHandler.setTask(task);
        mHandler.setParams(params);
        mHandler.setHandlerListenner(new SimpleTaskHandlerListenner() {
            @Override
            public void onPiceSuccessful(final ITask params) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvCompleteLength.setText(getFileSize(params.getCompleteLength()));
                        tvAllLength.setText(getFileSize(params.getLength()));

                        double progress = (double)params.getCompleteLength() / (double)params.getLength();
                        progress = progress * 100f;
                        progressLength.setProgress((int) progress);
                    }
                });
            }

            @Override
            public void onFinished(final ITask task) {
                super.onFinished(task);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvCompleteLength.setText(getFileSize(task.getCompleteLength()));
                        tvAllLength.setText(getFileSize(task.getLength()));

                        double progress = (double)task.getCompleteLength() / (double)task.getLength();
                        progress = progress * 100f;
                        progressLength.setProgress((int) progress);
                    }
                });
                Debugger.error(TAG,"========onFinished============");
            }

            @Override
            public void onSpeedChanged(long speed, final ITask params) {
                super.onSpeedChanged(speed, params);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvCompleteLength.setText(getFileSize(params.getCompleteLength()));
                        tvAllLength.setText(getFileSize(params.getLength()));

                        double progress = (double)params.getCompleteLength() / (double)params.getLength();
                        progress = progress * 100f;
                        progressLength.setProgress((int) progress);
                        tvSpeed.setText(TaskUtils.getFileSize(task.getSpeed()));
                    }
                });
                Debugger.error("OnlyDownloadActivity","speed = " + getFileSize(speed) + "/s");
            }

            @Override
            public void onError(int code, ITask params) {
                super.onError(code, params);
                Debugger.error("SimpleUploadActivity","error " + code);
            }
        });

        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(3,3,
                6000, TimeUnit.MILLISECONDS,new ArrayBlockingQueue<Runnable>(10000));
        mHandler.setThreadPool(threadPool);
    }

    @OnClick(R.id.btn_stop)
    public void stop() {
        mHandler.stop();
    }


    @OnClick(R.id.btn_start)
    public void start() {
        mHandler.start();
    }
}
