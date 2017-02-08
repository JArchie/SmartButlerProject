package com.jarchie.smartbutler.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.TextView;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.jarchie.smartbutler.R;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.ProgressListener;
import com.kymjs.rxvolley.http.VolleyError;
import com.kymjs.rxvolley.toolbox.FileUtils;
import java.io.File;

/**
 * 项目名:   SmartButler
 * 包名:     com.jarchie.smartbutler.ui
 * 文件名:   UpdateVersionActivity
 * 创建者:   Jarchie
 * 创建时间: 17/2/1 下午7:03
 * 描述:     版本更新,下载apk
 */

public class UpdateVersionActivity extends BaseActivity {
    //正在下载
    public static final int HANDLER_LOADING = 10001;
    //下载完成
    public static final int HANDLER_OK = 10002;
    //下载失败
    public static final int HANDLER_ON = 10003;
    private TextView sizeText;
    private String url;
    private String path;
    //进度条
    private NumberProgressBar numProgressBar;

    //采用Handler更新UI
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLER_LOADING:
                    //实时更新进度
                    Bundle bundle = msg.getData();
                    long transferredBytes = bundle.getLong("transferredBytes");
                    long totalSize = bundle.getLong("totalSize");
                    sizeText.setText(transferredBytes + "/" + totalSize);
                    numProgressBar.setProgress((int) (((float) transferredBytes / (float) totalSize) * 100));
                    break;
                case HANDLER_OK:
                    sizeText.setText("下载成功");
                    //完成时启动应用安装
                    startInstallApk();
                    break;
                case HANDLER_ON:
                    sizeText.setText("下载失败");
                    break;
            }
        }
    };

    //启动安装apk
    private void startInstallApk() {
        Intent intent = new Intent();
        intent.setAction(intent.ACTION_VIEW);
        intent.addCategory(intent.CATEGORY_DEFAULT);
        intent.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive");
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        initView();
    }

    //初始化View
    private void initView() {
        sizeText = (TextView) findViewById(R.id.tv_size);
        numProgressBar = (NumberProgressBar) findViewById(R.id.number_progress_bar);
        numProgressBar.setMax(100);

        path = FileUtils.getSDCardPath() + "/" + System.currentTimeMillis() + ".apk";
        //下载
        url = getIntent().getStringExtra("url");
        if (!TextUtils.isEmpty(url)) {
            RxVolley.download(path, url, new ProgressListener() {
                @Override
                public void onProgress(long transferredBytes, long totalSize) {
                    Message msg = new Message();
                    msg.what = HANDLER_LOADING;
                    Bundle bundle = new Bundle();
                    bundle.putLong("transferredBytes", transferredBytes);
                    bundle.putLong("totalSize", totalSize);
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                }
            }, new HttpCallback() {
                @Override
                public void onSuccess(String t) {
                    handler.sendEmptyMessage(HANDLER_OK);
                }

                @Override
                public void onFailure(VolleyError error) {
                    handler.sendEmptyMessage(HANDLER_ON);
                }
            });
        }
    }
}
