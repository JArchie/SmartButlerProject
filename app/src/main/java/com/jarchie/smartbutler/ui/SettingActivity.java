package com.jarchie.smartbutler.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.jarchie.smartbutler.R;
import com.jarchie.smartbutler.service.SmsService;
import com.jarchie.smartbutler.utils.ShareUtil;
import com.jarchie.smartbutler.utils.StaticClass;
import com.jarchie.smartbutler.utils.UtilTools;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.xys.libzxing.zxing.activity.CaptureActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 项目名:   SmartButler
 * 包名:     com.jarchie.smartbutler.ui
 * 文件名:   SettingActivity
 * 创建者:   Jarchie
 * 创建时间: 17/1/15 下午3:44
 * 描述:     设置页面
 */

public class SettingActivity extends BaseActivity implements View.OnClickListener {
    //语音播报
    private Switch mSwitch;
    //短信提醒
    private Switch smsSwitch;
    //版本更新
    private LinearLayout llUpdate;
    private TextView versionText;
    private String versionName;
    private int versionCode;
    private String url;
    //扫一扫
    private LinearLayout scanLayout;
    private TextView scanResultText;
    //我的二维码
    private LinearLayout qrCode;
    //我的位置
    private LinearLayout mylocation;
    //关于软件
    private LinearLayout aboutSoft;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
    }

    //初始化View
    private void initView() {
        //语音播报部分
        mSwitch = (Switch) findViewById(R.id.sw_speak);
        mSwitch.setOnClickListener(this);
        boolean isSpeak = ShareUtil.getBoolean(this, StaticClass.VOICE_FLAG, false);
        mSwitch.setChecked(isSpeak);
        //短信提醒部分
        smsSwitch = (Switch) findViewById(R.id.sw_sms);
        smsSwitch.setOnClickListener(this);
        boolean isSms = ShareUtil.getBoolean(this, StaticClass.SMS_FLAG, false);
        smsSwitch.setChecked(isSms);
        //版本更新部分
        llUpdate = (LinearLayout) findViewById(R.id.ll_update);
        llUpdate.setOnClickListener(this);
        versionText = (TextView) findViewById(R.id.tv_version);
        try {
            getVersionNameCode();
            versionText.setText("检测版本:" + versionName);
        } catch (PackageManager.NameNotFoundException e) {
            versionText.setText("检测版本");
        }
        //扫一扫
        scanLayout = (LinearLayout) findViewById(R.id.ll_scan);
        scanLayout.setOnClickListener(this);
        scanResultText = (TextView) findViewById(R.id.tv_scanresult);
        //我的二维码
        qrCode = (LinearLayout) findViewById(R.id.ll_qrcode);
        qrCode.setOnClickListener(this);
        //我的位置
        mylocation = (LinearLayout) findViewById(R.id.ll_mylocation);
        mylocation.setOnClickListener(this);
        //关于软件
        aboutSoft = (LinearLayout) findViewById(R.id.ll_about);
        aboutSoft.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //智能语音
            case R.id.sw_speak:
                //切换状态
                mSwitch.setSelected(!mSwitch.isSelected());
                //保存状态
                ShareUtil.putBoolean(this, StaticClass.VOICE_FLAG, mSwitch.isChecked());
                break;
            //短信提醒
            case R.id.sw_sms:
                //切换相反
                smsSwitch.setSelected(!smsSwitch.isSelected());
                //保存状态
                ShareUtil.putBoolean(this, StaticClass.SMS_FLAG, smsSwitch.isChecked());
                if (smsSwitch.isChecked()) {
                    startService(new Intent(this, SmsService.class));
                } else {
                    stopService(new Intent(this, SmsService.class));
                }
                break;
            //版本更新
            case R.id.ll_update:
                RxVolley.get(StaticClass.CHECK_UPDATE_URL, new HttpCallback() {
                    @Override
                    public void onSuccess(String t) {
                        parserJsonData(t);
                    }
                });
                break;
            //扫一扫
            case R.id.ll_scan:
                //打开扫描界面扫描条形码或者二维码
                Intent openCameraIntent = new Intent(this, CaptureActivity.class);
                startActivityForResult(openCameraIntent, 0);
                break;
            //我的二维码
            case R.id.ll_qrcode:
                startActivity(new Intent(this,QRCodeActivity.class));
                break;
            //我的位置
            case R.id.ll_mylocation:
                startActivity(new Intent(this,LocationActivity.class));
                break;
            //关于软件
            case R.id.ll_about:
                startActivity(new Intent(this,AboutSoftActivity.class));
                break;
        }
}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            scanResultText.setText(scanResult);
        }
    }

    //解析Json数据
    private void parserJsonData(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t);
            int code = jsonObject.getInt("versionCode");
            String content = jsonObject.getString("content");
            url = jsonObject.getString("url");
            if (code > versionCode) {
                showUpdateDialog(content);
            } else {
                UtilTools.toastShortMessage(this, "当前已经是最新版本了!");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //弹出升级提示
    private void showUpdateDialog(String content) {
        new AlertDialog.Builder(this)
                .setTitle("有新版本啦!")
                .setMessage(content)
                .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(SettingActivity.this, UpdateVersionActivity.class);
                        intent.putExtra("url", url);
                        startActivity(intent);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //执行dismiss方法
            }
        }).show();
    }

    //获取版本号/Code
    private void getVersionNameCode() throws PackageManager.NameNotFoundException {
        PackageManager pm = getPackageManager();
        PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
        versionName = info.versionName;
        versionCode = info.versionCode;
    }

}
